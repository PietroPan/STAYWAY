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
            System.out.println("Entra no try");
            model.login(username, password);
            System.out.println("Faz login no model");
            this.model.someoneWasInfected();
            System.out.println("Começa à espera de aviso");
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

        try {
            model.register(username, password);
            this.model.someoneWasInfected();
            menuPrincipal();

        } catch (Exception e) {
            this.view.printException(e);
        }

    }


    public void menuPrincipal() {
        int opcao = 10;
        int x,y;

        while (opcao != 0 && opcao != 4) {
            this.view.menuCliente();
            opcao = in.nextInt();

            switch (opcao) {
                case 1:
                    this.view.indicarLocalizacao();
                    x = this.in.nextInt();
                    y = this.in.nextInt();
                    this.model.setLocalizacao(x, y);
                    break;

                case 2: // número de pessoas numa localização
                    this.view.indicarLocalizacao();
                    x = this.in.nextInt();
                    y = this.in.nextInt();
                    this.model.numeroPessoasLocalizacao(x, y);
                    break;

                case 3:
                    // ??
                    break;

                case 4:
                    this.view.printMessage("O sitema foi informado.");
                    this.model.isInfected();
                    break;

                case 5:
                    this.model.showMap();
                    break;

                case 0:
                    break;

                default:
                    this.view.opcaoInvalida();
                    break;

            }

        }

    }

}
