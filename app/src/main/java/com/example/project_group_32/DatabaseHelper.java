package com.example.project_group_32;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserDB";
    private static final String TABLE_USER = "users";

    private static final String KEY_EMAIL = "email";
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
        String CREATE_DB = "CREATE TABLE " + TABLE_USER + "("
                + KEY_EMAIL + "TEXT PRIMARY KEY,"
                + KEY_USER_TYPE + " TEXT,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE_NUMBER + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_ORGANIZATION + " TEXT)";

        db.execSQL(CREATE_DB);

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, "admin@eams.com");
        values.put(KEY_PASSWORD, "password");
        values.put(KEY_USER_TYPE, "administrator");

        db.insert(TABLE_USER, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_FIRST_NAME, user.getFirstName());
        values.put(KEY_LAST_NAME, user.getLastNmae());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_PHONE_NUMBER, user.getPhoneNumber());
        values.put(KEY_ADDRESS, user.getAddress());
        if (user instanceof Organizer) {
            values.put(KEY_USER_TYPE, "organizer");
            values.put(KEY_ORGANIZATION, ((Organizer) user).getOrganization());
        } else {
            values.put(KEY_USER_TYPE, "attendee");
        }

        db.insert(TABLE_USER, null, values);
        db.close();
    }
}
