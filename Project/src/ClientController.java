import Client.ClientView;

import java.io.IOException;
import java.util.Scanner;

public class ClientController {
    private ClientView view;
    private ClientModel model;
    private Scanner in = new Scanner(System.in);

    public ClientController() throws Exception {
        this.view = new ClientView();
        this.model = new ClientModel();
    }


    public void menuInicial() {
        String opcao = "";

        while (!opcao.equals("0")) {
            this.view.inicioCliente();
            opcao = in.nextLine();

            switch (opcao) {
                case "1":
                    this.loginCliente();
                    break;

                case "2":
                    this.registoCliente();
                    break;

                case "0":
                    this.model.quit();
                    break;

                default:
                    this.view.opcaoInvalida();
                    break;

            }

        }
    }

    public void loginCliente() {
        String username, password;

        this.view.loginCliente();
        username = in.nextLine();
        password = in.nextLine();

        try {
            model.login(username, password);
            menuPrincipal();

        } catch (Exception e) {
            this.view.printException(e);

        }

    }

    public void registoCliente() {
        String username, password;

        this.view.registoCliente();
        username = in.nextLine();
        password = in.nextLine();
        int x = Integer.parseInt(in.nextLine());
        int y = Integer.parseInt(in.nextLine());

        try {
            model.register(username, password, x, y);
            menuPrincipal();

        } catch (Exception e) {
            this.view.printException(e);
        }

    }


    public void menuPrincipal() {
        this.model.someoneWasInfected();
        //this.model.waitInfected();
        String opcao = "";
        int x, y;

        while (!opcao.equals("0")&&!opcao.equals("4")) {
            this.view.menuCliente();
            opcao = in.nextLine();

            switch (opcao) {
                case "1":
                    this.view.indicarLocalizacao();
                    x = Integer.parseInt(in.nextLine());
                    y = Integer.parseInt(in.nextLine());
                    this.model.setLocalizacao(x, y);
                    break;

                case "2": // número de pessoas numa localização
                    this.view.indicarLocalizacao();
                    x = Integer.parseInt(in.nextLine());
                    y = Integer.parseInt(in.nextLine()); // CONTROLAR CASOS EM QUE NÃO É INTRODUZIDO UM NR
                    this.model.numeroPessoasLocalizacao(x, y);
                    break;

                case "3":
                    this.view.indicarLocalizacao();
                    x = Integer.parseInt(in.nextLine());
                    y = Integer.parseInt(in.nextLine());
                    this.model.waitLocation(x,y);
                    break;

                case "4":
                    this.view.printMessage("O sitema foi informado.");
                    this.model.isInfected();
                    break;

                case "5":
                    this.model.showMap();
                    break;

                case "6":
                    this.model.changeVip();
                    break;

                case "0":
                    this.model.logout();

                    break;

                default:
                    this.view.opcaoInvalida();
                    break;

            }

        }

    }

}
