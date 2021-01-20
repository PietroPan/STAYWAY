package Model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocationsMap {
    private MapLocation[][] map; //Mapa 10 por 10
    private Map<Integer,Condition> conds; //Map de Condition para cada lugar no mapa (serve para avisar quando esta estiver vazia)
    private Map<String,Condition> condsInf; //Map de Condition para cada Pessoa (serve para avisar pessoas que estiveram com infetado)
    private ReentrantReadWriteLock lock;

    public LocationsMap(){
        map = new MapLocation[10][10];
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                map[i][j]=new MapLocation(i,j);
            }
        }
        conds = new HashMap<>();
        condsInf = new HashMap<>();
        lock=new ReentrantReadWriteLock();
    }

    public void updateLocations(Location oldL, Location newL){//Update localizacao de pessoa já existente
        lock.writeLock().lock();
            MapLocation newML = map[newL.getX()][newL.getY()];
            newML.addTotal();
            newML.addCurrent();
            MapLocation oldML = map[oldL.getX()][oldL.getY()];
            oldML.removeCurrent();
            if (oldML.getCurrentPeople()==0) { //se novo location tiver 0 pessoas avisa todos os threads á espera nessa location
                if (conds.containsKey((oldML.getX() * 10) + oldML.getY())) conds.get((oldML.getX() * 10) + oldML.getY()).signalAll();
            }
        lock.writeLock().unlock();
    }

    public int getNUsers(Location l){
        try {
            lock.readLock().lock();
                return map[l.getX()][l.getY()].getCurrentPeople();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void updateLocationsR(Location l){//Update Localizacao quando é feito um novo registo
        lock.writeLock().lock();
            MapLocation newML = map[l.getX()][l.getY()];
            newML.addTotal();
            newML.addCurrent();
        lock.writeLock().unlock();
    }

    public void updateInfected(Set<Location> locations){//Adicionar potenciais infetados
        lock.writeLock().lock();
            for (Location i : locations) map[i.getX()][i.getY()].addInfected();
        lock.writeLock().unlock();
    }

    public void waitForLocation(Location loc, DataOutputStream out){//Esperar que location fique vazia
        new Thread(() -> {
            try {
                this.lock.writeLock().lock();
                int key = (loc.getX()*10)+loc.getY();//chave unica para cada localizacao
                if (conds.containsKey(key)) conds.get(key).await();
                else {
                    conds.put(key, this.lock.writeLock().newCondition());
                    conds.get(key).await();//espera que fique vazia (updateLocations)
                }
                out.writeInt(3);
                out.writeInt(loc.getX());
                out.writeInt(loc.getY());
                out.flush();
                this.lock.writeLock().unlock();
            } catch (InterruptedException | IOException e) {}
        }).start();
    }

    public void waitInfected(String name, DataOutputStream out){//Espera até estar potencialmente infetado
        new Thread(()->{
            try {
                condsInf.put(name,lock.writeLock().newCondition());
                while (true) {
                    this.lock.writeLock().lock();
                        condsInf.get(name).await();
                        System.out.println("Answer");
                        out.writeInt(4);
                        out.flush();
                    this.lock.writeLock().unlock();
                }
            } catch (IOException | InterruptedException e) {}
        }).start();
    }

    public void wakeInfected(Set<String> users){//Acorda todos os potencialmente infetados
        this.lock.writeLock().lock();
            for (String i : users) condsInf.get(i).signalAll();
        this.lock.writeLock().unlock();
    }

    public boolean isAlreadyZero(Location loc){//Não fica a espera que loc fique vazia
        lock.readLock().lock();
        try {
            return (map[loc.getX()][loc.getY()].getCurrentPeople() == 0);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void readMap(DataOutputStream out) throws IOException {//Manda toda a informação do map para client
        lock.writeLock().lock();
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                //out.writeInt(map[i][j].getCurrentPeople());
                out.writeInt(map[i][j].getTotalPeople());
                out.writeInt(map[i][j].getInfectedPeople());
            }
        }
        out.flush();
        lock.writeLock().unlock();
    }
}
