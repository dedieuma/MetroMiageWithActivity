package API;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import API.StopTime.StopTime;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andréas on 05/04/2018.
 */

public class ServiceFactory {

    static MetroInterface service;

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

    public List<Route> getTramLigne(List<Route> lignes){
        List<Route> trams = new ArrayList<>();
        for (Route ligne:lignes) {
            if (ligne.getMode().equals( "TRAM")){
                trams.add(ligne);
            }
        }

        return trams;
    }

    public List<String> getTramLignesToString(List<Route> trams) {

        List<String> tramsString = new ArrayList<>();
        for(Route tram:trams){
            tramsString.add(tram.getMode() +" "+tram.getShortName());
        }

        Collections.sort(tramsString);
        return tramsString;
    }

    public List<String> getArretsLignesToString(List<Arret> arretsTram) {
        List<String> arretsTextuel = new ArrayList<>();
        for(Arret arret : arretsTram){
            arretsTextuel.add(arret.getCity() +" - "+arret.getName());
        }
        return arretsTextuel;
    }

    public List<StopTime> getStopTime(List<StopTime> body, String currentRoute, int choiceDirection) {

        List<StopTime> stopTimesList = new ArrayList<>();
        for(StopTime st : body){
            if(isTheSameRoute(st.getPattern().getId(),currentRoute) && st.getPattern().getDir() == choiceDirection){
                stopTimesList.add(st);
            }
        }
        return stopTimesList;
    }

    private boolean isTheSameRoute(String id, String currentRoute) {
        String[] splitted = id.split(":");
        return splitted[0].equals("SEM") && splitted[1].equals(currentRoute);
    }

    public String getDrawingTime(Integer realtimeDeparture) {
        /*Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(realtimeDeparture);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");*/

        int currentTimeStamp = toSeconds(new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));

        String nextStop = toNextStop(realtimeDeparture, currentTimeStamp);

        return nextStop;

    }

    private String toNextStop(Integer realtimeDeparture, int currentTimeStamp) {
        int difference = realtimeDeparture-currentTimeStamp;
        /*Date date = new Date(difference);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");*/

        int minutes = difference /60;
        int secondes = difference%60;

        String res = minutes==0 ? "" : ""+minutes+" minutes";
        res+= " "+secondes+" secondes";

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
