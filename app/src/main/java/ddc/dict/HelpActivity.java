package ddc.dict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import ddc.dict.R;

public class HelpActivity extends AppCompatActivity {

    private Switch translateinDzo;
    private TextView help;
    private TextView point1;
    private TextView point2;
    private TextView point3;
    private TextView point4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        help = findViewById(R.id.help);
        help.setText("རོགས་རམ།");
        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        point3 = findViewById(R.id.point3);
        point4 = findViewById(R.id.point4);
        translateinDzo = findViewById(R.id.translatefeedbackDzo);

        translateinDzo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    help.setText("Help");
                    point1.setText(R.string.point1);
                    point2.setText(R.string.point2);
                    point3.setText(R.string.point3);
                    point4.setText(R.string.point4);
                }else{

                    help.setText("རོགས་རམ།");
                    point1.setText(R.string.Dzopoint1);
                    point2.setText(R.string.Dzopoint2);
                    point3.setText(R.string.Dzopoint3);
                    point4.setText(R.string.Dzopoint4);

                }
            }
        });
    }
}
