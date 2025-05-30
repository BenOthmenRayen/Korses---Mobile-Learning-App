package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeAdminActivity extends AppCompatActivity {

    private TextView tvUserCount;
    private Button btnAddCourse, btnManageUsers, btnViewUserList, btnSendNotification, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_admin);

        btnAddCourse = findViewById(R.id.btnAddCourse);
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnViewUserList = findViewById(R.id.btnViewUserList);
        btnSendNotification = findViewById(R.id.btnSendNotification);
        View btnhome = findViewById(R.id.btnHome);
        btnLogout = findViewById(R.id.btnLogout);





        btnAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, AddCourseActivity.class);
            startActivity(intent);
        });


        btnManageUsers.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });


        btnViewUserList.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, RegisteredUsersActivity.class);
            startActivity(intent);
        });


        btnSendNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, SendNotificationActivity.class);
            startActivity(intent);
        });

        btnhome.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAdminActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeAdminActivity.this, LoginFragment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserCount() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                tvUserCount.setText("Total Users: " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeAdminActivity.this, "Failed to load user count", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
