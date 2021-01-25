package Data;

import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocationsMap {
    private LocationInfo[][] map; //Mapa 10 por 10
    private ReentrantReadWriteLock lock;
    private Condition waitLocation;

    public LocationsMap(){
        map = new LocationInfo[10][10];
        for (int i=0;i<10;i++){
            for (int j=0;j<10;j++){
                map[i][j]=new LocationInfo(i,j);
            }
        }
        lock=new ReentrantReadWriteLock();
        waitLocation = lock.writeLock().newCondition();
    }

    public void updateLocations(Location oldL, Location newL){//Update localizacao de pessoa já existente
        lock.writeLock().lock();
        LocationInfo newML = map[newL.getX()][newL.getY()];
        newML.addTotal();
        newML.addCurrent();
        LocationInfo oldML = map[oldL.getX()][oldL.getY()];
        oldML.removeCurrent();
        if (oldML.isEmpty()) { //se novo location tiver 0 pessoas avisa todos os threads á espera nessa location
            waitLocation.signalAll();
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
        LocationInfo newML = map[l.getX()][l.getY()];
        newML.addTotal();
        newML.addCurrent();
        lock.writeLock().unlock();
    }

    public void updateInfected(Set<Location> locations){//Adicionar potenciais infetados
        lock.writeLock().lock();
        for (Location i : locations) map[i.getX()][i.getY()].addInfected();
        lock.writeLock().unlock();
    }

    public void waitForLocation(Location loc){//Esperar que location fique vazia
        try {
            this.lock.writeLock().lock();
            LocationInfo locI = map[loc.getX()][loc.getY()];
            while (!locI.isEmpty()) waitLocation.await();
                this.lock.writeLock().unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }
    public boolean isAlreadyZero(Location loc){//Não fica a espera que loc fique vazia
        lock.readLock().lock();
        try {
            return (map[loc.getX()][loc.getY()].getCurrentPeople() == 0);
        } finally {
            lock.readLock().unlock();
        }
    }

    public int[][][] readMap() {//Manda toda a informação do map para client
        try {
            lock.writeLock().lock();
            int [][][] res = new int[2][10][10];
            for (int i=0;i<10;i++){
                for (int j=0;j<10;j++){
                    res[0][i][j] = map[i][j].getTotalPeople();
                    res[1][i][j] = map[i][j].getInfectedPeople();
                }
            }
            return res;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
