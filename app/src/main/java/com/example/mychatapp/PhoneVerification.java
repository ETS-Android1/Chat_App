package com.example.mychatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class PhoneVerification extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText mEditText;
    FirebaseAuth firebaseAuth;
    ProgressBar mProgressBar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        // Setting title for action bar
        setTitle("Your Phone");
        initialize();


        /*
         * Setting the OnClickListener() for the floating action button
         * If the country code is valid then otp is sent
         * Else a toast is displayed for the invalid number.
         */
        fab.setOnClickListener(view -> {
            if (ccp.isValidFullNumber()) {
                Intent intent = new Intent(PhoneVerification.this, OtpAuth.class);
                intent.putExtra("phoneNumber", ccp.getFullNumberWithPlus());
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "invalid number", Toast.LENGTH_LONG).show();
            }
//            fab.setImageResource(0);
//            mProgressBar.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Method to initialize the view class objects with the views
     * from the layout resource file.
     *
     */
    private void initialize() {
        ccp = findViewById(R.id.ccp);
        mEditText = findViewById(R.id.editTextPhoneNumber);
        mProgressBar = findViewById(R.id.phoneVerificationProgressBar);

        // Registering the country code picker to work with the EditText
        ccp.registerCarrierNumberEditText(mEditText);
        fab = findViewById(R.id.fab);
    }
}