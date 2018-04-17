package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import API.MetroInterface;
import API.Route;
import API.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * activity_ligne_selection
 * Choix de la ligne
 */
public class activity_ligne_selection extends AppCompatActivity {

    ServiceFactory treatment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ligne_selection);

        // le ServiceFactory apporte les méthodes de traitement des données reçus par l'API
        treatment = new ServiceFactory();



        // voir TP4 pour le fonctionnement des appels à une API.
        // Plus de détails dans MetroInterface et ServiceFactory
        MetroInterface service = ServiceFactory.getInstance();
        service.getRoutes().enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if(response.isSuccessful()){
                    // si l'appel de l'API pour obtenir les routes, appel fait sur l'url
                    // https://data.metromobilite.fr/api/routers/default/index/routes
                    // alors on fait ce qu'il y a en dessous

                    // récupération des lignes de tram
                    final List<Route> routesTram = treatment.getTramLigne(response.body());
                    final List<String> routes = treatment.getTramLignesToString(routesTram);


                    // transformation des lignes de tram récupérées en tableau de layout
                    ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_ligne);
                    ArrayAdapter aa = new ArrayAdapter(getBaseContext(), R.layout.listview_selection, routes);
                    ligne_liste.setAdapter(aa);

                    // au clic sur une ligne du tableau, déclenchement de l'activité suivante : activity_arret_selection
                    ligne_liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                            Intent i = new Intent(getBaseContext(), activity_arret_selection.class);
                            Bundle b = new Bundle();


                            Route routeChoisie = getRouteFromPosition(position, routesTram);



                            if (routeChoisie != null){
                                // on passe l'objet Route en paramètre de l'intent, pour qu'on puisse la récupérer dans la nouvelle activité
                                // NB : pour passer des classes en paramètres de Bundle, il faut les déclarer comme implémentant Serializable
                                // Les Models de données ont été construites grâce à http://www.jsonschema2pojo.org/
                                // On lui file un json, il construit un model de donnée java
                                b.putSerializable("route", routeChoisie);
                                i.putExtras(b);
                                startActivityForResult(i, 2);
                            }

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.e("ligne_sel fail", t.getMessage());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Log.d("transition", "transition de activity_arret vers mainActivity");


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
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
