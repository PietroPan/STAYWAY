import Client.Response;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public abstract class TaggedConnection implements AutoCloseable {
    protected ReentrantLock lock;
    protected Socket socket;
    protected DataOutputStream out;
    protected DataInputStream in;
    protected String name;

    public TaggedConnection() throws IOException {
        this.socket = new Socket("localhost", 12345);
        this.lock = new ReentrantLock();
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public TaggedConnection(Socket s) throws IOException {
        this.socket = s;
        this.lock = new ReentrantLock();
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        socket.close();
    }

    public abstract Response receive() throws IOException;
}
