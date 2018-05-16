package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import API.MetroInterface;
import API.Route;
import API.ServiceFactory;
import Persistance.Data_Arret_Route_Direction;
import Persistance.StorageImpl;
import Persistance.StorageService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andréas on 20/04/2018.
 */

public class Activity_Shared_Pref_Selection extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ligne_selection);

        TextView state_txt = (TextView)findViewById(R.id.txt_traitement_ligne);
        state_txt.setVisibility(View.INVISIBLE);

        // le ServiceFactory apporte les méthodes de traitement des données reçus par l'API


        final StorageService storageService = new StorageImpl();
        final List<Data_Arret_Route_Direction> dataARD = storageService.restore(getApplicationContext());

        if(dataARD == null || dataARD.size() == 0){
            //Toast.makeText(getApplicationContext(), "Aucun arret sauvegardé !", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        dataARD.add(Data_Arret_Route_Direction.ARDEffacer());


        ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_ligne);
        //ArrayAdapter aa = new ArrayAdapter(getBaseContext(), R.layout.listview_selection_lignes, getCodeArret(dataARD));
        DataARDAdapter aa = new DataARDAdapter(Activity_Shared_Pref_Selection.this, dataARD);

        ligne_liste.setAdapter(aa);

        ligne_liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){

                if (position == dataARD.size()-1){
                    storageService.clear(getApplicationContext());
                    finish();
                }else {
                    Intent i = new Intent(getBaseContext(), Activity_Show_Stoptimes.class);
                    Bundle b = new Bundle();

                    b.putSerializable("arret", dataARD.get(position).getArret());
                    b.putSerializable("route", dataARD.get(position).getRoute());
                    b.putInt("choiceDirection", dataARD.get(position).getDirection());
                    b.putString("nameDirection", dataARD.get(position).getNameDirection());
                    b.putBoolean("flagSaveButton", false);
                    if (dataARD.get(position).getRoute().getType().equals("TRAM")){
                        b.putBoolean("isTram", true);
                    }
                    else{
                        b.putBoolean("isTram", false);
                    }

                    i.putExtras(b);
                    startActivityForResult(i, 11);
                }

            }
        });






    }

    private List getCodeArret(List<Data_Arret_Route_Direction> dataARD) {

        List<String> myArrets = new ArrayList<>();
        for(Data_Arret_Route_Direction ard : dataARD){
            myArrets.add(ard.getArret().getName());
        }
        return myArrets;

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        /*if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Log.d("transition", "transition de activity_show vers activity_shared_pref");
                setResult(Activity.RESULT_OK);
                finish();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("Arret CANCEL", "CANCEL de activity_show vers activity_shared_pref");
            }
        }*/
        if(requestCode == 11){
            if(resultCode == Activity.RESULT_OK){
                Log.d("transition", "transition de activity_show vers activity_shared_pref");
                setResult(Activity.RESULT_OK);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED){
                Log.d("Arret CANCEL", "CANCEL de activity_show vers activity_shared_pref");
            }
        }
    }


    /**
     * getRouteFromPosition
     * retourne la Route correspondante à sa position dans le tableau ListView
     * @param position
     * @param routes
     * @return
     */
    private Route getRouteFromPosition(int position, List<Route> routes) {
        char a = (char)((position)+'A');
        for(Route r:routes){
            if(r.getShortName().equals(Character.toString(a))){
                return r;
            }
        }
        return null;
    }

}
