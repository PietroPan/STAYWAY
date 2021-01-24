import Client.Response;
import Client.ResponsePair;
import Client.ResponsePairString;
import Data.SystemInfo;

import java.io.*;
import java.net.Socket;

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
                        boolean b = SI.login(username, password);
                        connection.loginReply(b);
                        break;

                    case 3:
                        ResponsePair p = (ResponsePair) request;
                        int x = p.getX();
                        int y = p.getY();
                        int r = SI.getNUsersLoc(x,y);
                        connection.sendNUsersLoc(r);
                        break;

                    default:

                        break;





                        
                }
            }
        } catch (Exception ignored) { }
    }
}
