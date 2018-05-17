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


    /***
     * store
     * enregistre la liste de DataARD passée en paramètre
     * @param context
     * @param ard
     * @return
     */
    @Override
    public boolean store(Context context, List<Data_Arret_Route_Direction> ard) throws Exception {

        if (ard == null) throw new Exception("ard can't be null");
        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(ard);
        editor.putString(sharedPrefStringName, json);
        editor.commit();
        return true;

    }

    /***
     * restore
     * retourne la liste de DataARD qui était stockée
     * @param context
     * @return
     */
    @Override
    public List<Data_Arret_Route_Direction> restore(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString(sharedPrefStringName, "");

        Type listType = new TypeToken<ArrayList<Data_Arret_Route_Direction>>(){}.getType();
        List<Data_Arret_Route_Direction> dataARD = gson.fromJson(json, listType);

        return dataARD;
    }

    /***
     * clear
     * retourne la liste de DataARD qui était stockée, puis supprime la base
     * @param context
     * @return
     */
    @Override
    public List<Data_Arret_Route_Direction> clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        List<Data_Arret_Route_Direction> dataARD = restore(context);

        editor.clear();
        editor.commit();

        return dataARD;
    }

    /***
     * add
     * ajoute à la liste stockée le nouvel ARD passé en paramètre
     * @param context
     * @param ard
     */
    @Override
    public void add(Context context, Data_Arret_Route_Direction ard) {
        if (isFavori(context, ard)){
            return;
        }
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
        try {
            store(context, dataARD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean isFavori(Context context, Data_Arret_Route_Direction ard){
        List<Data_Arret_Route_Direction> dataARD = restore(context);

        if (dataARD == null) return false;
        for (Data_Arret_Route_Direction localArd: dataARD) {
            if (localArd.getNameDirection().equals(ard.getNameDirection()) &&
                    localArd.getRoute().getShortName().equals(ard.getRoute().getShortName()) &&
                    localArd.getArret().getName().equals(ard.getArret().getName())){
                return true;
            }
        }

        return false;
    }
}
