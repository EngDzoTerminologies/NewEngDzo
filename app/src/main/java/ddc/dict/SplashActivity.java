package ddc.dict;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ddc.dict.R;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {
    private static int splashTimeOut=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //slide loader for splash page
     /*   Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        }
                    });
                }
            }
        });
        thread.start();
    }
    protected void onPause(){
        super.onPause();
        finish();


    */


     new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }, splashTimeOut);
    }
}
