package miage.metro.com.metromiagewithactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_arret_selection extends AppCompatActivity {
    int counterClick = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_arret_selection);

        Bundle b = getIntent().getExtras();

        /*if (b != null)
            counterClick = b.getInt("counter")+1;
        TextView txt = (TextView) findViewById(R.id.textView2);
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
        });*/

    }
}
