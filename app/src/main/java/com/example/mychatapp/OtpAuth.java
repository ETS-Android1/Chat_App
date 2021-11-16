package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpAuth extends AppCompatActivity {
    private EditText otpDigit1;
    private EditText otpDigit2;
    private EditText otpDigit3;
    private EditText otpDigit4;
    private EditText otpDigit5;
    private EditText otpDigit6;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab;
    private TextView mResendCode;
    private String phoneNumber;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);
        // Set title for action bar
        setTitle("Otp verification");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        verificationCode = getIntent().getStringExtra("verificationCode");
        initialize();

        fab.setOnClickListener(v->{
            if (otpDigit1.getText().toString().trim().isEmpty()
                || otpDigit2.getText().toString().trim().isEmpty()
                || otpDigit3.getText().toString().trim().isEmpty()
                || otpDigit4.getText().toString().trim().isEmpty()
                || otpDigit5.getText().toString().trim().isEmpty()
                || otpDigit6.getText().toString().trim().isEmpty()) {
                Toast.makeText(OtpAuth.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            fab.setImageResource(0);
            mProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Retrieve the OTP verification code entered by the user
            String retrievedCode = otpDigit1.getText().toString() +
                    otpDigit2.getText().toString() +
                    otpDigit3.getText().toString() +
                    otpDigit4.getText().toString() +
                    otpDigit5.getText().toString() +
                    otpDigit6.getText().toString();

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, retrievedCode);
            signIn(credential);
        });

        // When user resend the verification code
        mResendCode.setOnClickListener(v->{
            fab.setImageResource(0);
            mProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60, TimeUnit.SECONDS,
                    OtpAuth.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);

                            /* TODO:
                             *  Auto-retrieval of otp code.
                             */
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
                            Toast.makeText(OtpAuth.this, e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
                            // Setting the new verification code
                            verificationCode = s;

                        }
                    }
            );
        });
    }

    private void signIn(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressBar.setVisibility(View.GONE);
                        fab.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
                        if (task.isSuccessful()) {
                            startActivity(new Intent(OtpAuth.this, CreateProfile.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Toast.makeText(OtpAuth.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /**
     * Method to initialize the view class objects with the views
     * from the layout resource file.
     *
     */
    private void initialize() {
        otpDigit1 = findViewById(R.id.otpDigit1);
        otpDigit2 = findViewById(R.id.otpDigit2);
        otpDigit3 = findViewById(R.id.otpDigit3);
        otpDigit4 = findViewById(R.id.otpDigit4);
        otpDigit5 = findViewById(R.id.otpDigit5);
        otpDigit6 = findViewById(R.id.otpDigit6);
        otpDigit1.requestFocus();
        setOtpInput();
        ((TextView)findViewById(R.id.phoneTextView))
                .setText(getIntent().getStringExtra("phoneNumberToDisplay"));
        mResendCode = findViewById(R.id.resendCodeTextView);
        mProgressBar = findViewById(R.id.otpVerificationProgressBar);
        fab = findViewById(R.id.fabOtpVerification);
    }

    /**
     * Method to create a interactive user interface by adding
     * TextChangedListener() on otp EditTexts.
     *
     */
    private void setOtpInput() {
        otpDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) otpDigit2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) otpDigit3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) otpDigit4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) otpDigit5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) otpDigit6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otpDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) otpDigit5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // When user press the back key, it will take the user to
        // phoneVerification activity
        AlertDialog.Builder builder = new AlertDialog.Builder(OtpAuth.this);
        builder.setTitle("MyChatApp")
                .setMessage("Are you sure you want to cancel registration?")
                .setCancelable(false)
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.cancel();
                })
                .setNegativeButton("Stop", (dialog, which)-> {
                    startActivity(new Intent(OtpAuth.this, PhoneVerification.class));
                    finish();
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}