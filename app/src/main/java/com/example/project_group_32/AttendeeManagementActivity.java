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
    private ArrayList<EventRequest> results;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_management);

        Button btApproveAll = findViewById(R.id.approve_all);
        btApproveAll.setOnClickListener(v -> approveAll());
        ListView lvAttendees = findViewById(R.id.attendees);

        dbHelper = new DatabaseHelper(this);

        event = (Event) getIntent().getSerializableExtra("event");
        lvAttendees.setOnItemLongClickListener((parent, view, position, id) -> {
            processAttendee(position);
            return true;
        });
        results = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvAttendees.setAdapter(adapter);
        updateRequestList();
    }

    private void updateRequestList() {
        results.clear();
        adapter.clear();
        for (EventRequest request : dbHelper.getEventRequests()) {
            if (request.getEventId() != event.getId()) continue;
            results.add(request);
            adapter.add(request.getStatus() + "|\t" + dbHelper.getUserinfo(request.getUserId()));
        }
        adapter.notifyDataSetChanged();
    }

    private void processAttendee(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Attendee Management")
                .setNegativeButton("Reject", (dialog, which) -> {
                    dbHelper.updateEventRequest(results.get(position).getId(), Request.Status.Rejected);
                    updateRequestList();
                })
                .setPositiveButton("Approve", (dialog, which) -> {
                    dbHelper.updateEventRequest(results.get(position).getId(), Request.Status.Approved);
                    updateRequestList();
                })
                .show();
    }

    private void approveAll() {

    }
}