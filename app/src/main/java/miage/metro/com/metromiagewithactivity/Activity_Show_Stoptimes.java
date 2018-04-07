package miage.metro.com.metromiagewithactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
        }

        TextView text_title_route = (TextView) findViewById(R.id.text_title_route);
        String tmp = "Tram "+currentRoute.getShortName();
        text_title_route.setText(tmp);

        MetroInterface service = ServiceFactory.getInstance();
        service.getStopTimes(currentArret.getCode()).enqueue(new Callback<List<StopTime>>() {
            @Override
            public void onResponse(Call<List<StopTime>> call, Response<List<StopTime>> response) {
                if(response.isSuccessful()){
                    final List<StopTime> stopTimes = treatment.getStopTime(response.body(), currentRoute.getShortName(), choiceDirection);

                    TextView text_time = (TextView) findViewById(R.id.text_next_time);
                    String tmpPrime = ""+stopTimes.get(0).getTimes().get(0).getRealtimeDeparture();
                    text_time.setText(""+stopTimes.get(0).getTimes().get(0).getRealtimeDeparture());


                    /*ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_ligne);
                    ArrayAdapter aa = new ArrayAdapter(getBaseContext(), R.layout.listview_selection, routes);
                    ligne_liste.setAdapter(aa);

                    ligne_liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                            Intent i = new Intent(getBaseContext(), activity_arret_selection.class);
                            Bundle b = new Bundle();

                            Route routeChoisie = getRouteFromPosition(position, routesTram);

                            if (routeChoisie != null){
                                b.putSerializable("route", routeChoisie);
                                i.putExtras(b);
                                startActivityForResult(i, 2);
                            }

                        }
                    });*/


                }
            }

            @Override
            public void onFailure(Call<List<StopTime>> call, Throwable t) {
                Log.e("ligne_sel fail", t.getMessage());
            }
        });

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
