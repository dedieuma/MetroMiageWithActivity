package miage.metro.com.metromiagewithactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import API.Arret;
import API.MetroInterface;
import API.Route;
import API.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_arret_selection extends AppCompatActivity {
    Route currentRoute;
    boolean isTram;
    ServiceFactory treatment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_arret_selection);
        treatment = new ServiceFactory();
        Bundle b = getIntent().getExtras();

        // récupération de la route, qu'on avait passé en paramètre de l'intent
        if (b != null) {
            currentRoute = (Route) b.getSerializable("route");
            isTram = b.getBoolean("isTram");
        }


            // demande de récupération des arrêts de la route choisie
            //exemple pour le tram B : https://data.metromobilite.fr/api/routers/default/index/routes/SEM:B/clusters
            MetroInterface service = ServiceFactory.getInstance();
            service.getArrets(currentRoute.getId()).enqueue(new Callback<List<Arret>>() {
                @Override
                public void onResponse(Call<List<Arret>> call, Response<List<Arret>> response) {
                    if(response.isSuccessful()){
                        final List<Arret> arretsTram = response.body();
                        final List<String> arretsString = treatment.getArretsLignesToString(arretsTram);


                        ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_arret);
                        ArrayAdapter aa = new ArrayAdapter(getBaseContext(), R.layout.listview_selection_lignes, arretsString);
                        ligne_liste.setAdapter(aa);

                        ligne_liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){

                                // Création de l'activité suivante
                                Intent i = new Intent(getBaseContext(), Activity_Direction_Selection.class);
                                Bundle b = new Bundle();

                                b.putString("terminus1", arretsString.get(0));
                                b.putString("terminus2", arretsString.get(arretsString.size()-1));
                                b.putSerializable("route", currentRoute);
                                b.putSerializable("arret", arretsTram.get(position));
                                i.putExtras(b);
                                startActivityForResult(i, 3);
                            }
                        });


                    }
                }

                @Override
                public void onFailure(Call<List<Arret>> call, Throwable t) {
                    Log.e("arret_sel fail", t.getMessage());
                }
            });




    }


}
