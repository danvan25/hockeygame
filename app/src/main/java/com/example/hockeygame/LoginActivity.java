package com.example.hockeygame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
    }

    private void initializeListeners() {
        buttonLogin.setOnClickListener(view -> handleLogin());

        buttonCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = editTextEmail.getText()
                .toString()
                .trim();

        String password = editTextPassword.getText()
                .toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Email address is required.");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        Log.d(TAG, "Login pressed: " + email);

        Toast.makeText(
                this,
                "Login successful — temporary test",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                LoginActivity.this,
                MainMenuActivity.class
        );

        startActivity(intent);
        finish();
    }
}