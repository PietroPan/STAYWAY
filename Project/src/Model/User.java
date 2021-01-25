package Model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class User {
    private String name;
    private String password;
    private boolean isInfected;
    private boolean isVip;
    private boolean warningInfected;
    private boolean logedIn;
    private Location currentLocation;
    private Set<String> contacts;//Set com pessoas potencialmente infetadas
    private Set<Location> locations;//Set com localizacoes onde esta pessoa esteve

    public User(String name, String pass, Location loc){
        this.name=name;
        this.password=pass;
        this.isInfected=false;
        this.isVip=true;
        this.warningInfected=false;
        this.logedIn=true;
        this.currentLocation=loc;
        this.contacts=new HashSet<>();
        this.locations=new HashSet<>();
        this.locations.add(loc);
    }

    public User(User c){
        this.name=c.name;
        this.password=c.password;
        this.isInfected=c.isInfected;
        this.isVip=c.isVip;
        this.warningInfected=c.warningInfected;
        this.logedIn=c.logedIn;
        this.currentLocation=c.currentLocation.clone();
        this.contacts=new HashSet<>(c.contacts);
        this.locations=c.locations.stream().map(Location::clone).collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Set<String> getContacts() {
        return contacts;
    }

    public void setContacts(Set<String> contacts) {
        this.contacts = contacts;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public User clone(){
        return new User(this);
    }

    public void addContacts(Set<String> contacts){
        this.contacts.addAll(contacts);
    }

    public void addContact(String contact){
        this.contacts.add(contact);
    }

    public void addLocation(Location l){
        this.locations.add(l);
    }

    public void changeVip(){this.isVip=!this.isVip;}

    public boolean isWarningInfected() {
        return warningInfected;
    }

    public void setWarningInfected(boolean warningInfected) {
        this.warningInfected = warningInfected;
    }

    public boolean isLogedIn() {
        return logedIn;
    }

    public void setLogedIn(boolean logedIn) {
        this.logedIn = logedIn;
    }
}
