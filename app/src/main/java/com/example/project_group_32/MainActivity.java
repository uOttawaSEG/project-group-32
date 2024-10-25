package com.example.project_group_32;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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

        if (username.isEmpty()) {
            Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            Toast.makeText(this, "Username is not valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbHelper.getUser(username);
        if (user == null) {
            Toast.makeText(this, "User does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!user.verify(password)) {
            Toast.makeText(this, "Password does not correct!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}