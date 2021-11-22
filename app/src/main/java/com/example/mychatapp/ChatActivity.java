package com.example.mychatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private EditText mMessageInput;
    private ImageView mSendMessage;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initialize();

    }

    /**
     * Method to initialize the view class objects with the views
     * from the layout resource file.
     *
     */
    private void initialize() {
        mMessageInput = findViewById(R.id.editTextInputMsg);
        mSendMessage = findViewById(R.id.imageViewSendMsg);

        // Setting views in the toolbar
        ((TextView)findViewById(R.id.textViewReceiverUsername))
                .setText(getIntent().getStringExtra("userName"));
        String url = getIntent().getStringExtra("userImage");
        if (url != null) {
            Picasso.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.user)
                    .into(((ImageView)findViewById(R.id.receivingUserImageView)));
        }
        findViewById(R.id.backButtonChatActivity).setOnClickListener(v -> {
            finish();
        });
    }
}