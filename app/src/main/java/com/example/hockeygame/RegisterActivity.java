package com.example.hockeygame;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;

    private Button buttonRegister;
    private Button buttonBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword =
                findViewById(R.id.editTextConfirmPassword);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin);
    }

    private void initializeListeners() {
        buttonRegister.setOnClickListener(view -> handleRegistration());

        buttonBackToLogin.setOnClickListener(view -> finish());
    }

    private void handleRegistration() {
        String username = editTextUsername
                .getText()
                .toString()
                .trim();

        String email = editTextEmail
                .getText()
                .toString()
                .trim();

        String password = editTextPassword
                .getText()
                .toString();

        String confirmPassword = editTextConfirmPassword
                .getText()
                .toString();

        if (!validateUsername(username)) {
            return;
        }

        if (!validateEmail(email)) {
            return;
        }

        if (!validatePassword(password)) {
            return;
        }

        if (!validateConfirmPassword(password, confirmPassword)) {
            return;
        }

        registerUser(username, email, password);
    }

    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required.");
            editTextUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            editTextUsername.setError(
                    "Username must contain at least 3 characters."
            );

            editTextUsername.requestFocus();
            return false;
        }

        if (username.length() > 20) {
            editTextUsername.setError(
                    "Username cannot be longer than 20 characters."
            );

            editTextUsername.requestFocus();
            return false;
        }

        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            editTextUsername.setError(
                    "Only letters, numbers and underscores are allowed."
            );

            editTextUsername.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            editTextEmail.setError("Email address is required.");
            editTextEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email address.");
            editTextEmail.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            editTextPassword.setError(
                    "Password must contain at least 8 characters."
            );

            editTextPassword.requestFocus();
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            editTextPassword.setError(
                    "Password must contain an uppercase letter."
            );

            editTextPassword.requestFocus();
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            editTextPassword.setError(
                    "Password must contain a lowercase letter."
            );

            editTextPassword.requestFocus();
            return false;
        }

        if (!password.matches(".*[0-9].*")) {
            editTextPassword.setError(
                    "Password must contain a number."
            );

            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateConfirmPassword(
            String password,
            String confirmPassword
    ) {
        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError(
                    "Please confirm your password."
            );

            editTextConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError(
                    "Passwords do not match."
            );

            editTextConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void registerUser(
            String username,
            String email,
            String password
    ) {
        // Később itt küldjük el az adatokat a szervernek.

        Toast.makeText(
                this,
                "Account created successfully!",
                Toast.LENGTH_SHORT
        ).show();

        finish();
    }
}