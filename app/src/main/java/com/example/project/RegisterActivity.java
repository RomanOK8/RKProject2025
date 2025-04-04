package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText, emailEditText;
    private Button registerButton;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiClient = new ApiClient(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        emailEditText = findViewById(R.id.email);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(RegisterActivity.this, "No network connection", Toast.LENGTH_SHORT).show();
                    return;
                }

                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!isValidUsername(username)) {
                    Toast.makeText(RegisterActivity.this,
                            "Username must be 6-12 chars (letters and digits)",
                            Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(password)) {
                    Toast.makeText(RegisterActivity.this,
                            "Password must be 6-16 chars with uppercase, lowercase, digit and special char",
                            Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(RegisterActivity.this,
                            "Please enter a valid email address",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Отправляем обычный пароль (сервер сам его хеширует)
                    apiClient.registerUser(username, password, email, new ApiClient.ApiCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            runOnUiThread(() -> {
                                Toast.makeText(RegisterActivity.this,
                                        "Registration successful",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            });
                        }

                        @Override
                        public void onError(VolleyError error) {
                            runOnUiThread(() -> {
                                String errorMessage = "Registration failed";
                                if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
                                    errorMessage = new String(error.networkResponse.data);
                                }
                                Toast.makeText(RegisterActivity.this,
                                        errorMessage,
                                        Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isValidUsername(String username) {
        return username.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,12}$");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,16}$");
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}