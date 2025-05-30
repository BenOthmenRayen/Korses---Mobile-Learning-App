package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private TextView tvWelcome;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileImageView = findViewById(R.id.profileImage);
        tvWelcome = findViewById(R.id.tvUserName);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String loggedInEmail = prefs.getString("email", null);

        if (loggedInEmail != null) {
            fetchUserNameFromFirebase(loggedInEmail);
        } else {
            tvWelcome.setText("Welcome!");
        }

        setupMenuClickListeners();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    return true;
                case R.id.nav_courses:
                    startActivity(new Intent(this, MyCoursesActivity.class));
                    return true;
                case R.id.nav_assignments:
                    startActivity(new Intent(this, AssignmentsActivity.class));
                    return true;
                case R.id.nav_notifications:
                    startActivity(new Intent(this, NotificationsActivity.class));
                    return true;
                case R.id.nav_progress:
                    startActivity(new Intent(this, MyProgressActivity.class));
                    return true;
            }
            return false;
        });
    }

    private void fetchUserNameFromFirebase(String email) {
        mDatabase.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean userFound = false;
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    if (emailFromDB != null && emailFromDB.equalsIgnoreCase(email)) {
                        tvWelcome.setText("Welcome, " + (nameFromDB != null ? nameFromDB : "Users") + "!");
                        userFound = true;
                        break;
                    }
                }
            } else {
                Log.e("FirebaseError", "Error fetching user: ", task.getException());
                tvWelcome.setText("Welcome!");
            }
        });
    }

    public void openImageChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupMenuClickListeners() {
        LinearLayout layoutMyCourses = findViewById(R.id.layoutMyCourses);
        LinearLayout layoutAssignments = findViewById(R.id.layoutAssignments);
        LinearLayout layoutNotifications = findViewById(R.id.layoutNotifications);
        LinearLayout layoutMyProgress = findViewById(R.id.layoutMyProgress);
        LinearLayout layoutInfo = findViewById(R.id.layoutInfo);

        layoutMyCourses.setOnClickListener(v -> startActivity(new Intent(this, MyCoursesActivity.class)));
        layoutAssignments.setOnClickListener(v -> startActivity(new Intent(this, AssignmentsActivity.class)));
        layoutNotifications.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        layoutMyProgress.setOnClickListener(v -> startActivity(new Intent(this, MyProgressActivity.class)));
        layoutInfo.setOnClickListener(v -> startActivity(new Intent(this, InfoActivity.class)));

        LinearLayout layoutlogout = findViewById(R.id.layoutLogout);
        layoutlogout.setOnClickListener(v -> {

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, LoginFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
