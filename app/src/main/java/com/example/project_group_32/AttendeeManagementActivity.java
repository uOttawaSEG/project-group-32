package com.example.project_group_32;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AttendeeManagementActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_management);

        Button btApproveAll = findViewById(R.id.approve_all);
        btApproveAll.setOnClickListener(v -> approveAll());
        ListView lvAttendees = findViewById(R.id.attendees);

        dbHelper = new DatabaseHelper(this);

        Event event = (Event) getIntent().getSerializableExtra("event");
        lvAttendees.setOnItemLongClickListener((parent, view, position, id) -> {
            processAttendee(position);
            return true;
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvAttendees.setAdapter(adapter);
        updateAttendeeList();
    }

    private void updateAttendeeList() {
        ArrayList<Request> attendees = dbHelper.getRequests();
        adapter.clear();
        for (Request request : attendees) {
            if (request.getRole() != User.Role.Attendee) continue;
            adapter.add(request.toString());
        }
        adapter.notifyDataSetChanged();
    }

    private void processAttendee(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Attendee Management")
                .setNegativeButton("Reject", (dialog, which) -> {
                })
                .setPositiveButton("Approve", (dialog, which) -> {
                })
                .show();
    }

    private void approveAll() {
    }
}