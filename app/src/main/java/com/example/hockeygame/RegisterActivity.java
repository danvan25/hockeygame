package com.example.hockeygame;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button buttonBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        buttonBackToLogin.setOnClickListener(view -> finish());
    }
}