package com.example.project_group_32;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView tvMessage = findViewById(R.id.message);
        Button btSignOff = findViewById(R.id.signoff);

        User user = (User) getIntent().getSerializableExtra("user");

        tvMessage.setText(welcome(user));
        btSignOff.setOnClickListener(v -> signOff());
    }

    public String welcome(User user) {
        String message = "Welcome! You are logged in as ";
        if (user instanceof Administrator) {
            message += "Administrator";
        } else if (user instanceof Attendee) {
            message += "Attendee";
        } else if (user instanceof Organizer) {
            message += "Organizer";
        }
        return message;
    }

    public void signOff() {
        finish();
    }
}