package com.kshrd.ipcam.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kshrd.ipcam.R;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private Thread splashThread;
    private Animation fadeOut, fadeIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startAnimations();
    }

    protected void startAnimations(){

        Animation anim= AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();

        RelativeLayout l= (RelativeLayout) findViewById(R.id.activity_main);
        l.setBackgroundColor(Color.parseColor("#063146"));
        l.clearAnimation();
        l.startAnimation(anim);

        /*anim =AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();*/

        ImageView iv= (ImageView) findViewById(R.id.imgMainLogo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashThread=new Thread(() -> {
            try{
                int waited = 0;
                // Splash screen pause time
                while (waited < 500) {
                    sleep(100);
                    waited += 100;
                }
                Intent intent = new Intent(MainActivity.this,
                        WelcomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                MainActivity.this.finish();

            }catch (InterruptedException i){
                i.printStackTrace();
            }finally {
                MainActivity.this.finish();
            }
        });

        splashThread.start();
    }
}
