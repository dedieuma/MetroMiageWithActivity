package miage.metro.com.metromiagewithactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import API.Arret;
import API.Route;
import API.ServiceFactory;

/**
 * Created by Andréas on 07/04/2018.
 */

public class Activity_Direction_Selection extends AppCompatActivity {

ServiceFactory treatment;
Arret currentArret;
Route currentRoute;
String terminus1, terminus2;
int choiceDirection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_direction_selection);
        treatment = new ServiceFactory();

        Bundle b = getIntent().getExtras();

        if (b != null) {
            currentArret = (Arret) b.getSerializable("arret");
            terminus1 = b.getString("terminus1");
            terminus2 = b.getString("terminus2");
            currentRoute = (Route)b.getSerializable("route");
        }


        Button b_term_1 = (Button) findViewById(R.id.button_terminus1);
        Button b_term_2 = (Button) findViewById(R.id.button_terminus2);

        TextView text_term_1 = (TextView) findViewById(R.id.text_terminus1);
        text_term_1.setText(terminus1);
        TextView text_term_2 = (TextView) findViewById(R.id.text_terminus2);
        text_term_2.setText(terminus2);

        b_term_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceDirection = 2;
                launchActivityShowStopTimes(choiceDirection);
            }
        });

        b_term_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceDirection = 1;
                launchActivityShowStopTimes(choiceDirection);
            }
        });


    }

    private void launchActivityShowStopTimes(int choiceDirection) {

        Intent i = new Intent(getBaseContext(), Activity_Show_Stoptimes.class);
        Bundle b = new Bundle();

        b.putSerializable("arret", currentArret);
        b.putSerializable("route", currentRoute);
        b.putInt("choiceDirection", choiceDirection);
        if (choiceDirection==1){
            b.putString("nameDirection", terminus2);
        }else{
            b.putString("nameDirection", terminus1);
        }
        i.putExtras(b);
        startActivityForResult(i, 3);
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
