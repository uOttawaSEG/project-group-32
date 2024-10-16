package com.example.project_group_32;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSignIn, btnRegister;

        btnSignIn = findViewById(R.id.signin);
        btnRegister = findViewById(R.id.register);

        btnSignIn.setOnClickListener(v -> signIn());
        btnRegister.setOnClickListener(v -> register());
    }

    private void signIn() {

        EditText etUsername, etUserPassword;

        etUsername = findViewById(R.id.username);
        etUserPassword = findViewById(R.id.user_password);

        String username = etUsername.getText().toString().trim();
        String password = etUserPassword.getText().toString().trim();

    }

    private void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}