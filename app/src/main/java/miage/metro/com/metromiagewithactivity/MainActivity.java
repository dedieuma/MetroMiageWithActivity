package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private int counterClick = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent i = new Intent(this, activity_ligne_selection.class);

        Button but = (Button) findViewById(R.id.button_tram);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("counter", counterClick);
                i.putExtras(b);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Log.i("activityResult", "OUI");
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //counterClick = data.getIntExtra("counterClickResult", -2)+1;

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
