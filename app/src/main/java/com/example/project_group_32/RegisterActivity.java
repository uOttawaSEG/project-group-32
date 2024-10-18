package com.example.project_group_32;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        btRegister = findViewById(R.id.register_submit);

        btRegister.setOnClickListener(v -> register());
    }

    private void register() {

        RadioButton rbAttendee, rbOrganizer;
        EditText etFirstName, etLastName, etEmail, etPassword, etPhoneNumber, etAddress, etOrganization;

        rbAttendee = findViewById(R.id.attendeeRB);
        rbOrganizer = findViewById(R.id.organizerRB);

        etFirstName = findViewById(R.id.firstname);
        etLastName = findViewById(R.id.lastname);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        etPhoneNumber = findViewById(R.id.phone_number);
        etAddress = findViewById(R.id.address);
        etOrganization = findViewById(R.id.organization);

        boolean isAttendee = rbAttendee.isActivated();
        boolean isOrganizer = rbOrganizer.isActivated();

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String organization = etOrganization.getText().toString().trim();

        if (firstName.isEmpty()) {
            Toast.makeText(this, "First name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastName.isEmpty()) {
            Toast.makeText(this, "Last name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email is not valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone number cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Pattern.matches("^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}$", lastName)){
            Toast.makeText(this, "Last name is not valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Address cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOrganizer && organization.isEmpty()) {
            Toast.makeText(this, "Organization cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isAttendee) {
            Attendee attendee = new Attendee(firstName, lastName, email, password, phoneNumber, address);
            dbHelper.addUser(attendee);
        } else if (isOrganizer) {
            Organizer organizer = new Organizer(firstName, lastName, email, password, phoneNumber, address, organization);
            dbHelper.addUser(organizer);
        } else {
            Toast.makeText(this, "You must select one of roles!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "User register successfully!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
