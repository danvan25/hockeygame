package com.example.hockeygame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hockeygame.R;
import com.example.hockeygame.game.network.AuthApi;
import com.example.hockeygame.game.network.RetrofitClient;
import com.example.hockeygame.game.network.model.LoginRequest;
import com.example.hockeygame.game.network.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonCreateAccount;

    private AuthApi authApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        initializeNetwork();
        initializeListeners();
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
    }

    private void initializeNetwork() {
        authApi = RetrofitClient.getAuthApi();
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
        String username = editTextUsername.getText()
                .toString()
                .trim();

        String password = editTextPassword.getText()
                .toString();

        if (username.isEmpty()) {
            editTextUsername.setError("Username is required.");
            editTextUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required.");
            editTextPassword.requestFocus();
            return;
        }

        buttonLogin.setEnabled(false);

        Log.d(TAG, "Sending login request for: " + username);

        LoginRequest loginRequest =
                new LoginRequest(username, password);

        authApi.login(loginRequest).enqueue(
                new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(
                            Call<LoginResponse> call,
                            Response<LoginResponse> response
                    ) {
                        buttonLogin.setEnabled(true);

                        if (response.isSuccessful()
                                && response.body() != null) {

                            handleSuccessfulLogin(response.body());
                            return;
                        }

                        handleFailedLogin(response.code());
                    }

                    @Override
                    public void onFailure(
                            Call<LoginResponse> call,
                            Throwable throwable
                    ) {
                        buttonLogin.setEnabled(true);

                        Log.e(
                                TAG,
                                "Login request failed",
                                throwable
                        );

                        Toast.makeText(
                                LoginActivity.this,
                                "Cannot connect to the server.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void handleSuccessfulLogin(
            LoginResponse loginResponse
    ) {
        Toast.makeText(
                this,
                "Login successful.",
                Toast.LENGTH_SHORT
        ).show();

        Intent intent = new Intent(
                LoginActivity.this,
                MainMenuActivity.class
        );

        intent.putExtra(
                "username",
                loginResponse.getUsername()
        );

        startActivity(intent);
        finish();
    }

    private void handleFailedLogin(int statusCode) {
        if (statusCode == 401) {
            Toast.makeText(
                    this,
                    "Invalid username or password.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        Log.e(
                TAG,
                "Login failed with HTTP status: "
                        + statusCode
        );

        Toast.makeText(
                this,
                "Login failed. Server error: " + statusCode,
                Toast.LENGTH_LONG
        ).show();
    }
}