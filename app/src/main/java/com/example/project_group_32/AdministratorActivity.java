package com.example.project_group_32;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;

import java.util.ArrayList;

public class AdministratorActivity extends UserActivity {

    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrator);

        Button btSignOff = findViewById(R.id.signoff);
        btSignOff.setOnClickListener(v -> signOff());

        dbHelper = new DatabaseHelper(this);

        ListView lvRequests = findViewById(R.id.requests);
        requests = new ArrayList<>();

        TextView tvMessage = findViewById(R.id.message);
        User user = (User) getIntent().getSerializableExtra("user");
        tvMessage.setText(welcome(user));

        lvRequests.setOnItemLongClickListener((parent, view, position, id) -> {
            processRequest(position);
            return true;
        });
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvRequests.setAdapter(adapter);
        updateRequestList();
    }

    private void processRequest(int position) {
        long id = requests.get(position).getId();
        new AlertDialog.Builder(this)
                .setTitle("Reject or Approve")
                .setPositiveButton("Approve", (dialog, which) -> {
                    dbHelper.updateRequest(id, Request.Status.Approved);
                    dbHelper.addUser(id);
                    updateRequestList();
                })
                .setNegativeButton("Reject", (dialog, which) -> {
                    dbHelper.updateRequest(id, Request.Status.Rejected);
                    updateRequestList();
                }).show();
    }

    private void updateRequestList() {
        ArrayList<Request> reqs = dbHelper.getRequests();
        requests.clear();
        adapter.clear();
        for (Request request : reqs) {
            if (request.getStatus() == Request.Status.Approved) continue;
            adapter.add(request.toString());
            requests.add(request);
        }
        adapter.notifyDataSetChanged();
    }
}