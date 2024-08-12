package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SALT = "salt";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT NOT NULL, " +
            COLUMN_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_SALT + " TEXT NOT NULL, " +
            COLUMN_PHONE + " TEXT, " +
            COLUMN_EMAIL + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        insertTestUser(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    private void insertTestUser(SQLiteDatabase db) {
        byte[] salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword("Test123!", salt);
        String saltHex = bytesToHex(salt);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "testuser");
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_SALT, saltHex);
        values.put(COLUMN_PHONE, "1234567890");
        values.put(COLUMN_EMAIL, "testuser@example.com");
        db.insert(TABLE_USERS, null, values);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}