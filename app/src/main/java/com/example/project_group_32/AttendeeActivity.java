package com.example.project_group_32;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class AttendeeActivity extends UserActivity {

    private DatabaseHelper dbHelper;

    private ArrayAdapter<String> eventsAdapter;
    private ArrayList<EventRequest> result_events;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee);

        currentUser = (User) getIntent().getSerializableExtra("user");

        Button btSignOff = findViewById(R.id.signoff);
        btSignOff.setOnClickListener(v -> signOff());

        TextView tvMessage = findViewById(R.id.message);
        User user = (User) getIntent().getSerializableExtra("user");
        tvMessage.setText(welcome(user));

        Button btFindEvent = findViewById(R.id.find_event);
        btFindEvent.setOnClickListener(v -> findEvent());

        dbHelper = new DatabaseHelper(this);
        ListView lvEvents = findViewById(R.id.events);

        lvEvents.setOnItemLongClickListener(((parent, view, position, id) -> {
            processEvents(result_events.get(position));
            return true;
        }));

        result_events = new ArrayList<>();

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvEvents.setAdapter(eventsAdapter);
        updateEventsList();

        for (EventRequest request : result_events) {
            Event event = dbHelper.getEvent(request.getEventId());
            Date date = OrganizerActivity.convertDate(event.getDate() + " " + event.getStartTime());
            Instant now = Instant.now();
            if (date.toInstant().isAfter(now.minus(24, ChronoUnit.HOURS))) {
                Toast.makeText(this, "You have event starts within 24 hours!", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private void processEvents(EventRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("Event Request Cancel")
                .setNegativeButton("Cancel Request", (dialog, which) -> {
                    if (request.getStatus() == Request.Status.Approved) {
                        Toast.makeText(this, "You cannot cancel approved request!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Event event = dbHelper.getEvent(request.getEventId());
                    Date date = OrganizerActivity.convertDate(event.getDate() + " " + event.getStartTime());
                    Instant now = Instant.now();
                    if (date.toInstant().isAfter(now.minus(24, ChronoUnit.HOURS))) {
                        Toast.makeText(this, "You cannot cancel request starts within 24 hours!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dbHelper.deleteEventRequest(request);
                    updateEventsList();
                })
                .show();
    }

    private void updateEventsList() {
        ArrayList<EventRequest> events = dbHelper.getEventRequests();
        eventsAdapter.clear();
        result_events.clear();

        for (EventRequest request : events) {
            if (request.getUserId() == currentUser.getId()) {
                Event event = dbHelper.getEvent(request.getEventId());
                result_events.add(request);
            }
        }
        Collections.reverse(result_events);
        for (EventRequest request : result_events) {
            Event event = dbHelper.getEvent(request.getEventId());
            eventsAdapter.add(request.getStatus() + "|\t" + event);
        }

        eventsAdapter.notifyDataSetChanged();
    }

    private void findEvent() {
        Intent intent = new Intent(this, FindEventActivity.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
        updateEventsList();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        updateEventsList();
    }
}