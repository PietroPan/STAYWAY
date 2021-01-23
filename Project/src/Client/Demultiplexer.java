package Client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Demultiplexer implements AutoCloseable {
    private TaggedConnection connection;
    private ReentrantLock lock; // Porque temos que ter controlo de concorrência no acesso ao map
    private Map<Integer, Entry> entradas;

    public class Entry {
        Queue<byte[]> queue;
        Condition c;

        Entry() {
            this.queue = new ArrayDeque<>();
            this.c = lock.newCondition();
        }
    }

    public Demultiplexer() throws Exception {
        this.connection = new TaggedConnection();
        this.lock = new ReentrantLock();
        entradas = new HashMap<>();
    }


    public void start() {
        new Thread(() -> {
            TaggedConnection.Frame f;
            int tag;
            byte[] data;
            boolean b = true;
            while(b) {
                try {
                    f = connection.receive();
                    tag = f.tag;
                    data = f.data;

                    lock.lock();
                    try {
                        if (!entradas.containsKey(tag)) {
                            entradas.put(tag, new Entry());
                        }
                        entradas.get(tag).queue.add(data);
                        entradas.get(tag).c.signal();
                    }

                    finally {
                        lock.unlock();
                    }

                }
                catch (IOException e) {
                    b = false;
                }
            }
        }).start();


    }


    public byte[] receive(int tag) throws IOException, InterruptedException {
        byte[] res;

        lock.lock();
        try {
            if (!entradas.containsKey(tag)) {
                entradas.put(tag, new Entry());
            }
            while (entradas.get(tag).queue.isEmpty()) {
                entradas.get(tag).c.await();
            }

            res = entradas.get(tag).queue.remove();

        }
        finally {
            lock.unlock();
        }

        return res;
    }

    public void close() throws Exception {
        connection.close();
    }

    ///////////////////////////////////////////// Envio de Requests ////////////////////////////////////////////////////

    public void login(String username, String password) throws Exception {
        connection.login(username, password);
    }

    public void nrPessoasLocalizacao(int x, int y) throws Exception { // ver quando é a melhor altura para tratar estas exceções
        connection.getNUsersLoc(x, y);
    }

}
