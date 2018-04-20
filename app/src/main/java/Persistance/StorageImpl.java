package Persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import API.Arret;
import API.Route;

/**
 * Created by Andréas on 20/04/2018.
 */

public class StorageImpl implements StorageService {

    private final String sharedPrefName = "metroPref";
    private final String sharedPrefStringName = "Data_ARD";

    @Override
    public boolean store(Context context, List<Data_Arret_Route_Direction> ard) {

        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(ard);
        editor.putString(sharedPrefStringName, json);
        editor.commit();
        return true;

    }

    @Override
    public List<Data_Arret_Route_Direction> restore(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString(sharedPrefStringName, "");

        Type listType = new TypeToken<ArrayList<Data_Arret_Route_Direction>>(){}.getType();
        List<Data_Arret_Route_Direction> dataARD = gson.fromJson(json, listType);

        return dataARD;
    }

    @Override
    public List<Data_Arret_Route_Direction> clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = preferences.getString(sharedPrefStringName, "");

        Type listType = new TypeToken<ArrayList<Data_Arret_Route_Direction>>(){}.getType();
        List<Data_Arret_Route_Direction> dataARD = gson.fromJson(json, listType);

        editor.clear();
        editor.commit();

        return dataARD;
    }

    @Override
    public void add(Context context, Data_Arret_Route_Direction ard) {
        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = preferences.getString(sharedPrefStringName, "");

        Type listType = new TypeToken<ArrayList<Data_Arret_Route_Direction>>(){}.getType();
        List<Data_Arret_Route_Direction> dataARD = gson.fromJson(json, listType);

        if(dataARD == null){
            dataARD = new ArrayList<>();
        }

        dataARD.add(ard);
        store(context, dataARD);

    }
}
