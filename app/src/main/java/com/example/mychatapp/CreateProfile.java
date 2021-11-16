package com.example.mychatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mychatapp.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class CreateProfile extends AppCompatActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private ImageView mUserImage;
    private FloatingActionButton fab;
    private ProgressBar mProgressBar;
    private Uri imageData;
    private final static int IMAGE_PICK_REQUEST_CODE = 1;
    private String imageUrl;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        // Set title for action bar
        setTitle("Your Profile");
        initialize();

        // Set the OnClickListener() for the user to select the image
        mUserImage.setOnClickListener(v -> {
            // Implicit intent to pick image from the phone storage
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(i, IMAGE_PICK_REQUEST_CODE);
        });

        // Set the OnClickListener() for the floating action button
        fab.setOnClickListener(v -> {
            if (mFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(CreateProfile.this, "Enter the First Name", Toast.LENGTH_SHORT).show();
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            sendDataForNewUser();   // Calling method to store the data to firebase
            mProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            // Starting the chat activity
            startActivity(new Intent(CreateProfile.this, MainActivity.class));
            finish();
        });
    }

    private void sendDataForNewUser() {
        sendDataToRealtimeDB();
        if (imageData != null) {
            sendImageToFirebaseStorage();
        } else {
            sendDataToFirebaseFirestore();
        }
    }

    private void sendDataToRealtimeDB() {
        userName = mFirstName.getText().toString().trim();
        if (!mLastName.getText().toString().trim().isEmpty()) {
            userName += " " + mLastName.getText().toString().trim();
        }

        // Get the instance of Firebase Realtime DB
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        User user = new User(userName, FirebaseAuth.getInstance().getUid());
        // Adding user to DB
        reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.v("Add User", "User Added successfully to real-time DB");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Add User", "Add User unsuccessfully to real-time DB");
            }
        });
    }

    private synchronized void sendImageToFirebaseStorage() {
        StorageReference reference = FirebaseStorage.getInstance()
                .getReference()
                .child("User Images")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("Profile Image");

        // Compress image for convenient storage of profile images
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
        } catch (IOException e) {
            Log.e("Image Compression Error", "IO Error: " + e.getMessage());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null) bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        // Send image to firebase storage
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrl = uri.toString();
                        sendDataToFirebaseFirestore();
                        Log.v("Get URL", "URL received successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Get URL", "URL receive failed");
                    }
                });
                Log.v("Image Upload", "Image uploaded successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Image Upload", "Image upload failed");
            }
        });
    }

    private void sendDataToFirebaseFirestore() {
        DocumentReference reference = FirebaseFirestore.getInstance()
                .collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("userName", userName);
        userDetails.put("userImage", imageUrl);
        userDetails.put("uid", FirebaseAuth.getInstance().getUid());
        userDetails.put("status", "Online");

        reference.set(userDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.v("Add User", "User Added successfully to firestore");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Add User", "Add User unsuccessfully to firestore");
                    }
                });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case IMAGE_PICK_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Launch the image cropping activity
                        CropImage.activity(data.getData())
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(140, 140)
                                .setFixAspectRatio(true)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .start(this);
                    }
                }

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK && result != null) {
                    imageData = result.getUri();
                    mUserImage.setPaddingRelative(0, 0, 0, 0);  // Set padding to 0
                    mUserImage.setImageURI(imageData);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE && result != null) {
                    Log.e("Image Select", "Cropping Error: " + result.getError().toString());
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Method to initialize the view class objects with the views
     * from the layout resource file.
     *
     */
    private void initialize() {
        mFirstName = findViewById(R.id.editTextUserFirstName);
        mLastName = findViewById(R.id.editTextUserLastName);
        mUserImage = findViewById(R.id.userImageButton);
        fab = findViewById(R.id.fabCreateProfile);
        mProgressBar = findViewById(R.id.createProfileProgressBar);
    }


}