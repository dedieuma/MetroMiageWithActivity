package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import API.Arret;
import API.Route;
import API.ServiceFactory;

/**
 * Created by Andréas on 07/04/2018.
 */

/**
 * Activity_Direction_Selection
 * activité de sélection de la direction de la ligne
 */
public class Activity_Direction_Selection extends AppCompatActivity {

ServiceFactory treatment;
Arret currentArret;
Route currentRoute;
String terminus1, terminus2;
int choiceDirection;
boolean isTram;
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
            isTram = b.getBoolean("isTram");
        }


        // Gestion des 2 boutons de sélection
        CardView b_term_1 = (CardView) findViewById(R.id.button_terminus1);
        CardView b_term_2 = (CardView) findViewById(R.id.button_terminus2);
        ImageView button_left = (ImageView) findViewById(R.id.button_left);
        ImageView button_right = (ImageView) findViewById(R.id.button_right);
        if(isTram){
            button_left.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.cerclepurple));
            button_right.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.cerclepurple));
        } else {
            button_left.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.cerclegreen));
            button_right.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.cerclegreen));
        }

        TextView text_term_1 = (TextView) findViewById(R.id.text_terminus1);
        text_term_1.setText(terminus1);
        TextView text_term_2 = (TextView) findViewById(R.id.text_terminus2);
        text_term_2.setText(terminus2);

        // Ouais le numéro des directions et des boutons sont inversés. Remercie l'API qui a pris les numéros inverses que mon implémentation
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
        b.putBoolean("flagSaveButton", true);
        b.putBoolean("isTram", isTram);
        i.putExtras(b);
        startActivityForResult(i, 4);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 4) {
            if(resultCode == Activity.RESULT_OK){
                Log.d("Direction OK", "transition de Activity_show_stoptime vers activity_direction");
                setResult(Activity.RESULT_OK);
                finish();



            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("Direction CANCEL", "CANCEL de Activity_show_stoptime vers activity_direction");
            }
        }
    }


}
