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
        LocationsMap map=new LocationsMap(); //Mapa
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
                                        out.writeInt(0);//Resposta com tag 0
                                        out.writeBoolean(true);//Credenciais corretas
                                        out.writeUTF(u.getName());
                                        out.flush();
                                    lock.unlock();
                                    map.waitInfected(u.getName(),out);//Thread que fica á espera ate cliente potencialmente infetado
                                } else {
                                    lock.lock();
                                        out.writeInt(0);//Resposta com tag 0
                                        out.writeBoolean(false);//Credenciais erradas
                                        out.flush();
                                    lock.unlock();
                                }
                                break;
                            case "register":
                                l = new Location(0,0);
                                u = new User(in.readUTF(), in.readUTF(),l);
                                if (users.containsKey(u.getName())) {
                                    lock.lock();
                                        out.writeInt(1);//Resposta com tag 1
                                        out.writeBoolean(false);//Nome já existe
                                        out.flush();
                                    lock.unlock();
                                }
                                else {
                                    lock.lock();
                                        out.writeInt(1);//Resposta com tag 1
                                        out.writeBoolean(true);//Nome não existe
                                        out.writeUTF(u.getName());
                                        out.flush();
                                    lock.unlock();
                                    users.add(u.getName(), u);
                                    u.addLocation(l);
                                    users.updateUsers(u.getName(),l);
                                    map.updateLocationsR(l);//Adiciona loc atual ao mapa (0,0) ALGO A MUDAR!
                                    map.waitInfected(u.getName(),out);//Thread que fica á espera ate cliente potencialmente infetado
                                }
                                break;
                            case "setLocation":
                                u = users.get(in.readUTF());
                                l = new Location(in.readInt(), in.readInt());
                                oldL = u.getCurrentLocation();
                                if (!l.equals(oldL)){
                                    u.setCurrentLocation(l);
                                    u.addLocation(l);
                                    users.updateUsers(u.getName(),l);//Update de listas de potenciais infetados
                                    map.updateLocations(oldL,l);//Update da localizacao antiga e nova
                                }
                                break;
                            case "getNUsersLoc":
                                l=new Location(in.readInt(),in.readInt());
                                lock.lock();
                                    out.writeInt(2);//Resposta com tag 2
                                    out.writeInt(map.getNUsers(l));
                                    out.flush();
                                lock.unlock();
                                break;
                            case "waitLocation":
                                l=new Location(in.readInt(),in.readInt());
                                if (map.isAlreadyZero(l)) {//Se não estiver ninguem na localizacao
                                    lock.lock();
                                        out.writeInt(3);//Resposta com tag 0
                                        out.writeInt(l.getX());
                                        out.writeInt(l.getY());
                                        out.flush();
                                    lock.unlock();
                                }
                                map.waitForLocation(l,out);//Fica a espera que localizacao fique vazia
                                break;
                            case "isInfected":
                                u=users.get(in.readUTF());
                                u.setInfected(true);
                                map.updateInfected(u.getLocations());//Adiciona infetado a todas a localizacoes onde este esteve
                                map.wakeInfected(u.getContacts());//Acorda todos os potenciais infetados
                                break;
                            case "showMap": ;
                                u=users.get(in.readUTF());
                                if (u.isVip()){
                                    lock.lock();
                                        out.writeInt(5);//Resposta com tag 5
                                        out.writeBoolean(true);//Tem permissoes
                                        out.flush();
                                    lock.unlock();
                                    map.readMap(out);
                                } else {
                                    lock.lock();
                                        out.writeInt(5);//Resposta com tag 0
                                        out.writeBoolean(false);//Não tem permissoes
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
