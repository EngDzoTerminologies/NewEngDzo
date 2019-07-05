package ddc.dict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import ddc.dict.R;

public class PopupActivity extends AppCompatActivity {


    private Switch translateinDzo;
    private TextView about;
    private TextView versions;
    private TextView message;
    private TextView developer1;
    private TextView developer2;
    private TextView developer3;
    private TextView developer4;
    private TextView developer5;
    private TextView engtrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        about = findViewById(R.id.aboutappID);
        versions = findViewById(R.id.versionID);
        message = findViewById(R.id.messageID);
        developer1 = findViewById(R.id.developer1);
        developer2 = findViewById(R.id.developer2);
        developer3 = findViewById(R.id.developer3);
        developer4 = findViewById(R.id.developer4);
        developer5 = findViewById(R.id.developer5);
        about.setText("རིམ་ལུག་ཀྱི་སྐོར།");
        versions.setText("ཐོན་རིམ ༡.༠");
        message.setText(R.string.Dzoaboutapp);
        developer1.setText(R.string.Dzodeveloper1);
        developer2.setText(R.string.Dzodeveloper2);
        developer3.setText(R.string.Dzodeveloper3);
        developer4.setText(R.string.Dzodeveloper4);
        developer5.setText(R.string.Dzodeveloper5);

        translateinDzo = findViewById(R.id.translateaboutappinDzo);
        engtrans = findViewById(R.id.engtext);

        translateinDzo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    about.setText("About App");
                    versions.setText("Version 1.0");
                    message.setText(R.string.aboutapp);
                    developer1.setText(R.string.developer1);
                    developer2.setText(R.string.developer2);
                    developer3.setText(R.string.developer3);
                    developer4.setText(R.string.developer4);
                    developer5.setText(R.string.developer5);
                }else{

                    about.setText("རིམ་ལུག་ཀྱི་སྐོར།");
                    versions.setText("ཐོན་རིམ ༡.༠");
                    message.setText(R.string.Dzoaboutapp);
                    developer1.setText(R.string.Dzodeveloper1);
                    developer2.setText(R.string.Dzodeveloper2);
                    developer3.setText(R.string.Dzodeveloper3);
                    developer4.setText(R.string.Dzodeveloper4);
                    developer5.setText(R.string.Dzodeveloper5);
                }

            }
        });
        try{
            DisplayMetrics displayMetrics=new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width=displayMetrics.widthPixels;
            int height=displayMetrics.heightPixels;

            getWindow().setLayout((int)(width*.9), (int)(height*.9));
            WindowManager.LayoutParams params=getWindow().getAttributes();
            params.gravity=Gravity.CENTER;
            params.x=0;
            params.y=-25;

            getWindow().setAttributes(params);

        }
        catch (Exception e)
        {

        }



    }
}
