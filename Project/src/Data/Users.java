package Data;

import Data.Location;
import Data.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class Users {
    Map<String, User> users;//Map de utilizadores do sistema
    ReentrantReadWriteLock lock;

    public Users(){
        this.users=new HashMap<>();
        lock=new ReentrantReadWriteLock();
    }


    public User get(String readUTF) {
        try {
            lock.readLock().lock();
            return users.get(readUTF);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(String name) {
        try {
            lock.readLock().lock();
            return users.containsKey(name);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void add(String name, User u) {
        lock.writeLock().lock();
        users.put(name,u);
        lock.writeLock().unlock();
    }

    public void updateUsers(String name, Location loc){//Update das lista de potenciais infetados qnd alguem muda de lugar
        lock.writeLock().lock();
        Set<String> r = this.users.entrySet().stream().filter(i->i.getValue().getCurrentLocation().equals(loc)&& !i.getValue().getName().equals(name)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).keySet();
        users.get(name).addContacts(r);//Adiciona pessoas na nova area aos potenciais infetados
        for (String i : r){//Adiciona-se a si próprio á lista de potenciais infetados de pessoas na área
            users.get(i).addContact(name);
        }
        lock.writeLock().unlock();
    }
}
