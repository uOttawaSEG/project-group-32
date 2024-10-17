package com.example.project_group_32;

import android.app.Activity;
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

        User user = (User) getIntent().getSerializableExtra("userInfo");

        String message = "Welcome! You are logged in as ";
        if (user instanceof Administrator) {
            message += "Administrator";
        } else if (user instanceof Attendee) {
            message += "Attendee";
        } else {
            message += "Organizer";
        }

        tvMessage.setText(message);

        btSignOff.setOnClickListener(v -> signOff());
    }

    private void signOff() {
        finish();
    }
}
