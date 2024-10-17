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


    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "\"username\"";
    private static final String  KEY_USER_TYPE = "user_type";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ORGANIZATION = "organization";

    ArrayList<User> users;

    DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB = "CREATE TABLE " + USER_TABLE + " ("
                + KEY_USERNAME + " TEXT PRIMARY KEY,"
                + KEY_USER_TYPE + " TEXT,"
                + KEY_PASSWORD + " TEXT)";

        db.execSQL(CREATE_DB);

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, "admin@eams.com");
        values.put(KEY_USER_TYPE, "Administrator");
        values.put(KEY_PASSWORD, "password");

        db.insert(USER_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        if (user instanceof Organizer) {
            values.put(KEY_USER_TYPE, "Organizer");
        } else {
            values.put(KEY_USER_TYPE, "Attendee");
        }

        db.insert(USER_TABLE, null, values);
        db.close();
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {KEY_USERNAME, KEY_USER_TYPE, KEY_PASSWORD},
                KEY_USERNAME + " LIKE " + "'%" + username + "%'",
                null, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {

            String password = cursor.getString(2);
            if (cursor.getString(1).equals("Organizer")) {
                user = new Organizer("firstName", "lastName", username, password, "phoneNumber", "address", "organization");
            } else {
                user = new Attendee("firstName", "lastName", username, password, "phoneNumber", "address");
            }
            cursor.close();
        }

        return user;
    }
}
