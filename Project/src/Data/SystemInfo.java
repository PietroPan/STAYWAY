package Data;

import Data.Location;
import Data.LocationsMap;
import Data.User;
import Data.Users;

public class SystemInfo {
    private Users users = new Users();
    private LocationsMap locationsMap=new LocationsMap(); //Mapa

    public SystemInfo(){
        this.locationsMap= new LocationsMap();
        this.users= new Users();
    }

    public boolean login(String name,String pass){
        User u=users.get(name);
        if (u!=null&&u.getPassword().equals(pass)){
            //LocationsMap.waitInfected(name,...); NEEDS IO
            return true;
        } else return false;
    }

    public boolean register(String name,String pass){
        if (users.containsKey(name)) return false;
        else {
            Location loc = new Location(0,0);//Default
            User u = new User(name,pass,loc);
            users.add(name,u);
            users.updateUsers(name,loc);
            locationsMap.updateLocationsR(loc);
            //LocationsMap.waitInfected(name,...); NEEDS IO
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
        if (locationsMap.isAlreadyZero(l)) {//Se não estiver ninguem na localizacao
            return false;
        }
        //locationsMap.waitForLocation(l,...);//Fica a espera que localizacao fique vazia NEEDS IO
        return true;
    }

    public void isInfected(String name){
        User u = users.get(name);
        u.setInfected(true);
        locationsMap.updateInfected(u.getLocations());
        locationsMap.wakeInfected(u.getContacts());
    }

    public boolean showMap(String name){
        User u = users.get(name);
        if (u.isVip()){
            //locationsMap.readMap(...); NEEDS IO
            return true;
        }
        return false;
    }
}
