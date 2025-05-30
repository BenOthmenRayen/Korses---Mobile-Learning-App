package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ManageUsersActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button addUserButton, editUserButton, deleteUserButton;
    private TextView userInfoTextView;
    private LinearLayout foundUserLayout;

    private DatabaseReference usersRef;
    private User foundUser;
    private String foundUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        searchEditText = findViewById(R.id.searchEditText);
        addUserButton = findViewById(R.id.addUserButton);
        userInfoTextView = findViewById(R.id.userInfoTextView);
        editUserButton = findViewById(R.id.editUserButton);
        deleteUserButton = findViewById(R.id.deleteUserButton);
        foundUserLayout = findViewById(R.id.foundUserLayout);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        addUserButton.setOnClickListener(v -> showAddUserDialog());

        editUserButton.setOnClickListener(v -> showEditUserDialog());

        deleteUserButton.setOnClickListener(v -> deleteUser());

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            String searchText = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(searchText)) {
                searchUserByName(searchText);
            }
            return true;
        });
    }

    private void searchUserByName(String name) {
        Query query = usersRef.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        foundUser = userSnapshot.getValue(User.class);
                        foundUserId = userSnapshot.getKey();
                        if (foundUser != null) {
                            foundUserLayout.setVisibility(View.VISIBLE);
                            userInfoTextView.setText(
                                    "Name: " + foundUser.getName() + "\n" +
                                            "Email: " + foundUser.getEmail() + "\n" +
                                            "Phone: " + foundUser.getPhone() + "\n" +
                                            "Password: " + foundUser.getPassword()
                            );
                        }
                    }
                } else {
                    Toast.makeText(ManageUsersActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    foundUserLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUsersActivity.this, "Error searching user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddUserDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        EditText nameInput = dialogView.findViewById(R.id.nameEditText);
        EditText emailInput = dialogView.findViewById(R.id.emailEditText);
        EditText phoneInput = dialogView.findViewById(R.id.phoneEditText);
        EditText passwordInput = dialogView.findViewById(R.id.passwordEditText);

        new AlertDialog.Builder(this)
                .setTitle("Add New User")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameInput.getText().toString();
                    String email = emailInput.getText().toString();
                    String phone = phoneInput.getText().toString();
                    String password = passwordInput.getText().toString();

                    if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !password.isEmpty()) {
                        String id = usersRef.push().getKey();
                        User newUser = new User(name, email, phone, password);
                        usersRef.child(id).setValue(newUser);
                        Toast.makeText(ManageUsersActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManageUsersActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showEditUserDialog() {
        if (foundUser == null) return;

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        EditText nameInput = dialogView.findViewById(R.id.nameEditText);
        EditText emailInput = dialogView.findViewById(R.id.emailEditText);
        EditText phoneInput = dialogView.findViewById(R.id.phoneEditText);
        EditText passwordInput = dialogView.findViewById(R.id.passwordEditText);

        nameInput.setText(foundUser.getName());
        emailInput.setText(foundUser.getEmail());
        phoneInput.setText(foundUser.getPhone());
        passwordInput.setText(foundUser.getPassword());

        new AlertDialog.Builder(this)
                .setTitle("Edit User")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    foundUser.setName(nameInput.getText().toString());
                    foundUser.setEmail(emailInput.getText().toString());
                    foundUser.setPhone(phoneInput.getText().toString());
                    foundUser.setPassword(passwordInput.getText().toString());

                    usersRef.child(foundUserId).setValue(foundUser);
                    Toast.makeText(ManageUsersActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    searchUserByName(foundUser.getName());
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void deleteUser() {
        if (foundUserId != null) {
            usersRef.child(foundUserId).removeValue();
            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
            foundUserLayout.setVisibility(View.GONE);
        }
    }
}
