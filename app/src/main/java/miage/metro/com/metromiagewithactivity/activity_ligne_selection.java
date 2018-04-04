package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class activity_ligne_selection extends AppCompatActivity {

    int counterClick = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ligne_selection);

        Bundle b = getIntent().getExtras();

        ListView ligne_liste = (ListView) findViewById(R.id.listview_selection_ligne);
        String[] ressources= getResources().getStringArray(R.array.test_fill_list);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.listview_selection, new ArrayList<String>(Arrays.asList(ressources)));
        ligne_liste.setAdapter(aa);

        ligne_liste.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                Log.i("OK", "OK !");
            }
           });

        /*
        if (b != null)
            counterClick = b.getInt("counter")+1;
        TextView txt = (TextView) findViewById(R.id.textView1);
        txt.setText("Nombre de clicks : "+counterClick);

        Button b1 = (Button) findViewById(R.id.button_go_to_2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("counterClickResult", counterClick);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();



            }
        });

        final Intent i = new Intent(this, activity_arret_selection.class);


        Button b2 = (Button) findViewById(R.id.button_go_to_3);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bun = new Bundle();
                bun.putInt("counter", counterClick);
                i.putExtras(bun);
                startActivityForResult(i, 3);
            }
        });*/
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 3) {
            if(resultCode == Activity.RESULT_OK){
                counterClick = data.getIntExtra("counterClickResult", -2)+1;

                TextView txt = (TextView) findViewById(R.id.textView1);
                txt.setText("Nombre de clicks : "+counterClick);



            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }*/
}
