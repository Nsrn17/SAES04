package iut.dam.sae_s04.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import iut.dam.sae_s04.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "adherents.db";
    public static final int DATABASE_VERSION = 1; // Augmente la version


    // Table des utilisateurs
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Requête de création de table
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Inscription
    public boolean registerUser(String email, String username, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1; // Retourne true si l'insertion a réussi
    }


    // Connexion
    public boolean checkUser(String identifier, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE (" +
                        COLUMN_EMAIL + "=? OR " + COLUMN_USERNAME + "=?) AND " + COLUMN_PASSWORD + "=?",
                new String[]{identifier, identifier, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    // Récupérer tous les adhérents
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));

                userList.add(new User(id, username, email, fullName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    public static String getColumnEmail() {
        return COLUMN_EMAIL;
    }

    public static String getTableUsers() {
        return TABLE_USERS;
    }

    public static String getColumnPassword() {
        return COLUMN_PASSWORD;
    }

    public static String getColumnUsername() {
        return COLUMN_USERNAME;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }


}
