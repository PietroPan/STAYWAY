import Model.Location;
import Model.LocationsMap;
import Model.User;
import Model.Users;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12345);
        Users users = new Users();
        LocationsMap map=new LocationsMap();
        ReentrantLock lock= new ReentrantLock();

        while (true){
            Socket s = ss.accept();
            DataInputStream in=new DataInputStream(new BufferedInputStream(s.getInputStream()));
            DataOutputStream out=new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));

            Thread t = new Thread (() -> {
                try {
                    User u;
                    Location l;
                    Location oldL;
                    while (true) {
                        switch (in.readUTF()) {
                            case "login":
                                u = users.get(in.readUTF());
                                if (u != null && u.getPassword().equals(in.readUTF())) {
                                    lock.lock();
                                        out.writeInt(0);
                                        out.writeBoolean(true);
                                        out.writeUTF(u.getName());
                                        out.flush();
                                    lock.unlock();
                                    map.waitInfected(u.getName(),out);
                                } else {
                                    lock.lock();
                                        out.writeInt(0);
                                        out.writeBoolean(false);
                                        out.flush();
                                    lock.unlock();
                                }
                                break;
                            case "register":
                                l = new Location(0,0);
                                u = new User(in.readUTF(), in.readUTF(),l);
                                if (users.containsKey(u.getName())) {
                                    lock.lock();
                                        out.writeInt(1);
                                        out.writeBoolean(false);
                                        out.flush();
                                    lock.unlock();
                                }
                                else {
                                    lock.lock();
                                        out.writeInt(1);
                                        out.writeBoolean(true);
                                        out.writeUTF(u.getName());
                                        out.flush();
                                    lock.unlock();
                                    users.add(u.getName(), u);
                                    u.addLocation(l);
                                    users.updateUsers(u.getName(),l);
                                    map.updateLocationsR(l);
                                    map.waitInfected(u.getName(),out);
                                }
                                break;
                            case "setLocation":
                                u = users.get(in.readUTF());
                                l = new Location(in.readInt(), in.readInt());
                                oldL = u.getCurrentLocation();
                                if (!l.equals(oldL)){
                                    u.setCurrentLocation(l);
                                    u.addLocation(l);
                                    users.updateUsers(u.getName(),l);
                                    map.updateLocations(oldL,l);
                                }
                                break;
                            case "getNUsersLoc":
                                l=new Location(in.readInt(),in.readInt());
                                lock.lock();
                                    out.writeInt(2);
                                    out.writeInt(map.getNUsers(l));
                                    out.flush();
                                lock.unlock();
                                break;
                            case "waitLocation":
                                l=new Location(in.readInt(),in.readInt());
                                if (map.isAlreadyZero(l)) {
                                    lock.lock();
                                        out.writeInt(3);
                                        out.writeInt(l.getX());
                                        out.writeInt(l.getY());
                                        out.flush();
                                    lock.unlock();
                                }
                                map.waitForLocation(l,out);
                                break;
                            case "isInfected":
                                u=users.get(in.readUTF());
                                u.setInfected(true);
                                map.updateInfected(u.getLocations());
                                map.wakeInfected(u.getContacts());
                                break;
                            case "showMap": ;
                                u=users.get(in.readUTF());
                                if (u.isVip()){
                                    lock.lock();
                                        out.writeInt(5);
                                        out.writeBoolean(true);
                                        out.flush();
                                    lock.unlock();
                                    map.readMap(out);
                                } else {
                                    lock.lock();
                                        out.writeInt(5);
                                        out.writeBoolean(false);
                                        out.flush();
                                    lock.unlock();
                                }
                                break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Utilizador Desconectado");
                }
            });
            t.start();
        }
    }
}
