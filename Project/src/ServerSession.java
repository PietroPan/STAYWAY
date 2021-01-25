import Client.Response;
import Client.ResponsePair;
import Client.ResponsePairString;
import Client.ResponseIntMatrix;
import Client.ResponseString;
import Client.ResponsePair;
import Data.SystemInfo;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerSession implements Runnable{
    private final TaggedConnectionServer connection; // hmmm deixar o final ou mudar o try-with-resources
    private SystemInfo SI;
    private String name;

    public ServerSession(Socket s, SystemInfo SI) throws IOException {
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
            while (true) {
                Response request = connection.receive();
                int tag = request.getTag();

                switch(tag) {
                    case 0:
                        ResponsePairString par = (ResponsePairString) request;
                        String username = par.getFirst();
                        String password = par.getSecond();
                        this.name = username;
                        boolean b = SI.login(username, password);
                        connection.loginReply(b);
                        break;

                    case 1:
                        par = (ResponsePairString) request;
                        username = par.getFirst();
                        password = par.getSecond();
                        this.name = username;
                        b = SI.register(username, password);
                        connection.registerReply(b);
                        break;

                    case 2:
                        ResponsePair parInt = (ResponsePair) request;
                        SI.changeLocation(this.name, parInt.getX(), parInt.getY());
                        break;
                        
                    case 3:
                        ResponsePair p = (ResponsePair) request;
                        int x = p.getX();
                        int y = p.getY();
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
                        break;

                    case 6:
                        Map.Entry<Boolean,int[][][]> resMat = SI.showMap(this.name);
                        connection.showMap(resMat.getValue(),resMat.getKey());
                        break;
                    default:

                        break;





                        
                }
            }
        } catch (Exception ignored) { }
    }
}
