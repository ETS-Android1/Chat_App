package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mychatapp.models.UserFireStore;
import com.example.mychatapp.utilities.UserViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private FirestoreRecyclerAdapter<UserFireStore, UserViewHolder> chatAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth.getInstance();
        mRecyclerView = findViewById(R.id.usersRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);

        Query query = FirebaseFirestore.getInstance()
                .collection("Users");

        FirestoreRecyclerOptions<UserFireStore> options = new FirestoreRecyclerOptions.Builder<UserFireStore>()
                .setQuery(query, UserFireStore.class)
                .build();

        // Firestore recycler adapter
        chatAdapter = new FirestoreRecyclerAdapter<UserFireStore, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder mUserViewHolder, int i, @NonNull UserFireStore userFireStore) {
                mUserViewHolder.setUsername(userFireStore.getUserName());
                mUserViewHolder.setImage(MainActivity.this, userFireStore.getUserImage());
                mUserViewHolder.setStatus(userFireStore.getStatus());

                mUserViewHolder.itemView.setOnClickListener(v -> {

                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_container_chat, parent, false);

                return new UserViewHolder(view);
            }
        };


        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                mLinearLayoutManager.getOrientation()));
        mRecyclerView.setAdapter(chatAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chatAdapter != null) {
            chatAdapter.stopListening();
        }
    }
}