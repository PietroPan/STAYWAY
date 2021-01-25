import Client.Response;
import Client.ResponsePair;
import Client.ResponsePairString;
import Client.ResponseStringsInts;
import Data.SystemInfo;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerSession implements Runnable{
    private Socket s;
    private final TaggedConnectionServer connection; // hmmm deixar o final ou mudar o try-with-resources
    private SystemInfo SI;
    private String name;

    public ServerSession(Socket s, SystemInfo SI) throws IOException {
        this.s=s;
        this.connection = new TaggedConnectionServer(s);
        this.SI=SI;
    }

    public ServerSession(TaggedConnectionServer tc, SystemInfo SI) throws IOException {
        this.connection = tc;
        this.SI=SI;
    }

    @Override
    public void run() {
        try (this.connection) {
            Response request = connection.receive();
            int tag = request.getTag();
            while (tag!=10) {
                switch(tag) {
                    case 0:
                        ResponsePairString par = (ResponsePairString) request;
                        String username = par.getFirst();
                        String password = par.getSecond();
                        this.name = username;
                        int i = SI.login(username, password);
                        connection.loginReply(i);
                        break;

                    case 1:
                        ResponseStringsInts sti = (ResponseStringsInts) request;
                        username = sti.getFirstStr();
                        password = sti.getSecondStr();
                        int x = sti.getFirstInt();
                        int y = sti.getSecondInt();
                        this.name = username;
                        boolean b = SI.register(username, password, x, y);
                        connection.registerReply(b);
                        break;

                    case 2:
                        ResponsePair parInt = (ResponsePair) request;
                        SI.changeLocation(this.name, parInt.getX(), parInt.getY());
                        break;
                        
                    case 3:
                        ResponsePair p = (ResponsePair) request;
                        x = p.getX();
                        y = p.getY();
                        int r = SI.getNUsersLoc(x,y);
                        connection.sendNUsersLoc(r);
                        break;

                    case 4:
                        ResponsePair p4 = (ResponsePair) request;
                        x = p4.getX();
                        y = p4.getY();
                        new Thread( () -> {
                            SI.waitForLocation(x,y);
                            connection.locationAvailable(x,y);
                        }).start();
                        break;

                    case 5:
                        SI.isInfected(this.name);
                        SI.logout(this.name);
                        SI.stopWait();
                        connection.warningIsInfected("STOP");
                        break;

                    case 6:
                        Map.Entry<Boolean,int[][][]> resMat = SI.showMap(this.name);
                        connection.showMap(resMat.getValue(),resMat.getKey());
                        break;

                    case 7:
                        SI.changeVip(this.name);
                        break;

                    case 8:
                        new Thread(() -> {
                            System.out.println("Entra no thread");
                            boolean b2=true;
                            while(b2) {
                                b2=SI.waitInfected(this.name);
                                if (b2) connection.warningIsInfected("Esteve com alguém infetado!!");
                            }
                            System.out.println("Sai do thread");
                        }).start();
                        break;

                    case 9:
                        SI.logout(this.name);
                        connection.warningIsInfected("STOP");
                        break;

                    default:
                        break;
                }
                request=connection.receive();
                tag = request.getTag();
            }
        } catch (Exception ignored) { }
    }
}
