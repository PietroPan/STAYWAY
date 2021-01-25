import Client.*;

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

    public boolean login(String username, String password) throws ExceptionIncorretLogin, IOException, InterruptedException, ExceptionIsInfected {
        int success = 0;
        dm.login(username, password);
        ResponseInt data = (ResponseInt) dm.receive(0);

        success = data.getInt();
        if (success==0) {
            throw new ExceptionIncorretLogin("Username ou Password errados.");
        } else if (success==1) throw new ExceptionIsInfected("Está infetado! Sistema Bloqueado");
        else return true;
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
                ResponsePair data = (ResponsePair) dm.receive(4);
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
                 ResponseIntMatrix map = (ResponseIntMatrix) dm.receive(6);

                 if (map.getMatrix() == null) {
                     System.out.println("Não te permissões para executar este comando!");
                 } else {
                     System.out.println(Arrays.deepToString(map.getMatrix()));
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
                    ResponseString data = (ResponseString) dm.receive(5);
                    System.out.println(data.getStr());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }

    public void changeVip(){
        new Thread(() -> {
            dm.changeVip();
        }).start();
    }

    public void waitInfected(){
        new Thread(() ->{
            dm.waitInfected();
        }).start();
    }

}
