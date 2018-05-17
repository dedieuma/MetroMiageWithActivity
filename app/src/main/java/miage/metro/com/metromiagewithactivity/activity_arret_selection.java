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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import API.Arret;
import API.MetroInterface;
import API.Route;
import API.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * activity_arret_selection
 * Choix de la ligne
 */
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

        // Texte Pop-up
        final TextView treatment_message = (TextView) findViewById(R.id.txt_traitement_arret);
        treatment_message.setVisibility(View.VISIBLE);
        treatment_message.setText("En cours...");


        // demande de récupération des arrêts de la route choisie
            //exemple pour le tram B : https://data.metromobilite.fr/api/routers/default/index/routes/SEM:B/clusters
            MetroInterface service = ServiceFactory.getInstance();
            service.getArrets(currentRoute.getId()).enqueue(new Callback<List<Arret>>() {
                @Override
                public void onResponse(Call<List<Arret>> call, Response<List<Arret>> response) {
                    if(response.isSuccessful()){
                        // Liste des Arrets
                        final List<Arret> arretsTram = response.body();
                        // Liste des String, le nom des arrets
                        final List<String> arretsString = treatment.getArretsLignesToString(arretsTram);

                        treatment_message.setVisibility(View.INVISIBLE);

                        ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_arret);
                        SelectionArretAdapter aa = new SelectionArretAdapter(getApplicationContext(), arretsString);
                        //ArrayAdapter aa = new ArrayAdapter(getBaseContext(), R.layout.listview_selection_lignes, arretsString);
                        ligne_liste.setAdapter(aa);

                        ligne_liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){

                                // Récupération des noms des terminus et de l'arret sélectionné
                                String terminus1 = arretsString.get(0);
                                String terminus2 = arretsString.get(arretsString.size()-1);
                                Arret arretSelected = arretsTram.get(position);

                                Bundle b = new Bundle();
                                b.putSerializable("route", currentRoute);

                                // Si on a choisi un terminus, il ne faut pas demander à l'utilisateur
                                // quelle direction prendre
                                // Si c'est le cas, on lance directement activity_show_stoptimes
                                // Terminus 1
                                if(terminus1.equals(arretSelected.getCity()+" - "+arretSelected.getName())){
                                    Intent i = new Intent(getBaseContext(), Activity_Show_Stoptimes.class);

                                    b.putSerializable("arret", arretSelected);
                                    // choiceDirection : numéro de la direction à prendre
                                    b.putInt("choiceDirection", 1);
                                    b.putString("nameDirection", terminus2);
                                    // flagSaveButton : booléen indiquant à activity_show_stoptimes si il faut afficher le bouton de sauvegarde de l'arret
                                    b.putBoolean("flagSaveButton", true);
                                    b.putBoolean("isTram", isTram);
                                    i.putExtras(b);
                                    startActivityForResult(i, 4);


                                }else // Terminus 2
                                    if (terminus2.equals(arretSelected.getCity()+" - "+arretSelected.getName())) {
                                    Intent i = new Intent(getBaseContext(), Activity_Show_Stoptimes.class);

                                    b.putSerializable("arret", arretSelected);
                                    b.putInt("choiceDirection", 2);
                                    b.putString("nameDirection", terminus1);
                                    b.putBoolean("flagSaveButton", true);
                                    b.putBoolean("isTram", isTram);
                                    i.putExtras(b);
                                    startActivityForResult(i, 4);


                                }else // C'est pas un terminus ! On lance Activity_Direction_Selection
                                    {
                                    // Création de l'activité suivante
                                    Intent i = new Intent(getBaseContext(), Activity_Direction_Selection.class);


                                    b.putString("terminus1", arretsString.get(0));
                                    b.putString("terminus2", arretsString.get(arretsString.size()-1));

                                    b.putSerializable("arret", arretsTram.get(position));
                                    b.putBoolean("isTram", isTram);
                                    i.putExtras(b);
                                    startActivityForResult(i, 3);
                                }



                            }
                        });


                    }else{
                        Log.e("arret_sel_not_succesful", response.code()+" "+response.raw().message());
                        treatment_message.setVisibility(View.VISIBLE);
                        treatment_message.setText(response.code()+ " "+response.raw().message());
                    }
                }

                @Override
                public void onFailure(Call<List<Arret>> call, Throwable t) {
                    Log.e("arret_sel fail", t.getMessage());
                    Toast.makeText(activity_arret_selection.this, "Une erreur s'est produite lors de l'appel à l'API. Veuillez réessayer", Toast.LENGTH_LONG);
                    treatment_message.setVisibility(View.VISIBLE);
                    treatment_message.setText("Une erreur s'est produite lors de l'appel à l'API\n"+t.getMessage());
                }
            });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                Log.d("Arret OK", "transition de activity_direction vers activity_arret");
                setResult(Activity.RESULT_OK);
                finish();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("Arret CANCEL", "CANCEL de activity_direction vers activity_arret");
            }
        }
        if (requestCode == 4){
            if(resultCode == Activity.RESULT_OK){
                Log.d("Arret OK", "transition de activity_show_stoptimes vers activity_arret");
                setResult(Activity.RESULT_OK);
                finish();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("Arret CANCEL", "CANCEL de activity_stoptimes vers activity_arret");
            }
        }
    }


}
