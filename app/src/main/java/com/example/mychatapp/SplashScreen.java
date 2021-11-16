package com.example.mychatapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set the splash screen image
        ImageView mImageView = findViewById(R.id.splashScreenIcon);
        mImageView.setImageResource(R.drawable.chat_app_icon);
        Animation iconAnimation = AnimationUtils.loadAnimation(SplashScreen.this,
                R.anim.splash_screen_icon_animation);
        mImageView.startAnimation(iconAnimation);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this,
                    PhoneVerification.class);

            startActivity(i);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}