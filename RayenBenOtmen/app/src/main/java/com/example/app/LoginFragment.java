package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loginframe, container, false);

        EditText email = view.findViewById(R.id.etEmail);
        EditText password = view.findViewById(R.id.etPassword);
        Button loginBtn = view.findViewById(R.id.auth_button);

        dbRef = FirebaseDatabase.getInstance().getReference("users");

        loginBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPass)) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userEmail.equals("rayen@example.com") && userPass.equals("536320123")) {
                Toast.makeText(getContext(), "Bienvenue Admin Rayen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeAdminActivity.class));
            } else {
                dbRef.orderByChild("email").equalTo(userEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean found = false;
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    String dbPass = snap.child("password").getValue(String.class);
                                    if (dbPass != null && dbPass.equals(userPass)) {
                                        found = true;
                                        Toast.makeText(getContext(), "Bienvenue", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), HomeActivity.class));
                                        break;
                                    }
                                }
                                if (!found) {
                                    Toast.makeText(getContext(), "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), "Erreur base de données", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }
}
