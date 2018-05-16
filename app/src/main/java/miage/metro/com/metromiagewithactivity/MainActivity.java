package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import Persistance.Data_Arret_Route_Direction;
import Persistance.StorageImpl;
import Persistance.StorageService;


/***
 * MainActivity
 * Activité de lancement
 * Choix de sélection : Tram, Bus, Arrets sauvegardés ou Arrets à proximité (maps)
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Création de l'activité suivante
        final Intent i = new Intent(this, activity_ligne_selection.class);

        // Au click sur le bouton tram, lancement de l'activité activity_ligne_selection
        CardView but = (CardView) findViewById(R.id.button_tram);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putBoolean("isTram", true); // isTram servira à l'affichage, certaines choses diffèrent si on a choisi tram ou bus
                i.putExtras(b);
                startActivityForResult(i, 0);
            }
        });


        // Au click sur le bouton tram, lancement de l'activité activity_ligne_selection avec isTram à faux
        CardView but_bus = (CardView) findViewById(R.id.button_bus);
        but_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putBoolean("isTram",false);
                i.putExtras(b);
                startActivityForResult(i, 1);
            }
        });

        // Lancement de l'activity Activity_Shared_Pref, listant les activités sauvegardés
        CardView but_saved = (CardView) findViewById(R.id.button_access_save_stops);
        but_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageService storageService = new StorageImpl();
                List<Data_Arret_Route_Direction> dataARD = storageService.restore(getApplicationContext());

                // Si on a pas d'arrets enregistrés
                if (dataARD == null || dataARD.size() == 0){
                    Toast.makeText(MainActivity.this, "Aucun arrêt enregistré !", Toast.LENGTH_SHORT).show();

                }else {
                    Intent intent_shared_pref = new Intent(getApplicationContext(), Activity_Shared_Pref_Selection.class);
                    startActivityForResult(intent_shared_pref, 10);
                }
            }
        });

        // Lancement de l'activity activity_map, qui permettra de visualiser les arrêts autour de soi
        CardView but_map = (CardView) findViewById(R.id.button_map);
        but_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "TO DO", Toast.LENGTH_SHORT).show();
                Intent intent_map = new Intent(getApplicationContext(), activity_map.class);
                startActivityForResult(intent_map, 100);

            }
        });
    }

    // Quand on veut qu'une activité termine, on lui indique un code qu'elle doit renvoyer à l'activité mère.
    // Dans l'activité mère, ce code est récupéré dans cette méthode en dessous, méthode qui est déclenchée à chaque fois que l'activité mère
    // reçoit une activité se terminant.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        // activity_lignes_selection (tram)
        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                Log.i("Main OK", "Activity result OK: "+resultCode);
                Log.d("Main OK", "Transistion de activity_ligne avec tram vers mainActivity");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Main CANCEL", "Activity result CANCEL: "+resultCode);
                Log.d("Main CANCEL", "Cancel de activity_ligne avec tram vers mainActivity");
            }
        }
        // activity_lignes_selection (bus)
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Log.i("Main OK", "Activity result OK: "+resultCode);
                Log.d("Main OK", "Transistion de activity_ligne avec bus vers mainActivity");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Main CANCEL", "Activity result CANCEL: "+resultCode);
                Log.d("Main CANCEL", "Cancel de activity_ligne avec bus vers mainActivity");
            }
        }


        // activity_shared_pref
        if (requestCode == 10){
            if(resultCode == Activity.RESULT_OK){
                Log.i("Main OK", "Activity result OK: "+resultCode);
                Log.d("Main OK", "Transistion de activity_Shared_Pref vers mainActivity");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Main CANCEL", "Activity result CANCEL: "+resultCode);
                Log.d("Main CANCEL", "Cancel de activity_Shared_Pref vers mainActivity");
            }
        }

        // activity_map
        if (requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                Log.i("Main OK", "Activity result OK: "+resultCode);
                Log.d("Main OK", "Transistion de activity_map vers mainActivity");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Main CANCEL", "Activity result CANCEL: "+resultCode);
                Log.d("Main CANCEL", "Cancel de activity_map vers mainActivity");
            }
        }
    }
}
