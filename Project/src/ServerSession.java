import Data.SystemInfo;

import java.io.*;
import java.net.Socket;

public class ServerSession implements Runnable{
    private Socket s;
    private SystemInfo SI;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    //FALTA CLASSE TAGGED CONNECTION

    public ServerSession(Socket s, SystemInfo SI) throws IOException {
        this.s=s;
        this.SI=SI;
        this.in=new DataInputStream(new BufferedInputStream(s.getInputStream()));
        this.out=new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
    }

    @Override
    public void run() {
        //Demultiplexer Parte do Servidor
    }
}
