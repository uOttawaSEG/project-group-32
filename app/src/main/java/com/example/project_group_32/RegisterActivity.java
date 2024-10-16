package com.example.project_group_32;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private RadioButton attendee, organizer;
    private EditText firstName, lastName, email, password, phoneNumber, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        attendee = findViewById(R.id.attendeeRB);
        organizer = findViewById(R.id.organizerRB);

        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phone_number);
        address = findViewById(R.id.address);


    }
}
