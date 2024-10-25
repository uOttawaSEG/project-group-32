package com.example.project_group_32;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDB";
    private static final String USER_TABLE = "users";
    private static final String USERINFO_TABLE = "userinfo";


    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String  KEY_USER_TYPE = "user_type";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ORGANIZATION = "organization";

    DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_USER_TYPE + " INTEGER NOT NULL) ";

        String CREATE_USERINFO_TABLE = "CREATE TABLE " + USERINFO_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_ORGANIZATION + " TEXT)";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USERINFO_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        values.put(KEY_USERNAME, "admin@esms.com");
        values.put(KEY_PASSWORD, "password");
        values.put(KEY_USER_TYPE, User.Type.Administrator.ordinal());

        db.insert(USER_TABLE, null, values);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public long hasUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE, new String[] {KEY_ID},
                KEY_USERNAME + " LIKE " + "'%" + username + "%'",
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long id =  Long.parseLong(cursor.getString(0));
            cursor.close();
            return id;
        }
        return -1;
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
                KEY_ID + " = " + "'%" + id + "%'",
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

    public void addAttendee(String username, String password, Userinfo userinfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues user_values = new ContentValues();
        user_values.put(KEY_USERNAME, username);
        user_values.put(KEY_PASSWORD, password);
        user_values.put(KEY_USER_TYPE, User.Type.Attendee.ordinal());

        long id = db.insert(USER_TABLE, null, user_values);
        addUserinfo(id, userinfo);
    }

    public void addOrganizer(String username, String password, Userinfo userinfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues user_values = new ContentValues();
        user_values.put(KEY_USERNAME, username);
        user_values.put(KEY_PASSWORD, password);
        user_values.put(KEY_USER_TYPE, User.Type.Organizer.ordinal());

        long id = db.insert(USER_TABLE, null, user_values);
        addUserinfo(id, userinfo);
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_USER_TYPE},
                KEY_USERNAME + " LIKE " + "'%" + username + "%'",
                null, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            long id = Long.parseLong(cursor.getString(0));
            String password =  cursor.getString(2);
            int type = Integer.parseInt(cursor.getString(3));

            if (type == User.Type.Administrator.ordinal()) {
                user = new Administrator(username, password);
            } else if (type == User.Type.Attendee.ordinal()) {
                Userinfo userinfo = getUserinfo(id);
                user = new Attendee(username, password, userinfo);
            } else if (type == User.Type.Organizer.ordinal()) {
                Userinfo userinfo = getUserinfo(id);
                user = new Organizer(username, password, userinfo);
            }

            cursor.close();
        }

        return user;
    }
}
