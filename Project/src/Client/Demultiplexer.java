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
    private ReentrantLock lock = new ReentrantLock(); // Porque temos que ter controlo de concorrência no acesso ao map
    private Map<Integer, Entry> entradas = new HashMap<>();

    public class Entry {
        Queue<Response> queue;
        Condition c;

        Entry() {
            this.queue = new ArrayDeque<>();
            this.c = lock.newCondition();
        }
    }

    public Demultiplexer() throws Exception {
        this.connection = new TaggedConnection();
    }


    public void start() {
        new Thread(() -> {
            TaggedConnection.Frame f;
            int tag;
            Response data;
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


    public Response receive(int tag) throws IOException, InterruptedException {
        Response res;

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

    public void close() throws IOException {
        connection.close();
    }

}
