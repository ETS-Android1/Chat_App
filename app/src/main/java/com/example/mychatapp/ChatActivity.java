package com.example.mychatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mychatapp.adapters.MessageAdapter;
import com.example.mychatapp.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private EditText mMessageInput;
    private ImageView mSendMessage;
    private RecyclerView mRecyclerView;
    private List<MessageModel> chatMessages;
    private MessageAdapter messageAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Objects.requireNonNull(getSupportActionBar()).hide();
        initialize();
        mSendMessage.setOnClickListener(v -> {
            String message = mMessageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
            }
        });
        listenForMessages();
    }

    private void sendMessage(String message) {
        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("senderId", FirebaseAuth.getInstance().getUid());
        messageMap.put("receiverId", getIntent().getStringExtra("receiverUid"));
        messageMap.put("timestamp", new Date());
        db.collection("chats").add(messageMap);

        mMessageInput.setText(null);
    }

    private void listenForMessages() {
        db.collection("chats")
                .whereEqualTo("senderId", FirebaseAuth.getInstance().getUid())
                .whereEqualTo("receiverId", getIntent().getStringExtra("receiverUid"))
                .addSnapshotListener(eventListener);
        db.collection("chats")
                .whereEqualTo("senderId", getIntent().getStringExtra("receiverUid"))
                .whereEqualTo("receiverId", FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if (error != null) return;
            if (value != null) {
                int count = chatMessages.size();
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        MessageModel messageModel = new MessageModel();
                        messageModel.setMessage(documentChange.getDocument().getString("message"));
                        messageModel.setSenderId(documentChange.getDocument().getString("senderId"));
                        messageModel.setReceiverId(documentChange.getDocument().getString("receivedId"));
                        messageModel.setTimestamp(getReadableTime(documentChange.getDocument().getDate("timestamp")));
                        messageModel.setDate(documentChange.getDocument().getDate("timestamp"));
                        chatMessages.add(messageModel);
                    }
                }

                Collections.sort(chatMessages, (ob1, ob2) -> ob1.getDate().compareTo(ob2.getDate()));
                if (count == 0) {
                    messageAdapter.notifyDataSetChanged();
                } else {
                    messageAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                    mRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                }
            }
        }
    };

    /**
     * Method to initialize the view class objects with the views
     * from the layout resource file.
     *
     */
    private void initialize() {
        mMessageInput = findViewById(R.id.editTextInputMsg);
        mSendMessage = findViewById(R.id.imageViewSendMsg);
        mRecyclerView = findViewById(R.id.messagesRecyclerView);

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

        // Initializing the MessageAdapter and the List
        // and setting the adapter to the recycler view
        chatMessages = new ArrayList<>();
        messageAdapter = new MessageAdapter(ChatActivity.this,
                chatMessages, FirebaseAuth.getInstance().getUid());
        mRecyclerView.setAdapter(messageAdapter);

        // Getting the Firestore instance
        db = FirebaseFirestore.getInstance();
    }

    private String getReadableTime(Date date) {
        return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(date);
    }
}