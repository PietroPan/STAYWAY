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

    public boolean login(String username, String password) throws ExceptionIncorretLogin, IOException, InterruptedException, ExceptionIsInfected, ExceptionIsLogedIn {
        int success = 0;
        dm.login(username, password);
        ResponseInt data = (ResponseInt) dm.receive(0);

        success = data.getInt();
        if (success==0) {
            throw new ExceptionIncorretLogin("Username ou Password errados.");
        } else if (success==1) throw new ExceptionIsInfected("Está infetado! Sistema Bloqueado");
        else if (success==2) throw new ExceptionIsLogedIn("Utilizador já tem sessão iniciada");
        else return true;
    }

    public boolean register(String username, String password, int x, int y) throws ExceptionImpossibleRegister, IOException, InterruptedException {
        boolean success = false;
        dm.register(username, password, x, y);
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
                     printMap(map.getMatrix());
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

    public void logout(){
        dm.logout();
    }

    public void quit(){
        new Thread(()->{
            dm.quit();
        }).start();
    }

    public void printMap(int[][][] m){
        for (int i=0;i<10;i++){
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("|  Pos  ("+i+",0)  |  Pos  ("+i+",1)  |  Pos  ("+i+",2)  |  Pos  ("+i+",3)  |  Pos  ("+i+",4)  |  Pos  ("+i+",5)  |  Pos  ("+i+",6)  |  Pos  ("+i+",7)  |  Pos  ("+i+",8)  |  Pos  ("+i+",9)  |");
            System.out.println("| Pessoas:   "+m[0][i][0]+" | Pessoas:   "+m[0][i][1]+" | Pessoas:   "+m[0][i][2]+" | Pessoas:   "+m[0][i][3]+" | Pessoas:   "+m[0][i][4]+" | Pessoas:   "+m[0][i][5]+" | Pessoas:   "+m[0][i][6]+" | Pessoas:   "+m[0][i][7]+" | Pessoas:   "+m[0][i][8]+" | Pessoas:   "+m[0][i][9]+" |");
            System.out.println("| Infetados: "+m[1][i][0]+" | Infetados: "+m[1][i][1]+" | Infetados: "+m[1][i][2]+" | Infetados: "+m[1][i][3]+" | Infetados: "+m[1][i][4]+" | Infetados: "+m[1][i][5]+" | Infetados: "+m[1][i][6]+" | Infetados: "+m[1][i][7]+" | Infetados: "+m[1][i][8]+" | Infetados: "+m[1][i][9]+" |");
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");
    }
}
