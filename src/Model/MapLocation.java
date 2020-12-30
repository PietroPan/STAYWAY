package Model;

import java.util.Objects;

public class MapLocation extends Location {
    private int currentPeople;
    private int totalPeople;
    private int infectedPeople;

    public MapLocation(int x, int y) {
        super(x, y);
        this.currentPeople=0;
        this.totalPeople=0;
        this.infectedPeople=0;
    }

    public MapLocation(Location c) {
        super(c);
        this.currentPeople=0;
        this.totalPeople=0;
        this.infectedPeople=0;
    }

    public int getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(int currentPeople) {
        this.currentPeople = currentPeople;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }

    public int getInfectedPeople() {
        return infectedPeople;
    }

    public void setInfectedPeople(int infectedPeople) {
        this.infectedPeople = infectedPeople;
    }

    public void addCurrent(){
        this.currentPeople++;
    }

    public void addInfected(){
        this.infectedPeople++;
    }

    public void addTotal(){
        this.totalPeople++;
    }

    public void removeCurrent(){
        this.currentPeople--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    public boolean equalsS(int x,int y){
        return ((this.getX()==x)&&(this.getY()==y));
    }
}
