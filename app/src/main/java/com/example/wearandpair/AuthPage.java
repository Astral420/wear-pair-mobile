package com.example.wearandpair;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog; // Import AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthPage extends AppCompatActivity {

    public Button btnLogin, btnForgotPassword, btnRegister;
    public TextInputEditText userInfo, passwordInfo;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(AuthPage.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        initializeViews();

        // --- NEW: Show the demo credentials dialog on startup ---
        showDemoCredentials();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin.setOnClickListener(v -> validateLogin());
    }


    private void showDemoCredentials() {
        StringBuilder details = new StringBuilder();
        try {
            // Get the user data from SessionManager
            JSONArray users = new JSONArray(sessionManager.getSampleLoginJson());
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                details.append("User " + (i + 1) + ":\n");
                details.append("  Username: ").append(user.getString("username")).append("\n");
                details.append("  Email: ").append(user.getString("email")).append("\n");
                details.append("  Password: ").append(user.getString("password")).append("\n\n");
            }
        } catch (JSONException e) {
            details.append("Could not load demo credentials.");
        }

        new AlertDialog.Builder(this)
                .setTitle("Demo Login Credentials")
                .setMessage(details.toString())
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    private void validateLogin() {
        String inputUser = userInfo.getText() != null ? userInfo.getText().toString().trim() : "";
        String inputPass = passwordInfo.getText() != null ? passwordInfo.getText().toString() : "";

        if (inputUser.isEmpty() || inputPass.isEmpty()) {
            Toast.makeText(this, "Please enter both username/email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // FIX: Get the user data from the single source of truth in SessionManager
            JSONArray users = new JSONArray(sessionManager.getSampleLoginJson());
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                String email = user.getString("email");
                String username = user.getString("username");
                String password = user.getString("password");

                boolean isCredentialsMatch = (inputUser.equalsIgnoreCase(username) || inputUser.equalsIgnoreCase(email)) && inputPass.equals(password);

                if (isCredentialsMatch) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(email);
                    Intent intent = new Intent(AuthPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
            Toast.makeText(this, "Invalid username/email or password", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, "Error validating login", Toast.LENGTH_SHORT).show();
        }
    }

    public void initializeViews() {
        btnLogin = findViewById(R.id.loginButton);
        btnForgotPassword = findViewById(R.id.forgotPasswordButton);
        btnRegister = findViewById(R.id.createAccountButton);
        userInfo = findViewById(R.id.userInfo);
        passwordInfo = findViewById(R.id.passwordInfo);
    }
}