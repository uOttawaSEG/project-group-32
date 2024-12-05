package com.example.project_group_32;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDB";
    private static final String USER_TABLE = "users";
    private static final String USERINFO_TABLE = "userinfo";
    private static final String REQUEST_TABLE = "requests";
    private static final String EVENT_TABLE = "events";
    private static final String EVENT_REQUEST_TABLE = "event_request";


    private static final String KEY_ID = "id";
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String  KEY_ROLE = "user_type";
    private static final String KEY_STATUS = "status";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ORGANIZATION = "organization";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_MODE = "mode";
    private static final String KEY_USERS = "users";
    private static final String KEY_EVENTS = "events";

    DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_ROLE + " INTEGER NOT NULL,"
                + KEY_EVENTS + " TEXT)";

        String CREATE_USERINFO_TABLE = "CREATE TABLE " + USERINFO_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_ORGANIZATION + " TEXT)";

        String CREATE_REQUEST_TABLE = "CREATE TABLE " + REQUEST_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_STATUS + " INTEGER NOT NULL,"
                + KEY_ROLE + " INTEGER NOT NULL,"
                + KEY_USERNAME + " TEXT NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL)";

        String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_MODE + " INTEGER,"
                + KEY_USERS + " TEXT)";

        String CREATE_EVENT_REQUEST_TABLE = "CREATE TABLE " + EVENT_REQUEST_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EVENT_ID + " INTEGER,"
                + KEY_USER_ID + " INTEGER,"
                + KEY_STATUS + " INTEGER)";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USERINFO_TABLE);
        db.execSQL(CREATE_REQUEST_TABLE);
        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_EVENT_REQUEST_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        values.put(KEY_USERNAME, "admin@esms.com");
        values.put(KEY_PASSWORD, "password");
        values.put(KEY_ROLE, User.Role.Administrator.ordinal());

        db.insert(USER_TABLE, null, values);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }


    private void addUserinfo(long id, Userinfo userinfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(KEY_FIRST_NAME, userinfo.getFirstName());
        values.put(KEY_LAST_NAME, userinfo.getLastName());
        values.put(KEY_EMAIL, userinfo.getEmail());
        values.put(KEY_PHONE_NUMBER, userinfo.getPhoneNumber());
        values.put(KEY_ADDRESS, userinfo.getAddress());
        values.put(KEY_ORGANIZATION, userinfo.getOrganization());

        db.insert(USERINFO_TABLE, null, values);
    }

    public Userinfo getUserinfo(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USERINFO_TABLE,
                new String[] {KEY_ID, KEY_FIRST_NAME, KEY_LAST_NAME, KEY_EMAIL, KEY_PHONE_NUMBER, KEY_ADDRESS, KEY_ORGANIZATION},
                KEY_ID + " LIKE " + "'%" + id + "%'",
                null, null, null, null);

        Userinfo userinfo = null;
        if (cursor != null && cursor.moveToFirst()) {

            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String email = cursor.getString(3);
            String phoneNumber = cursor.getString(4);
            String address = cursor.getString(5);
            String organization = cursor.getString(6);
            userinfo = new Userinfo(firstName, lastName, email, phoneNumber, address, organization);

            cursor.close();
        }
        return userinfo;
    }

    public void addRequest(String username, String password, User.Role role, Userinfo userinfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues user_values = new ContentValues();
        user_values.put(KEY_STATUS, Request.Status.Processing.ordinal());
        user_values.put(KEY_ROLE, role.ordinal());
        user_values.put(KEY_USERNAME, username);
        user_values.put(KEY_PASSWORD, password);

        long id = db.insert(REQUEST_TABLE, null, user_values);
        addUserinfo(id, userinfo);
    }

    public void updateRequest(long id, Request.Status status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status.ordinal());
        db.update(REQUEST_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Request.Status getRequestStatus(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(REQUEST_TABLE,
                new String[] {KEY_STATUS},
                KEY_USERNAME + " LIKE " + "'%" + username + "%'",
                null, null, null, null);

        Request.Status status = null;

        if (cursor != null && cursor.moveToFirst()) {
            int ord = Integer.parseInt(cursor.getString(0));
            status = Request.Status.values()[ord];
            cursor.close();
        }

        return status;
    }

    public ArrayList<Request> getRequests() {
        ArrayList<Request> requests = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + REQUEST_TABLE;
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                long id = Long.parseLong(cursor.getString(0));
                int status_ord = Integer.parseInt(cursor.getString(1));
                int role_ord = Integer.parseInt(cursor.getString(2));
                Userinfo userinfo = getUserinfo(id);
                Request request = new Request(id, Request.Status.values()[status_ord], User.Role.values()[role_ord], userinfo);
                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return requests;
    }

    public void addUser(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(REQUEST_TABLE,
                new String[] {KEY_ROLE, KEY_USERNAME, KEY_PASSWORD},
                KEY_ID + " LIKE " + "'%" + id + "%'",
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int role_ord = Integer.parseInt(cursor.getString(0));
            String username = cursor.getString(1);
            String password = cursor.getString(2);

            ContentValues values = new ContentValues();
            values.put(KEY_ID, id);
            values.put(KEY_USERNAME, username);
            values.put(KEY_PASSWORD, password);
            values.put(KEY_ROLE, role_ord);
            values.put(KEY_EVENTS, "");

            db.insert(USER_TABLE, null, values);
            cursor.close();
        }
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {KEY_ID, KEY_PASSWORD, KEY_ROLE, KEY_EVENTS},
                KEY_USERNAME + " LIKE " + "'%" + username + "%'",
                null, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            long id = Long.parseLong(cursor.getString(0));
            String password =  cursor.getString(1);
            int ord = Integer.parseInt(cursor.getString(2));
            User.Role role = User.Role.values()[ord];

            String eventStr = cursor.getString(3);
            ArrayList<Long> events = new ArrayList<>();
            if (eventStr != null && !eventStr.isEmpty()) {
                for (String EventIdStr : eventStr.split(" ")) {
                    if (EventIdStr == null || EventIdStr.isEmpty()) continue;
                    long event_id = Long.parseLong(EventIdStr.trim());
                    events.add(event_id);
                }
            }

            user = new User(id, username, password, role, events);

            cursor.close();
        }

        return user;
    }

    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_ROLE, KEY_EVENTS},
                KEY_ID + " LIKE " + "'%" + id + "%'",
                null, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            long idStr = Long.parseLong(cursor.getString(0));
            String username =  cursor.getString(1);
            String password =  cursor.getString(2);
            int ord = Integer.parseInt(cursor.getString(3));
            User.Role role = User.Role.values()[ord];

            String eventStr = cursor.getString(4);
            ArrayList<Long> events = new ArrayList<>();
            if (eventStr != null && !eventStr.isEmpty()) {
                for (String EventIdStr : eventStr.split(" ")) {
                    if (EventIdStr == null || EventIdStr.isEmpty()) continue;
                    long event_id = Long.parseLong(EventIdStr.trim());
                    events.add(event_id);
                }
            }
            user = new User(id, username, password, role, events);

            cursor.close();
        }

        return user;
    }

    public Event getEvent(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(EVENT_TABLE,
                new String[] {KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_DATE, KEY_START_TIME, KEY_END_TIME, KEY_ADDRESS, KEY_MODE, KEY_USERS},
                KEY_ID + " LIKE " + "'%" + id + "%'",
                null, null, null, null);

        Event event = null;
        if (cursor != null && cursor.moveToFirst()) {

            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String date = cursor.getString(3);
            String startTime = cursor.getString(4);
            String endTime = cursor.getString(5);
            String address = cursor.getString(6);
            int mode_ord = Integer.parseInt(cursor.getString(7));

            String userStr = cursor.getString(8);
            ArrayList<Long> users = new ArrayList<>();

            if (userStr != null && !userStr.isEmpty()) {
                for (String userIdStr : userStr.split(" ")) {
                    if (userIdStr == null || userIdStr.isEmpty()) continue;
                    long userId = Long.parseLong(userIdStr.trim());
                    users.add(userId);
                }
            }

            event = new Event(id, title, description, date, startTime, endTime, address, Event.Mode.values()[mode_ord], users);

            cursor.close();
        }
        return event;
    }

    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, event.getTitle());
        values.put(KEY_DESCRIPTION, event.getDescription());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_START_TIME, event.getStartTime());
        values.put(KEY_END_TIME, event.getEndTime());
        values.put(KEY_ADDRESS, event.getAddress());
        values.put(KEY_MODE, event.getMode().ordinal());
        values.put(KEY_USERS, "");

        db.insert(EVENT_TABLE, null, values);
    }

    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public ArrayList<Event> getEvents() {
        ArrayList<Event> events = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_QUERY = "SELECT * FROM " + EVENT_TABLE;
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                long id = Long.parseLong(cursor.getString(0));
                String title = cursor.getString(1);
                String description = cursor.getString(2);
                String date = cursor.getString(3);
                String start_time = cursor.getString(4);
                String end_time = cursor.getString(5);
                String address = cursor.getString(6);
                int mode_ord = Integer.parseInt(cursor.getString(7));

                String userStr = cursor.getString(8);
                ArrayList<Long> users = new ArrayList<>();
                if (userStr != null && !userStr.isEmpty()) {
                    for (String idStr : userStr.split(" ")) {
                        if (idStr == null || idStr.isEmpty()) continue;
                        long user_id = Long.parseLong(idStr.trim());
                        users.add(user_id);
                    }
                }
                Event event = new Event(id, title, description, date, start_time, end_time, address, Event.Mode.values()[mode_ord], users);
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return events;
    }

    public void updateEventRequest(long id, Request.Status status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status.ordinal());
        db.update(EVENT_REQUEST_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteEventRequest(EventRequest request) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EVENT_REQUEST_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(request.getId())});
        db.close();
    }

    public ArrayList<EventRequest> getEventRequests() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<EventRequest> requests = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + EVENT_REQUEST_TABLE;
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                long id = Long.parseLong(cursor.getString(0));
                long event_id = Long.parseLong(cursor.getString(1));
                long user_id = Long.parseLong(cursor.getString(2));
                int ord = Integer.parseInt(cursor.getString(3));
                EventRequest eventRequest = new EventRequest(id, event_id, user_id, Request.Status.values()[ord]);
                requests.add(eventRequest);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return requests;
    }

    public void registerEvent(User user, Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_ID, event.getId());
        values.put(KEY_USER_ID, user.getId());
        values.put(KEY_STATUS, Request.Status.Processing.ordinal());

        db.insert(EVENT_REQUEST_TABLE, null, values);

//        addUserToEvent(user, event);
//        addEventToUser(user, event);
    }

    public void addUserToEvent(User user, Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(EVENT_TABLE,
                new String[] {KEY_USERS},
                KEY_ID + " LIKE " + "'%" + event.getId() + "%'",
                null, null, null, null);

        cursor.moveToFirst();
        String oldUsers = cursor.getString(0);
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(KEY_USERS, oldUsers + " " + user.getId());
        db.update(EVENT_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(event.getId())});
    }

    public void addEventToUser(User user, Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {KEY_EVENTS},
                KEY_ID + " LIKE " + "'%" + user.getId() + "%'",
                null, null, null, null);
        cursor.moveToFirst();
        String oldEvents = cursor.getString(0);
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENTS, oldEvents + " " + event.getId());
        db.update(USER_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }
}