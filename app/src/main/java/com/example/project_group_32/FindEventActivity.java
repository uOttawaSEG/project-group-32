package com.example.project_group_32;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class FindEventActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    private ArrayAdapter<String> eventsAdapter;
    private ArrayList<Event> result_events;
    private User currentUser;

    ArrayList<Event> currentEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentUser = (User) getIntent().getSerializableExtra("user");

        EditText etKeyword = findViewById(R.id.keyword);
        Button btSearch = findViewById(R.id.search);

        btSearch.setOnClickListener(v -> updateEventsList(etKeyword.getText().toString()));

        dbHelper = new DatabaseHelper(this);
        ListView lvEvents = findViewById(R.id.events);

        lvEvents.setOnItemLongClickListener(((parent, view, position, id) -> {
            processEvents(result_events.get(position), etKeyword.getText().toString());
            return true;
        }));


        result_events = new ArrayList<>();

        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvEvents.setAdapter(eventsAdapter);
        updateEventsList(etKeyword.getText().toString());
    }

    private void processEvents(Event event, String keyword) {
        new AlertDialog.Builder(this)
                .setTitle("Event Register")
                .setNegativeButton("Register Event", (dialog, which) -> {
                    currentEvents = new ArrayList<>();
                    for (EventRequest request : dbHelper.getEventRequests()) {
                        if (request.getUserId() == currentUser.getId()) {
                            currentEvents.add(dbHelper.getEvent(request.getEventId()));
                        }
                    }

                    Date startDate = OrganizerActivity.convertDate(event.getDate() + " " + event.getStartTime());
                    Date endDate = OrganizerActivity.convertDate(event.getDate() + " " + event.getEndTime());

                    boolean conflict = false;
                    for (Event ev : currentEvents) {
                        Date evStartDate = OrganizerActivity.convertDate(ev.getDate() + " " + ev.getStartTime());
                        Date evEndDate = OrganizerActivity.convertDate(ev.getDate() + " " + ev.getEndTime());

                        Date max, min;
                        if (startDate.after(evStartDate)) {
                            max = startDate;
                        } else {
                            max = evStartDate;
                        }
                        if (endDate.before(evEndDate)) {
                            min = endDate;
                        } else {
                            min = evEndDate;
                        }
                        if (max.before(min)) {
                            conflict = true;
                            break;
                        }
                    }
                    if (conflict) {
                        Toast.makeText(this, "Time conflict!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dbHelper.registerEvent(currentUser, event);
                    Toast.makeText(this, "Register successful!", Toast.LENGTH_SHORT).show();

                    updateEventsList(keyword);
                })
                .show();
    }



    private void updateEventsList(String keyword) {
        ArrayList<Event> events = dbHelper.getEvents();
        ArrayList<EventRequest> requests = dbHelper.getEventRequests();
        eventsAdapter.clear();
        result_events.clear();

        for (Event event : events) {
            Date endDate = OrganizerActivity.convertDate(event.getDate() + " " + event.getEndTime());
            assert endDate != null;
            boolean contains = false;
            for (EventRequest request : requests) {
                if (request.getEventId() == event.getId() && request.getUserId() == currentUser.getId()) {
                    contains = true;
                    break;
                }
            }
            if (contains) continue;
            if (event.getTitle().contains(keyword) || event.getDescription().contains(keyword)) {
                result_events.add(event);
                eventsAdapter.add(event.toString());
            }
        }
        eventsAdapter.notifyDataSetChanged();
    }
}