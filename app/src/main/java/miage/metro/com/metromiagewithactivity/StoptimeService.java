package miage.metro.com.metromiagewithactivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import API.Arret;
import API.MetroInterface;
import API.Route;
import API.ServiceFactory;
import API.StopTime.StopTime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andréas on 17/04/2018.
 */

public class StoptimeService extends IntentService{
    Arret currentArret;
    Route currentRoute;
    int choiceDirection;
    public StoptimeService(){
        super("StoptimeService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {



        Bundle b = intent.getExtras();
        if (b != null){
            currentArret = (Arret) b.getSerializable("arret");
            currentRoute = (Route)b.getSerializable("route");
            choiceDirection = b.getInt("choiceDirection");
        }

        // Appel des stoptimes
        // Exemple : https://data.metromobilite.fr/api/routers/default/index/clusters/SEM:GENALSACELO/stoptimes
        MetroInterface service = ServiceFactory.getInstance();
        service.getStopTimes(currentArret.getCode()).enqueue(new Callback<List<StopTime>>() {
            @Override
            public void onResponse(Call<List<StopTime>> call, Response<List<StopTime>> response) {
                Intent localIntent = new Intent("ACTION_DONE");
                if(response.isSuccessful()){
                    ServiceFactory treatment = new ServiceFactory();
                    List<StopTime> stopTimes = treatment.getStopTime(response.body(), currentRoute.getShortName(), choiceDirection);


                    if(stopTimes == null || stopTimes.size() == 0 || stopTimes.get(0).getTimes()==null){
                        try {
                            //localIntent.putExtra("stopTime", "");
                            localIntent.putExtra("errorMsg", "Erreur de lecture des Stoptimes");
                            throw new Exception("Erreur de parse de stopTimes");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (stopTimes.get(0).getTimes().size() == 0 || stopTimes.get(0).getTimes().get(0).getRealtimeDeparture() == null) {
                        try {
                            //localIntent.putExtra("stopTime", "");
                            ArrayList<String> vide = new ArrayList<>();
                            vide.add("Il n'y a pas de prochain départ...");
                            localIntent.putExtra("stopTime", vide);
                            localIntent.putExtra("errorMsg", "Pas de prochain départ");
                            throw new Exception("Pas de prochain départ");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        //int realTimeDeparture = stopTimes.get(0).getTimes().get(0).getRealtimeDeparture();
                        //localIntent.putExtra("stopTime", treatment.getDrawingTime(realTimeDeparture));
                        localIntent.putExtra("stopTime", treatment.getDrawingTime(stopTimes.get(0).getTimes()));
                        localIntent.putExtra("errorMsg", "");

                    }




                }else{
                    //localIntent.putExtra("stopTime", "");
                    localIntent.putExtra("errorMsg", response.code()+ " "+response.raw().message());
                    Log.e("StoptimeService", "Error Message : "+response.message());

                }

                LocalBroadcastManager.getInstance(StoptimeService.this).sendBroadcast(localIntent);
            }



            @Override
            public void onFailure(Call<List<StopTime>> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
