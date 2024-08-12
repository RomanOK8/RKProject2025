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
    private EditText usernameEditText, passwordEditText, phoneEditText, emailEditText;
    private Button registerButton;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiClient = new ApiClient(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        phoneEditText = findViewById(R.id.phone);
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
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!isValidUsername(username)) {
                    Toast.makeText(RegisterActivity.this, "Username must be 6-12 characters long and contain letters and digits", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "Password must be 6-16 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character", Toast.LENGTH_SHORT).show();
                } else {
                    byte[] salt = PasswordHasher.generateSalt();
                    String hashedPassword = PasswordHasher.hashPassword(password, salt);
                    String saltHex = bytesToHex(salt);

                    apiClient.registerUser(username, hashedPassword, saltHex, phone, email, new ApiClient.ApiCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if (error != null && error.networkResponse != null) {
                                String errorMessage = new String(error.networkResponse.data);
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
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

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}