import Client.Response;
import Data.SystemInfo;

import java.io.*;
import java.net.Socket;

public class ServerSession implements Runnable{
    private final TaggedConnection connection; // hmmm deixar o final ou mudar o try-with-resources
    private SystemInfo SI;
    private String name;

    public ServerSession(Socket s, SystemInfo SI) throws IOException {
        this.connection = new TaggedConnection(s);
        this.SI=SI;
    }

    public ServerSession(TaggedConnection tc, SystemInfo SI) throws IOException {
        this.connection = tc;
        this.SI=SI;
    }



    @Override
    public void run() {
        //Demultiplexer Parte do Servidor
        try (this.connection) {
            while (true) {
                Response res = connection.receive();
                int tag = res.getTag();

                switch(tag) {
                    case 0:
                        //...
                        
                }
            }
        } catch (Exception ignored) { }
    }
}
