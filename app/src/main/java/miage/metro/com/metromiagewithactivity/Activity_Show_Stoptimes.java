package miage.metro.com.metromiagewithactivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import API.Arret;
import API.MetroInterface;
import API.Route;
import API.ServiceFactory;
import API.StopTime.StopTime;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andréas on 07/04/2018.
 */

public class Activity_Show_Stoptimes extends AppCompatActivity {

    ServiceFactory treatment;
    Arret currentArret;
    Route currentRoute;
    int choiceDirection;
    String nameDirection;
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
        }

        TextView text_title_route = (TextView) findViewById(R.id.text_title_route);
        String tmp = "Tram "+currentRoute.getShortName()+"\nArret "+currentArret.getName()+"\nDirection "+nameDirection;
        text_title_route.setText(tmp);





        /*MetroInterface service = ServiceFactory.getInstance();
        service.getStopTimes(currentArret.getCode()).enqueue(new Callback<List<StopTime>>() {
            @Override
            public void onResponse(Call<List<StopTime>> call, Response<List<StopTime>> response) {
                if(response.isSuccessful()){
                    final List<StopTime> stopTimes = treatment.getStopTime(response.body(), currentRoute.getShortName(), choiceDirection);

                    TextView text_time = (TextView) findViewById(R.id.text_next_time);
                    final String time = treatment.getDrawingTime(stopTimes.get(0).getTimes().get(0).getRealtimeDeparture());
                    text_time.setText("Prochain arret dans\n"+time.toString());

                    BroadcastReceiver receiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            TextView text_time = (TextView) findViewById(R.id.text_next_time);
                            final String time = treatment.getDrawingTime(stopTimes.get(0).getTimes().get(0).getRealtimeDeparture());
                            text_time.setText("Prochain arret dans\n"+time.toString());
                        }
                    };

                }
            }



            @Override
            public void onFailure(Call<List<StopTime>> call, Throwable t) {
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });*/

        final Intent arretIntent = new Intent(this, ArretService.class);
        Bundle bun = new Bundle();
        bun.putSerializable("arret", currentArret);
        bun.putSerializable("route", currentRoute);
        bun.putInt("choiceDirection", choiceDirection);
        arretIntent.putExtras(bun);
        startService(arretIntent);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent intentReceive = new Intent(context, Activity_Show_Stoptimes.class);
                intentReceive.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intentPendingReceive =
                        PendingIntent.getActivity(Activity_Show_Stoptimes.this, 0, intentReceive, 0);

                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(Activity_Show_Stoptimes.this)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle(currentArret.getName())
                        .setContentText("Passage dans "+intent.getStringExtra("stopTime"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .addAction(android.R.drawable.ic_menu_compass, "Voir détails",
                                intentPendingReceive);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Activity_Show_Stoptimes.this);
                notificationManagerCompat.notify(8, mBuilder.build());

                TextView text_time = (TextView) findViewById(R.id.text_next_time);
                text_time.setText("Prochain arret dans\n"+intent.getStringExtra("stopTime"));
            }
        };

        IntentFilter myServiceIntentFilter = new IntentFilter(
                "ACTION_DONE");

        LocalBroadcastManager.getInstance(this).registerReceiver(
                receiver,
                myServiceIntentFilter);




    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                Log.d("transition", "transition de activity_arret vers mainActivity");



            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }*/
}
