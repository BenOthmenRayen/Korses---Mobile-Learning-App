package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupFragment extends Fragment {

    private DatabaseReference dbRef;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signupframe, container, false);

        EditText name = view.findViewById(R.id.etName);
        EditText email = view.findViewById(R.id.etEmail);
        EditText phone = view.findViewById(R.id.etPhone);
        EditText password = view.findViewById(R.id.etPassword);
        EditText confirmPassword = view.findViewById(R.id.etConfirmPassword);
        Button signupBtn = view.findViewById(R.id.auth_button);

        signupBtn.setOnClickListener(v -> {
            String username = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPhone = phone.getText().toString().trim();
            String userPass = password.getText().toString().trim();
            String userConfirmPass = confirmPassword.getText().toString().trim();
            if (!validateInputs(username, userEmail, userPhone, userPass)) {
                return;
            }

            showProgressDialog();

            String userId = dbRef.push().getKey();
            HashMap<String, Object> userData = new HashMap<>();
            userData.put("name", username);
            userData.put("email", userEmail);
            userData.put("phone", userPhone);
            userData.put("password", userPass);

            dbRef.child(userId).setValue(userData)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            navigateToHomeActivity(userId);
                        } else {
                            handleSignupFailure(task.getException());
                        }
                    });
        });

        return view;
    }

    private boolean validateInputs(String username, String email, String phone, String password) {
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Le nom est requis", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "Email invalide", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(phone) || phone.length() < 8) {
            Toast.makeText(getContext(), "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Toast.makeText(getContext(), "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Création du compte...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void navigateToHomeActivity(String userId) {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void handleSignupFailure(Exception exception) {
        Log.e("FirebaseError", "Erreur d'inscription", exception);
        String errorMessage = "Échec de la création du compte";

        if (exception != null && exception.getMessage() != null) {
            errorMessage += ": " + exception.getMessage();
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroyView();
    }
}