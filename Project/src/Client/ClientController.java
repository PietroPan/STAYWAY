package Client;

import java.util.Scanner;

public class ClientController {
    private ClientView view;
    private ClientModel model;
    private Scanner in = new Scanner(System.in);


    public void menuInicial() {
        int opcao = 10;

        while (opcao != 0) {
            this.view.inicioCliente();
            opcao = in.nextInt();

            switch (opcao) {
                case 1:
                    this.loginCliente();
                    break;

                case 2:
                    //this.registoCliente();
                    break;

                case 0:
                    break;

                default:
                    this.view.opcaoInvalida();
                    break;

            }

        }
    }

    public void loginCliente() {
        String username, password;
        boolean success;

        this.view.loginCliente();
        username = in.nextLine();
        password = in.nextLine();

        try {
            success = model.login(username, password);
        } catch (Exception e) {
            success = false;
        }

        if (success) {
            menuPrincipal();
        } else {
            this.view.loginInvalido();
        }

    }

    public void menuPrincipal() {
        int opcao = 10;

        while (opcao != 0) {
            this.view.menuCliente();
            opcao = in.nextInt();

            switch (opcao) {

                case 2: // número de pessoas numa localização
                    this.view.numeroPessoasLocalizacao();
                    int x = this.in.nextInt();
                    int y = this.in.nextInt();
                    this.model.numeroPessoasLocalizacao(x, y);
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
