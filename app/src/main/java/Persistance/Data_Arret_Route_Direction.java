package Persistance;

import API.Arret;
import API.Route;

/**
 * Created by Andréas on 20/04/2018.
 */

public class Data_Arret_Route_Direction {

    private Arret arret;
    private Route route;
    private int direction;
    private String nameDirection;

    public Data_Arret_Route_Direction(Arret arret, Route route, int direction, String nameDirection){
        this.setArret(arret);
        this.setRoute(route);
        this.setDirection(direction);
        this.setNameDirection(nameDirection);
    }

    public static Data_Arret_Route_Direction ARDEffacer(){
        Arret a = new Arret();
        a.setName("Effacer les données");
        return new Data_Arret_Route_Direction(a, new Route(), -1, "");
    }


    public Arret getArret() {
        return arret;
    }

    public void setArret(Arret arret) {
        this.arret = arret;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getNameDirection() {
        return nameDirection;
    }

    public void setNameDirection(String nameDirection) {
        this.nameDirection = nameDirection;
    }
}
