package API;

import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
}
