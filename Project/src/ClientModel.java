
package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;


public class ClientModel {
    Demultiplexer dm;

    public ClientModel() throws Exception {
        dm = new Demultiplexer();
        dm.start();
    }

    //////////////////////////// Pedidos Cliente //////////////////////////////

    public boolean login(String username, String password) throws ExceptionIncorretLogin, IOException, InterruptedException {
        boolean success = false;
        dm.login(username, password);
        ResponseBool data = (ResponseBool) dm.receive(0);

        success = data.getBool();
        if (!success) {
            throw new ExceptionIncorretLogin("Username ou Password errados.");
        }
        return success;
    }

    public boolean register(String username, String password) throws ExceptionImpossibleRegister, IOException, InterruptedException {
        boolean success = false;
        dm.register(username, password);
        ResponseBool data = (ResponseBool) dm.receive(1);

        success = data.getBool();
        if (!success) {
            throw new ExceptionImpossibleRegister("Username indisponível.");
        }
        return success;
    }

    public void setLocalizacao(int x, int y) {
        new Thread( () -> {
            dm.setLocation(x, y);
            System.out.println("O sistema foi informado da sua nova localização.");
        }).start();
    }

    public void waitLocation (int x, int y) {
        new Thread (() -> {
            try {
                dm.waitLocation(x, y);
                ResponsePair data = (ResponsePair) dm.receive(2);
                System.out.println("Localização (" + x + "," + y + ") disponível!");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public void numeroPessoasLocalizacao(int x, int y) {
        new Thread(() -> {
            try  {
                dm.nrPessoasLocalizacao(x, y);
                //Thread.sleep(100);
                ResponseInt data = (ResponseInt) dm.receive(3);
                System.out.println("Número de pessoas em (" + x + "," + y + "): " + data.getInt());
            }

            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }


    public void isInfected () {
        new Thread (() -> {
            dm.isInfected();
        }).start();
    }

    public void showMap () {
        new Thread (() -> {
            dm.showMap();
             try {
                 ResponseIntMatrix map = (ResponseIntMatrix) dm.receive(5);

                 if (map == null) {
                     System.out.println("Não te permissões para executar este comando!");
                 } else {
                    //fazer print
                 }
             } catch (Exception e) {
                 System.out.println(e.getMessage());
             }
        }).start();
    }

    public void someoneWasInfected () {
        new Thread (() -> {
            while (true) {
                try {
                    ResponseString data = (ResponseString) dm.receive(4);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }

}
