package com.example.project_group_32;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {
    
    EditText etTitle, etDescription, etAddress;
    TextView tvDate, tvStartTime, tvEndTime;
    RadioButton rbManual, rbAuto;
    
    Calendar calendar;
    Date startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_event);

        calendar = Calendar.getInstance();

        etTitle = findViewById(R.id.title);
        etDescription = findViewById(R.id.description);
        tvDate = findViewById(R.id.date);
        tvStartTime = findViewById(R.id.start_time);
        tvEndTime = findViewById(R.id.end_time);
        etAddress = findViewById(R.id.address);

        rbManual = findViewById(R.id.manualRB);
        rbAuto = findViewById(R.id.autoRB);

        Button btCreate = findViewById(R.id.create_submit);
        btCreate.setOnClickListener(v -> create());
        
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickStartTime();
            }
        });

        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picEndTime();
            }
        });
    }

    private void picEndTime() {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                if (minute % 30 != 0) {
                    Toast.makeText(CreateEventActivity.this, "Invalid time," +
                            "times must be selected in 30-minute increments ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (calendar.before(Calendar.getInstance())) {
                    Toast.makeText(CreateEventActivity.this, "Invalid date", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CANADA);
                String time = sdf.format(calendar.getTime());
                endTime = calendar.getTime();
                tvEndTime.setText(time);
            }
        };
        new TimePickerDialog(CreateEventActivity.this, listener,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void pickStartTime() {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                if (minute % 30 != 0) {
                    Toast.makeText(CreateEventActivity.this, "Invalid time," +
                            "times must be selected in 30-minute increments ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (calendar.before(Calendar.getInstance())) {
                    Toast.makeText(CreateEventActivity.this, "Invalid date", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CANADA);
                String time = sdf.format(calendar.getTime());
                startTime = calendar.getTime();
                tvStartTime.setText(time);
            }
        };
        new TimePickerDialog(this, listener,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void pickDate() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                if (calendar.before(Calendar.getInstance())) {
                    Toast.makeText(CreateEventActivity.this, "Invalid date", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.CANADA);
                String date = sdf.format(calendar.getTime());
                tvDate.setText(date);
            }
        };
        new DatePickerDialog(this,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)

        ).show();
    }

    private void create() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = tvDate.getText().toString().trim();
        String start_time = tvStartTime.getText().toString().trim();
        String end_time = tvEndTime.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if (title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Choose a date!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (start_time.isEmpty()) {
            Toast.makeText(this, "Choose a start time!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (end_time.isEmpty()) {
            Toast.makeText(this, "Choose a end time!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startTime.after(endTime)) {
            Toast.makeText(this, "End time has to be after Start time!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (address.isEmpty()) {
            Toast.makeText(this, "Address cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Event.Mode mode;
        if (rbManual.isChecked()) {
            mode = Event.Mode.manual;
        } else if (rbAuto.isChecked()) {
            mode = Event.Mode.auto;
        } else {
            Toast.makeText(this, "You must select one of modes!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbHelper.addEvent(new Event(title, description, date, start_time, end_time, address, mode));

        Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();


        finish();
    }
}