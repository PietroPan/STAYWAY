package Client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class SACovidStub {
    private ReentrantLock lock;
    private Socket sock;
    private DataOutputStream out;
    private DataInputStream in;
    private String name;

    public SACovidStub(Socket socket) throws IOException {
        this.sock=socket;
        this.lock=new ReentrantLock();
        this.in=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out=new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void login(String name, String pass) throws IOException, InterruptedException {
            try {
                lock.lock();
                out.writeUTF("login");
                out.writeUTF(name);
                out.writeUTF(pass);
                out.flush();
                lock.unlock();
            } catch (IOException e) {}
    }

    public void register(String name,String pass) throws IOException, InterruptedException {
            try {
                lock.lock();
                out.writeUTF("register");
                out.writeUTF(name);
                out.writeUTF(pass);
                out.flush();
                lock.unlock();
            } catch (IOException e) {}
    }

    public void setLocation(int x,int y) throws IOException, InterruptedException {
            try {
                lock.lock();
                out.writeUTF("setLocation");
                out.writeUTF(this.name);
                out.writeInt(x);
                out.writeInt(y);
                out.flush();
                lock.unlock();
            } catch (IOException e) {}
    }

    public void getNUsersLoc(int x,int y) throws IOException, InterruptedException {
            try {
                lock.lock();
                out.writeUTF("getNUsersLoc");
                out.writeInt(x);
                out.writeInt(y);
                out.flush();
                lock.unlock();
            } catch (IOException e) {}
    }

    public void waitLocation(int x,int y){
            try {
                lock.lock();
                out.writeUTF("waitLocation");
                out.writeInt(x);
                out.writeInt(y);
                out.flush();
                lock.unlock();
            } catch (IOException e) {}
    }

    public void isInfected(){
            try {
                lock.lock();
                out.writeUTF("isInfected");
                out.writeUTF(this.name);
                out.flush();
                lock.unlock();
            }catch (IOException e) {}
    }

    public void showMap(){
            try {
                lock.lock();
                out.writeUTF("showMap");
                out.writeUTF(this.name);
                out.flush();
                lock.unlock();
            }catch (IOException e) {}
    }

    public void readReplies(){
        new Thread(() -> {
            try {
                while (true) {
                    switch (in.readInt()) {
                        case 0:
                            if (in.readBoolean()) {
                                this.name = (in.readUTF());
                                System.out.println("Welcome " + this.name);
                            } else System.out.println("Name or Password are incorrect");
                            break;
                        case 1:
                            if (in.readBoolean()) {
                                this.name = (in.readUTF());
                                System.out.println("Welcome " + this.name);
                            } else System.out.println("Name is already in use");
                            break;
                        case 2:
                            System.out.println("Há " + in.readInt() + " pessoas na localização indicada");
                            break;
                        case 3:
                            System.out.println("Location ("+in.readInt()+","+in.readInt()+") is available!!");
                            break;
                        case 4:
                            System.out.println("Someone you've been with has been infected!!!");
                            break;
                        case 5:
                            if (!in.readBoolean()) System.out.println("You don't have permission");
                            else {
                                for (int i=0;i<10;i++){
                                    for (int j=0;j<10;j++){
                                        //System.out.println("("+i+","+j+") Current People: "+in.readInt());
                                        System.out.println("("+i+","+j+") Total People: "+in.readInt());
                                        System.out.println("("+i+","+j+") Infected People: "+in.readInt());
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (IOException e){}
        }).start();
    }
}
