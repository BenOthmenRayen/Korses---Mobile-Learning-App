package com.example.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class Inscription extends AppCompatActivity {

    private TextView loginTab, signupTab;
    private View underline;
    private FrameLayout formContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscription);

        loginTab = findViewById(R.id.loginTab);
        signupTab = findViewById(R.id.signupTab);
        underline = findViewById(R.id.underline);
        formContainer = findViewById(R.id.formContainer);

        loadFragment(new SignupFragment());
        highlightTab(signupTab);

        loginTab.setOnClickListener(v -> {
            loadFragment(new LoginFragment());
            highlightTab(loginTab);
        });

        signupTab.setOnClickListener(v -> {
            loadFragment(new SignupFragment());
            highlightTab(signupTab);
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.formContainer, fragment)
                .commit();
    }

    private void highlightTab(TextView selectedTab) {
        loginTab.setTextColor(Color.BLACK);
        signupTab.setTextColor(Color.BLACK);

        selectedTab.setTextColor(Color.parseColor("#DAA520"));



        underline.animate()
                .x(selectedTab.getX())
                .setDuration(200)
                .start();
    }
}

