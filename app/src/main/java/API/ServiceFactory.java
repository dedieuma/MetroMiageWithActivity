package API;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import API.StopTime.StopTime;
import API.StopTime.Time;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andréas on 05/04/2018.
 */

public class ServiceFactory {

    static MetroInterface service;

    /**
     * getInstance()
     * Singleton qui renvoie l'instance Retrofit de MetroInterface, servant à faire les appels à l'API
     * @return
     */
    public static MetroInterface getInstance(){
        if (service == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://data.metromobilite.fr/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(MetroInterface.class);
        }
        return service;

    }

    /**
     * getTramLigne(List<Route> lignes)
     * renvoie la liste des Route de tram venant de la liste de Route du paramètre "lignes"
     * En effet, le paramètre "lignes" contient toutes les lignes de Metro, y compris les bus
     * @param lignes
     * @return
     */
    public List<Route> getTramLigne(List<Route> lignes){
        List<Route> trams = new ArrayList<>();
        for (Route ligne:lignes) {
            if (ligne.getMode().equals( "TRAM")){
                trams.add(ligne);
            }
        }

        return trams;
    }

    /***
     * getBusLigne(Liste<Route> body)
     * renvoie la liste des Route de bus venant de la liste de Route du paramètre "body"
     * @param body
     * @return
     */
    public List<Route> getBusLigne(List<Route> body) {
        List<Route> bus = new ArrayList<>();
        for (Route ligne:body) {
            if (ligne.getMode().equals( "BUS")){
                bus.add(ligne);
            }
        }

        return bus;
    }

    /**
     * getTramLignesToString(List<Route> trams)
     * construit la liste de nom des trams à afficher sur le layout à partir du paramètre "trams", qui est une liste de Route
     * De plus, les classe alphabétiquement
     * @param trams
     * @return
     */
    public List<String> getTramLignesToString(List<Route> trams) {

        List<String> tramsString = new ArrayList<>();
        for(Route tram:trams){
            tramsString.add(tram.getMode() +" "+tram.getShortName());
        }

        Collections.sort(tramsString);
        return tramsString;
    }


    /***
     * getBusLigneToString(List<Route> listRoutes)
     * construit la liste de nom des bus à afficher sur le layout à partir du paramètre "listRoutes", qui est une liste de Route
     * @param listRoutes
     * @return
     */
    public List<String> getBusLigneToString(List<Route> listRoutes) {

        List<String> busString = new ArrayList<>();
        for(Route bus:listRoutes){
            busString.add(bus.getType() +" "+bus.getShortName());
        }

        Collections.sort(busString);
        return busString;
    }


    /**
     *getArretsLignesToString(List<Arret> arretsTram)
     * construit la liste de nom des arrets à afficher sur le layout à partir de la liste d'arrets "arretsTram"
     * @param arretsTram
     * @return
     */
    public List<String> getArretsLignesToString(List<Arret> arretsTram) {
        List<String> arretsTextuel = new ArrayList<>();
        for(Arret arret : arretsTram){
            arretsTextuel.add(arret.getCity() +" - "+arret.getName());
        }
        return arretsTextuel;
    }

    /**
     * getStopTime(List<StopTime> body, String currentRoute, int choiceDirection)
     * Récupère la liste des stopTime venant de "body" (la réponse de l'API) de la route "currentRoute" dans la direction "choiceDirection"
     * En effet l'API renvoie la liste des routes de l'arret considéré, on doit récupérer les stopTime de la route qu'on a précédement choisi,
     * dans la direction précédemment choisie
     * https://data.metromobilite.fr/api/routers/default/index/clusters/SEM:GENALSACELO/stoptimes
     * @param body
     * @param currentRoute
     * @param choiceDirection
     * @return
     */
    public List<StopTime> getStopTime(List<StopTime> body, String currentRoute, int choiceDirection) {

        List<StopTime> stopTimesList = new ArrayList<>();
        for(StopTime st : body){
            if(isTheSameRoute(st.getPattern().getId(),currentRoute) && st.getPattern().getDir() == choiceDirection){
                stopTimesList.add(st);
            }
        }
        return stopTimesList;
    }


    /**
     * isTheSameRoute(String id, String currentRoute)
     * vérifie que le stopTime en cours de traitement et la route choisie soient les mêmes
     * @param stopTime
     * @param currentRoute
     * @return
     */
    private boolean isTheSameRoute(String stopTime, String currentRoute) {
        String[] splitted = stopTime.split(":");
        return splitted[1].equals(currentRoute);
    }


    /***
     * getDrawingTime(List<Time> times)
     * renvoie sous forme de ArrayList<String> les différents horaires à afficher, venant de la liste de times qui vient de l'API
     * @param times
     * @return
     */
    public ArrayList<String> getDrawingTime(List<Time> times) {

        int currentTimeStamp = toSeconds(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));

        ArrayList<String> stringTimes = new ArrayList<>();
        for (Time t : times){
            stringTimes.add(toNextStop(t.getRealtimeDeparture(), currentTimeStamp));
        }
        return stringTimes;

    }
    
    /**
     * toNextStop(Integer realtimeDeparture, int currentTimeStamp)
     * calcule la différence entre l'heure actuelle et l'heure du prochain passage.
     * NB : un bug peut survenir si l'API est à la traine et ne met pas à jours ses données suffisememnt rapidement.
     * On peut alors avoir des "Heure du prochain passage : dans -15 secondes."
     * @param realtimeDeparture
     * @param currentTimeStamp
     * @return
     */
    private String toNextStop(Integer realtimeDeparture, int currentTimeStamp) {
        int difference = realtimeDeparture-currentTimeStamp;

        /*Date date = new Date(difference);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");*/

        int minutes = difference /60;
        int secondes = difference%60;

        String res = minutes==0 ? "" : ""+minutes+" minutes";
        res+= " "+secondes+" secondes";

        if (difference < 0){
            res+= "\nL'API semble retarder. Veuillez patienter...";
        }

        return res;

    }

    private int toSeconds(String ss) {
        String[] splitted = ss.split(":");
        int hours = Integer.parseInt(splitted[0])*3600;
        int minutes = Integer.parseInt(splitted[1])*60;
        int seconds = Integer.parseInt(splitted[2]);

        return hours+minutes+seconds;

    }



}
