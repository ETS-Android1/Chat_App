package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText mEditText;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab;

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
                fab.setImageResource(0);
                mProgressBar.setVisibility(View.VISIBLE);

                // Sending the Otp using the PhoneAuthProvider
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        ccp.getFullNumberWithPlus(),
                        60, TimeUnit.SECONDS,
                        PhoneVerification.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                mProgressBar.setVisibility(View.GONE);
                                fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);

                                /* TODO:
                                *  Auto-retrieval of otp code.
                                */
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                mProgressBar.setVisibility(View.GONE);
                                fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
                                Toast.makeText(PhoneVerification.this, e.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                mProgressBar.setVisibility(View.GONE);
                                fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
                                Intent intent = new Intent(PhoneVerification.this, OtpAuth.class);
                                intent.putExtra(
                                        "phoneNumberToDisplay",
                                        ccp.getSelectedCountryCodeWithPlus() + "-" + mEditText.getText().toString()
                                );
                                intent.putExtra("verificationCode", s);
                                intent.putExtra("phoneNumber", ccp.getFullNumberWithPlus());
                                startActivity(intent);
                                finish();
                            }
                        });

            } else {
                Toast.makeText(PhoneVerification.this, "invalid number", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        // When the user is already logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent i = new Intent(PhoneVerification.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
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
        fab = findViewById(R.id.fabPhoneVerification);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}