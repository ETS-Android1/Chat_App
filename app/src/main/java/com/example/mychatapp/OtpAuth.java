package com.example.mychatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OtpAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        Intent intent = getIntent();
        String ph = intent.getStringExtra("phoneNumber");
        Toast.makeText(this, ph, Toast.LENGTH_LONG).show();
    }
}