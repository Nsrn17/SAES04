package iut.dam.sae_s04.utils;

import android.content.Context;
import android.content.SharedPreferences;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.models.User;

public class SessionManager {
    private static final String PREF_NAME = "SessionPrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_TYPE = "user_type"; // "admin" ou "user"
    private static final String KEY_USER_ASSOCIATION = "user_association"; // pour admin
    private static final String KEY_ADMIN_NAME = "admin_name";
    private static final String KEY_ADMIN_USERNAME = "admin_username";

    public static void setCurrentUser(Context context, User user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, user.getEmail()); // ou getUsername()
        editor.putString(KEY_USER_TYPE, "user");
        editor.remove(KEY_USER_ASSOCIATION);
        editor.apply();
    }

    public static void setCurrentAdmin(Context context, Admin admin) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_ID, admin.getEmail());
        editor.putString(KEY_USER_TYPE, "admin");
        editor.putString(KEY_USER_ASSOCIATION, admin.getAssociation());
        editor.putString(KEY_ADMIN_NAME, admin.getName());
        editor.putString(KEY_ADMIN_USERNAME, admin.getUsername());
        editor.apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getCurrentUser(context, new DatabaseHelper(context)) != null ||
                getCurrentAdmin(context, new DatabaseHelper(context)) != null;
    }
    
    public static String getUserType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_TYPE, null);
    }

    public static String getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ID, null);
    }

    public static String getUserAssociation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USER_ASSOCIATION, null);
    }

    public static void clear(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static User getCurrentUser(Context context, DatabaseHelper dbHelper) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if ("user".equals(prefs.getString(KEY_USER_TYPE, null))) {
            String identifier = prefs.getString(KEY_USER_ID, null);
            if (identifier != null) {
                return dbHelper.getUserByIdentifier(identifier);
            }
        }
        return null;
    }

    public static Admin getCurrentAdmin(Context context, DatabaseHelper dbHelper) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if ("admin".equals(prefs.getString(KEY_USER_TYPE, null))) {
            String email = prefs.getString(KEY_USER_ID, null);
            String association = prefs.getString(KEY_USER_ASSOCIATION, null);
            String name = prefs.getString(KEY_ADMIN_NAME, ""); // valeur par défaut = vide
            String username = prefs.getString(KEY_ADMIN_USERNAME, "");

            if (email != null && association != null) {
                return new Admin(-1, name, email, username, association); // maintenant le nom & username sont bons
            }
        }
        return null;
    }

}
