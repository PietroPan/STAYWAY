package Client;

import java.net.Socket;
import java.util.Arrays;


public class ClientModel {
    Demultiplexer dm;

    public ClientModel() throws Exception {
        dm = new Demultiplexer();
        dm.start();
    }
/*
    public Response login(String username, String password) throws Exception {
        p.login(username, password);
    }

    while (opcao != 0) {
        switch ()

    } */

    //////////////////////////// Pedidos Cliente //////////////////////////////

    public boolean login(String username, String password) throws Exception { // ver onde se lida com estas exceptions
        boolean success = false;
        dm.login(username, password);
        byte[] data = dm.receive(0);
        if ((new String(data)).equals("true")) {
            success = true;
        }

        return success;
    }

    public void numeroPessoasLocalizacao(int x, int y) {

        new Thread(() -> {
            try  {
                dm.nrPessoasLocalizacao(x, y);
                Thread.sleep(100);
                byte[] data = dm.receive(4);
                System.out.println("Número de pessoas em (" + x + "," + y + "): " + new String(data));
            }
            catch (Exception ignored)
            {}
        }).start();
    }

}
