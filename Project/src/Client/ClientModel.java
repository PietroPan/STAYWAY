package Client;

import java.net.Socket;

public class ClientModel {
    PedidosCliente p;

    public ClientModel() throws Exception {
        p = new PedidosCliente();
    }

    public Response login(String username, String password) throws Exception {
        p.login(username, password);
    }

    while (opcao != 0) {
        switch ()

    }

}
