package com.example.grahaksuraksha.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grahaksuraksha.R;
import com.example.grahaksuraksha.UI.Activity.Main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        TextView versionTv = findViewById(R.id.version);
        ImageView applogo = findViewById(R.id.imageView);

//        //FullScreen
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//
//        }
        try {
            versionTv.setText("Version: "+getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Animation anim1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splashanimation);
        applogo.setAnimation(anim1);

        new Handler().postDelayed((Runnable) () -> {
            onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
            boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

            if(isFirstTime){
                SharedPreferences.Editor editor = onBoardingScreen.edit();
                //Todo change to false
                editor.putBoolean("firstTime", true);
                editor.apply();
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
                finish();
            }else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }

}