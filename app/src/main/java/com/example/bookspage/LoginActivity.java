package com.example.bookspage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private TextView usernameErrorText;
    private TextView passwordErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Ensure you have activity_login.xml

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        usernameErrorText = findViewById(R.id.username_error_text);
        passwordErrorText = findViewById(R.id.password_error_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });
    }

    private void validateAndLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isUsernameValid = false;
        boolean isPasswordValid = false;

        // Validate Username
        if (username.length() >= 4 && username.matches("^[a-zA-Z0-9]+$")) {
            usernameErrorText.setVisibility(View.GONE);
            isUsernameValid = true;
        } else {
            if (username.length() < 4) {
                usernameErrorText.setText(getString(R.string.username_too_short));
            } else {
                usernameErrorText.setText("Username must contain only letters and numbers");
            }
            usernameErrorText.setVisibility(View.VISIBLE);
        }

        // Validate Password
        if (password.length() >= 4 && password.matches("^[a-zA-Z0-9]+$")) {
            passwordErrorText.setVisibility(View.GONE);
            isPasswordValid = true;
        } else {
            if (password.length() < 4) {
                passwordErrorText.setText(getString(R.string.password_too_short));
            } else {
                passwordErrorText.setText("Password must contain only letters and numbers");
            }
            passwordErrorText.setVisibility(View.VISIBLE);
        }

        // If both are valid, proceed to the home page
        if (isUsernameValid && isPasswordValid) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("USERNAME_EXTRA", username);
            startActivity(intent);
            finish(); // Finish LoginActivity so the user cannot go back to it from HomeActivity
        }
    }
}