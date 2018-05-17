package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import API.Arret;
import API.Route;
import API.ServiceFactory;
import Persistance.Data_Arret_Route_Direction;
import Persistance.StorageImpl;
import Persistance.StorageService;

/**
 * Created by Andréas on 07/04/2018.
 */

/**
 * Affichage des Temps
 */
public class Activity_Show_Stoptimes extends AppCompatActivity {

    ServiceFactory treatment;
    Arret currentArret;
    Route currentRoute;
    int choiceDirection;
    String nameDirection;
    boolean flagSaveButtonVisibility, isTram;
    Handler handler;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_times);
        treatment = new ServiceFactory();


        Bundle b = getIntent().getExtras();

        if (b != null) {
            currentArret = (Arret) b.getSerializable("arret");
            currentRoute = (Route)b.getSerializable("route");
            choiceDirection = b.getInt("choiceDirection");
            nameDirection = b.getString("nameDirection");
            // indique si le bouton de sauvegarde doit être activé ou non (si l'arret est déjà enregistré ou non)
            flagSaveButtonVisibility = b.getBoolean("flagSaveButton");
            isTram = b.getBoolean("isTram");
        }

        ImageView horloge = manageUIPreTreatment();






        /*
        Bloc réservé à la notification et à l'affichage du temps restant
        La demande à l'API se fait au travers d'un service (= une activité qui n'a pas de layout en quelque sorte). Cela sert pour la notification.
        Basé sur le TP4
         */
        final Intent arretIntent = new Intent(this, StoptimeService.class);
        Bundle bun = new Bundle();
        bun.putSerializable("arret", currentArret);
        bun.putSerializable("route", currentRoute);
        bun.putInt("choiceDirection", choiceDirection);
        arretIntent.putExtras(bun);
        //startService(arretIntent);


        updateEvery30Sec(arretIntent);

        BroadcastReceiver receiver = onReceiveFromService();

        IntentFilter myServiceIntentFilter = new IntentFilter(
                "ACTION_DONE");

        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                myServiceIntentFilter);


        manageUIPostTreatment(horloge, arretIntent);


    }

    private void manageUIPostTreatment(ImageView horloge, final Intent arretIntent) {
        // Boutons de sauvegarde et de la map
        ImageView b_save = (ImageView) findViewById(R.id.button_save);
        TextView lbl_save = (TextView) findViewById(R.id.lbl_save);
        ImageView button_map = (ImageView) findViewById(R.id.button_map);
        TextView lbl_map = (TextView) findViewById(R.id.lbl_save);

        //Si cet arrêt est déjà enregistré en favori
        final StorageService storage = new StorageImpl();
        Data_Arret_Route_Direction ard = new Data_Arret_Route_Direction(currentArret, currentRoute, choiceDirection, nameDirection);
        if(storage.isFavori(getApplicationContext(), ard)) {
            b_save.setBackgroundResource(R.drawable.cerclegrey);
            //b_save.setClickable(false);
            flagSaveButtonVisibility = false;


        }

        // si le bouton de sauvegarde doit etre activé

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data_Arret_Route_Direction ard = new Data_Arret_Route_Direction(currentArret, currentRoute, choiceDirection, nameDirection);
                if (flagSaveButtonVisibility){
                    // Stockage de l'ARD local

                    storage.add(getApplicationContext(), ard);
                    Toast.makeText(getApplicationContext(), "Arret enregistré !", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.cerclegrey);
                    //view.setClickable(false);
                    flagSaveButtonVisibility = false;
                }else {

                    storage.delete(getApplicationContext(), ard);
                    Toast.makeText(getApplicationContext(), "Arret supprimé !", Toast.LENGTH_SHORT).show();
                    view.setBackgroundResource(R.drawable.cerclepink);
                    flagSaveButtonVisibility = true;
                }
            }
        });
/*
        }else{
            b_save.setBackgroundResource(R.drawable.cerclegrey);
        }
*/
        // Bouton de retour au menu principal
        Button b_back_main = (Button) findViewById(R.id.button_back_main);
        b_back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                stopService(arretIntent);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


        // Bouton de la map
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                stopService(arretIntent);

                // Passage de la longitude et la latitude à l'intent
                Bundle b= new Bundle();
                b.putDouble("latitude", currentArret.getLat());
                b.putDouble("longitude", currentArret.getLon());
                b.putString("nameArret", currentArret.getName());

                Intent i = new Intent(getApplicationContext(), activity_map.class);
                i.putExtras(b);
                startActivityForResult(i, 100);


            }
        });

        // Au clic sur l'horloge, rafaichissement immédiat de l'heure indiquée
        horloge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handler.removeCallbacks(runnable);
                stopService(arretIntent);
                startService(arretIntent);
            }
        });
    }

    @NonNull
    private BroadcastReceiver onReceiveFromService() {
        return new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(!intent.getStringExtra("errorMsg").equals("")){
                        TextView text_time = (TextView) findViewById(R.id.text_next_time);
                        text_time.setText("Erreur lors de la requête à l'API\n\n"+intent.getStringExtra("errorMsg"));
                    }else {


                        Intent intentReceive = new Intent(context, Activity_Show_Stoptimes.class);
                        // Ces flags servent à afficher l'activité Activity_Show_Stoptimes si elle existe déjà lors du clic sur la notification,
                        // ou la créer si elle n'existe pas (dans notre cas elle existera toujours je suppose)
                        intentReceive.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent intentPendingReceive =
                                PendingIntent.getActivity(Activity_Show_Stoptimes.this, 5, intentReceive, 0);

                        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Activity_Show_Stoptimes.this)
                                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                                .setContentTitle(currentArret.getName())
                                .setContentText("Passage dans " + intent.getStringArrayListExtra("stopTime").get(0))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);
                                /*.addAction(android.R.drawable.ic_menu_compass, "Voir détails",
                                        intentPendingReceive);*/
                                // Ne marche plus depuis qu'on a fait en sorte que lorsqu'on retourne au menu principal,
                                // l'activité se finish()

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Activity_Show_Stoptimes.this);
                        notificationManagerCompat.notify(8, mBuilder.build());

                        // Li piti texte qui s'affiche
                        TextView text_time = (TextView) findViewById(R.id.text_next_time);
                        ArrayList<String> stopTimes = intent.getStringArrayListExtra("stopTime");
                        if (stopTimes.size() == 1){
                            text_time.setText("Prochain passage dans\n"+stopTimes.get(0));
                        }else{
                            text_time.setText("1er passage dans\n" + stopTimes.get(0)+"\n\n2e passage dans\n"+stopTimes.get(1));
                        }

                    }

                }
            };
    }

    private void updateEvery30Sec(final Intent arretIntent) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startService(arretIntent);
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private ImageView manageUIPreTreatment() {
        // Texte de résumé de la ligne, arret, direction
        // Panneau du haut résumant les caractéristiques de l'ARD choisis
        TextView text_title_route = (TextView) findViewById(R.id.text_title_route);
        ImageView horloge = (ImageView) findViewById(R.id.horloge_icon);
        ImageView trambus = (ImageView) findViewById(R.id.trambus_icon);
        Button back_menu = (Button) findViewById(R.id.button_back_main);
        String tmp = "";
        if (isTram){
            tmp += "Tram ";
        }else{
            tmp += "Bus ";
            horloge.setBackgroundResource(R.drawable.cerclegreen);
            trambus.setBackgroundResource(R.drawable.cerclegreen);
            trambus.setImageResource(R.drawable.ic_directions_bus_black_24dp);
            back_menu.setBackgroundColor(getResources().getColor(R.color.green));
        }
        tmp += currentRoute.getShortName()+"\nArret "+currentArret.getName()+"\nDirection "+nameDirection;
        text_title_route.setText(tmp);
        return horloge;
    }

}
