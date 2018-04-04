package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Activity1 extends AppCompatActivity {

    int counterClick = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        Bundle b = getIntent().getExtras();

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

        final Intent i = new Intent(this, Activity2.class);


        Button b2 = (Button) findViewById(R.id.button_go_to_3);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bun = new Bundle();
                bun.putInt("counter", counterClick);
                i.putExtras(bun);
                startActivityForResult(i, 3);
            }
        });
    }


    @Override
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
    }
}
