package Client;

import java.util.Scanner;

public class ClientController {
    private final int nrTiposPedidos = 5;
    private boolean[] availableRequests = new boolean[nrTiposPedidos];

    private Scanner in = new Scanner(System.in);
    private ClientView view;
    private ClientModel model;

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
                    this.registerCliente();
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

        this.view.loginCliente();
        username = in.nextLine();
        password = in.nextLine();

        ResponseLogin r = model.login(username, password);
        if (r.getBool()) {
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
                case 1:
                    //this.loginCliente();
                    break;

                case 2: // número de pessoas numa localização
                    this.view.numeroPessoasLocalizacao();
                    int x = this.in.nextInt();
                    int y = this.in.nextInt();

                    new Thread(() -> {
                        try  {
                            // send request
                            ResponsePos r = model.login(username, password);
                        }
                        catch (Exception ignored)
                        {}
                    }).start();
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
