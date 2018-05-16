package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
            flagSaveButtonVisibility = b.getBoolean("flagSaveButton");
            isTram = b.getBoolean("isTram");
        }

        // Texte de résumé de la ligne, arret, direction
        TextView text_title_route = (TextView) findViewById(R.id.text_title_route);
        String tmp = "";
        if (isTram){
            tmp += "Tram ";
        }else{
            tmp += "Bus ";
        }
        tmp += currentRoute.getShortName()+"\nArret "+currentArret.getName()+"\nDirection "+nameDirection;
        text_title_route.setText(tmp);




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


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startService(arretIntent);
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runnable, 0);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getStringExtra("stopTime").equals("")){
                    TextView text_time = (TextView) findViewById(R.id.text_next_time);
                    text_time.setText("Erreur lors de la requete à l'API MetroMobilité");
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
                            .setContentText("Passage dans " + intent.getStringExtra("stopTime"))
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)
                            .addAction(android.R.drawable.ic_menu_compass, "Voir détails",
                                    intentPendingReceive);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Activity_Show_Stoptimes.this);
                    notificationManagerCompat.notify(8, mBuilder.build());

                    // Li piti texte qui s'affiche
                    TextView text_time = (TextView) findViewById(R.id.text_next_time);
                    text_time.setText("Prochain passage dans\n" + intent.getStringExtra("stopTime"));
                }

            }
        };

        IntentFilter myServiceIntentFilter = new IntentFilter(
                "ACTION_DONE");

        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                myServiceIntentFilter);




        Button b_save = (Button) findViewById(R.id.button_save);
        if (flagSaveButtonVisibility){
            b_save.setVisibility(View.VISIBLE);
            b_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StorageService storage = new StorageImpl();
                    Data_Arret_Route_Direction ard = new Data_Arret_Route_Direction(currentArret, currentRoute, choiceDirection, nameDirection);
                    storage.add(getApplicationContext(), ard);
                    Toast.makeText(getApplicationContext(), "Arret enregistré !", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            b_save.setVisibility(View.GONE);
        }

        Button b_back_main = (Button) findViewById(R.id.button_back_main);
        b_back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(arretIntent);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


        Button button_map = (Button) findViewById(R.id.button_map);
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(arretIntent);

                Bundle b= new Bundle();
                b.putDouble("latitude", currentArret.getLat());
                b.putDouble("longitude", currentArret.getLon());
                b.putString("nameArret", currentArret.getName());

                Intent i = new Intent(getApplicationContext(), activity_map.class);
                i.putExtras(b);
                startActivityForResult(i, 100);


            }
        });

    }

}
