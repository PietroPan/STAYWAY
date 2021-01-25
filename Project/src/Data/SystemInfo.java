package Data;

import Data.Location;
import Data.LocationsMap;
import Data.User;
import Data.Users;

import java.util.AbstractMap;
import java.util.Map;

public class SystemInfo {
    private Users users = new Users();
    private LocationsMap locationsMap=new LocationsMap(); //Mapa

    public SystemInfo(){
        this.locationsMap= new LocationsMap();
        this.users= new Users();
    }

    public int login(String name,String pass){
        User u=users.get(name);
        int r;
        if (u==null||!u.getPassword().equals(pass)) r=0;
        else if (u.isInfected()) r=1;
        else r=2;
        return r;
    }

    public boolean register(String name,String pass,int x,int y){
        if (users.containsKey(name)) return false;
        else {
            Location loc = new Location(x,y);//Default
            User u = new User(name,pass,loc);
            users.add(name,u);
            users.updateUsers(name,loc);
            locationsMap.updateLocationsR(loc);
            return true;
        }
    }

    public void changeLocation(String name,int x,int y){
        User u = users.get(name);
        Location l = new Location(x,y);
        Location oldL = u.getCurrentLocation();
        if (!l.equals(oldL)){
            u.setCurrentLocation(l);
            u.addLocation(l);
            users.updateUsers(u.getName(),l);//Update de listas de potenciais infetados
            locationsMap.updateLocations(oldL,l);//Update da localizacao antiga e nova
        }
    }

    public int getNUsersLoc(int x,int y){
        return locationsMap.getNUsers(new Location(x,y));
    }

    public boolean waitForLocation(int x, int y){
        Location l=new Location(x,y);
        locationsMap.waitForLocation(l);//Fica a espera que localizacao fique vazia NEEDS IO
        return true;
    }

    public void isInfected(String name){
        User u = users.get(name);
        u.setInfected(true);
        locationsMap.updateInfected(u.getLocations());
        this.users.wakeInfected(u.getContacts());
    }

    public Map.Entry<Boolean,int[][][]> showMap(String name){
        User u = users.get(name);
        Boolean res = u.isVip();
        if (res) return new AbstractMap.SimpleEntry<>(res, locationsMap.readMap());
        return new AbstractMap.SimpleEntry<>(res,null);
    }

    public void changeVip(String name){
        users.get(name).changeVip();
    }

    public void waitInfected(String name){
        this.users.waitInfected(name);
    }
}
