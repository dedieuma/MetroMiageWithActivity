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

public class activity_ligne_selection extends AppCompatActivity {

    ServiceFactory treatment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ligne_selection);
        treatment = new ServiceFactory();

        Bundle b = getIntent().getExtras();

        MetroInterface service = ServiceFactory.getInstance();
        service.getRoutes().enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                if(response.isSuccessful()){
                    final List<Route> routesTram = treatment.getTramLigne(response.body());
                    final List<String> routes = treatment.getTramLignesToString(routesTram);


                    ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_ligne);
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
