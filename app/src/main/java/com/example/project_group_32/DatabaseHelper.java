package com.example.project_group_32;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.PushbackReader;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDB";
    private static final String USER_TABLE = "users";

    private static final String KEY_USERNAME = "username";
    private static final String  KEY_USER_TYPE = "user_type";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ORGANIZATION = "organization";

    DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB = "CREATE TABLE " + USER_TABLE + " ("
                + KEY_USERNAME + " TEXT PRIMARY KEY,"
                + KEY_USER_TYPE + " TEXT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_ORGANIZATION + " TEXT)";

        db.execSQL(CREATE_DB);

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, "admin@esms.com");
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
        values.put(KEY_FIRST_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastNmae());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_PHONE_NUMBER, user.getPhoneNumber());
        values.put(KEY_ADDRESS, user.getAddress());
        if (user instanceof Organizer) {
            values.put(KEY_USER_TYPE, "Organizer");
            values.put(KEY_ORGANIZATION, ((Organizer) user).getOrganization());
        } else if (user instanceof Attendee) {
            values.put(KEY_USER_TYPE, "Attendee");
        } else {
            values.put(KEY_USER_TYPE, "Administrator");
        }

        db.insert(USER_TABLE, null, values);
        db.close();
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE,
                new String[] {KEY_USERNAME, KEY_USER_TYPE, KEY_PASSWORD, KEY_FIRST_NAME, KEY_LAST_NAME, KEY_PHONE_NUMBER, KEY_ADDRESS, KEY_ORGANIZATION},
                KEY_USERNAME + " LIKE " + "'%" + username + "%'",
                null, null, null, null);

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {

            String usertype = cursor.getString(1);
            String password = cursor.getString(2);
            String firstName = cursor.getString(3);
            String lastName = cursor.getString(4);
            String phoneNumber = cursor.getString(5);
            String address = cursor.getString(6);
            String organization = cursor.getString(7);
            if (usertype.equals("Organizer")) {
                user = new Organizer(firstName, lastName, username, password, phoneNumber, address, organization);
            } else if (usertype.equals("Attendee")) {
                user = new Attendee(firstName, lastName, username, password, phoneNumber, address);
            } else {
                user = new Administrator(firstName, lastName, username, password, phoneNumber, address);
            }
            cursor.close();
        }

        return user;
    }
}
