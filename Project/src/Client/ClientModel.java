package Client;

import java.net.Socket;
import java.util.Arrays;


public class ClientModel {
    Demultiplexer dm;

    public ClientModel() throws Exception {
        dm = new Demultiplexer();
        dm.start();
    }

    //////////////////////////// Pedidos Cliente //////////////////////////////

    public boolean login(String username, String password) throws Exception { // ver onde se lida com estas exceptions
        boolean success = false;
        dm.login(username, password);
        ResponseBool data = (ResponseBool) dm.receive(0);

        success = data.getBool();
        return success;
    }

    public void numeroPessoasLocalizacao(int x, int y) {
        new Thread(() -> {
            try  {
                dm.nrPessoasLocalizacao(x, y);
                Thread.sleep(100);
                ResponseInt data = (ResponseInt) dm.receive(4);
                System.out.println("Número de pessoas em (" + x + "," + y + "): " + data.getInt());
            }
            catch (Exception ignored)
            {}
        }).start();
    }

}
