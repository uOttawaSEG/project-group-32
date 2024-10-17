package com.example.project_group_32;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

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

        User user = dbHelper.getUser(username);
        if (user == null) {
            Toast.makeText(this, "User does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!user.getPassword().equals(password)) {
            Toast.makeText(this, "Password does not correct!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        intent.putExtra("userInfo", user);
        startActivity(intent);
    }

    private void register() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}