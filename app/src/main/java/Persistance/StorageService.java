package Persistance;

import android.content.Context;

import java.util.List;

import API.Arret;
import API.Route;

/**
 * Created by Andréas on 20/04/2018.
 */

public interface StorageService {

    boolean store(Context context, List<Data_Arret_Route_Direction> ard) throws Exception;
    List<Data_Arret_Route_Direction> restore(Context context);
    List<Data_Arret_Route_Direction> clear(Context context);
    void add(Context context, Data_Arret_Route_Direction ard);
    boolean isFavori(Context context, Data_Arret_Route_Direction ard);
}
