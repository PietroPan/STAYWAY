package Client;

public class ClientView {

    public void inicioCliente() {
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("0 - Sair do programa.");
    }

    public void loginCliente() {
        System.out.println("Insira o nome: ");
        System.out.println("Insira a password: ");
    }

    public void registoCliente() {
        loginCliente();
        indicarLocalizacao();
    }

    public void menuCliente() {
        System.out.println("1-Atualizar Posição");
        System.out.println("2-Número de pessoas numa localização");
        System.out.println("3-Informar sobre localização vazia");
        System.out.println("4-Informar contágio");
        System.out.println("5-Descarregar mapa");
        System.out.println("0-Log Out");
    }

    public void opcaoInvalida() {
        System.out.println("Opcao invalida");
    }

    public void printException(Exception e) {
        System.out.println(e.getMessage());
    }

    public void indicarLocalizacao() {
        System.out.println("Coordenada x: ");
        System.out.println("Coordenada y: ");
    }

    public void printMessage (String message) {
        System.out.println(message);
    }
}
