import Data.SystemInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static private ServerSocket SS;
    static private SystemInfo SI;
    static private int NWorkers;

    public static void main(String[] args) throws IOException {
        SS = new ServerSocket(12345);
        SI = new SystemInfo();
        NWorkers = 3;

        while (true){
            Socket s = SS.accept();
            //CRIAR TAGGED CONNECTION
            ServerSession SS = new ServerSession(s,SI);//FALTA PASSAR A TAGGED CONNECTION COMO PARAMETRO
            for (int i = 0; i<NWorkers; i++)
                new Thread(SS).start();
        }
    }
}
