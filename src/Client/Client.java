package Client;

import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        Socket s = new Socket("localhost", 12345);
        SACovidStub SACS = new SACovidStub(s);//Cliente
        SACS.readReplies();

        //Menu de bosta
        String inS1;
        String inS2;
        int inOpt;
        int inOpt2;
        int inp=1;
        System.out.println("1-Login");
        System.out.println("2-Register");
        inS1=in.nextLine();
        if (inS1.equals("1")) {
            System.out.println("Insira o nome: ");
            inS1=in.nextLine();
            System.out.println("Inisira a password: ");
            inS2=in.nextLine();
            SACS.login(inS1,inS2);
        } else if (inS1.equals("2")){
            System.out.println("Insira o nome: ");
            inS1=in.nextLine();
            System.out.println("Inisira a password: ");
            inS2=in.nextLine();
            SACS.register(inS1,inS2);
        }
        System.out.println("1-Atualizar Posição");
        System.out.println("2-Número de pessoas numa localização");
        System.out.println("3-Informar sobre localização vazia");
        System.out.println("4-Informar contagio");
        System.out.println("5-Descarregar mapa");
        System.out.println("6-Lista De Comandos");
        System.out.println("0-Log Out");
        while (inp!=0) {
            switch (inp = in.nextInt()) {
                case 1:
                    System.out.println("Insira coordenada x: ");
                    inOpt = in.nextInt();
                    System.out.println("Insira coordenada y: ");
                    inOpt2 = in.nextInt();
                    SACS.setLocation(inOpt, inOpt2);
                    break;
                case 2:
                    System.out.println("Insira coordenada x: ");
                    inOpt = in.nextInt();
                    System.out.println("Insira coordenada y: ");
                    inOpt2 = in.nextInt();
                    SACS.getNUsersLoc(inOpt, inOpt2);
                    break;
                case 3:
                    System.out.println("Insira coordenada x: ");
                    inOpt = in.nextInt();
                    System.out.println("Insira coordenada y: ");
                    inOpt2 = in.nextInt();
                    SACS.waitLocation(inOpt,inOpt2);
                    break;
                case 4:
                    System.out.println("Contagio registado");
                    SACS.isInfected();
                    break;
                case 5:
                    SACS.showMap();
                    break;
                case 6:
                    System.out.println("1-Atualizar Posição");
                    System.out.println("2-Número de pessoas numa localização");
                    System.out.println("3-Informar sobre localização vazia");
                    System.out.println("4-Informar contagio");
                    System.out.println("5-Descarregar mapa");
                    System.out.println("0-Log Out");
                    break;
            }
        }
    }
}
