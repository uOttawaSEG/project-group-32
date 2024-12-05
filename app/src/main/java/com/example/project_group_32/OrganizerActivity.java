package com.example.project_group_32;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OrganizerActivity extends UserActivity {

    private DatabaseHelper dbHelper;

    private ArrayAdapter<String> upcomingAdapter;
    private ArrayAdapter<String> pastAdapter;
    private ArrayList<Event> upComingEvents;
    private ArrayList<Event> pastEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer);

        Button btSignOff = findViewById(R.id.signoff);
        btSignOff.setOnClickListener(v -> signOff());

        TextView tvMessage = findViewById(R.id.message);
        User user = (User) getIntent().getSerializableExtra("user");
        tvMessage.setText(welcome(user));

        Button btCreateEvent = findViewById(R.id.create_event);
        btCreateEvent.setOnClickListener(v -> createEvent());

        dbHelper = new DatabaseHelper(this);
        ListView lvUpcoming = findViewById(R.id.upcoming_events);
        ListView lvPast = findViewById(R.id.past_events);

        lvUpcoming.setOnItemLongClickListener((parent, view, position, id) -> {
            processEvents(upComingEvents.get(position));
            return true;
        });
        lvPast.setOnItemLongClickListener(((parent, view, position, id) -> {
            processEvents(pastEvents.get(position));
            return true;
        }));

        upComingEvents = new ArrayList<>();
        pastEvents = new ArrayList<>();

        upcomingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        pastAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvUpcoming.setAdapter(upcomingAdapter);
        lvPast.setAdapter(pastAdapter);
        updateEventsList();
    }

    private void processEvents(Event event) {
        new AlertDialog.Builder(this)
                .setTitle("Event Management")
                .setNegativeButton("Delete Event", (dialog, which) -> {

                    boolean hasRegistration = false;
                    for (EventRequest request : dbHelper.getEventRequests()) {
                        if (request.getEventId() == event.getId()) {
                            hasRegistration = true;
                            break;
                        }
                    }

                    if (hasRegistration) {
                        Toast.makeText(this, "You cannot delete event that has registration!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dbHelper.deleteEvent(event);
                    updateEventsList();
                })
                .setPositiveButton("Attendee Management", (dialog, which) -> {
                    Intent intent = new Intent(this, AttendeeManagementActivity.class);
                    intent.putExtra("event", event);
                    startActivity(intent);
                })
                .show();
    }

    public static Date convertDate(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm", Locale.CANADA);
        Date date;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
    private void updateEventsList() {
        ArrayList<Event> events = dbHelper.getEvents();
        upcomingAdapter.clear();
        pastAdapter.clear();
        for (Event event : events) {
            Date endDate = convertDate(event.getDate() + " " + event.getEndTime());
            assert endDate != null;
            if (endDate.before(new Date())) {
                pastAdapter.add(event.toString());
                pastEvents.add(event);
            } else {
                upcomingAdapter.add(event.toString());
                upComingEvents.add(event);
            }
        }
        upcomingAdapter.notifyDataSetChanged();
        pastAdapter.notifyDataSetChanged();
    }

    private void createEvent() {
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
        updateEventsList();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        updateEventsList();
    }
}