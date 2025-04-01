package iut.dam.sae_s04.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.mindrot.jbcrypt.BCrypt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Declaration des tables et leurs colonnes
    private static final String DATABASE_NAME = "appDatabase";
    public static final int DATABASE_VERSION = 3;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String TABLE_PAYMENT_INFO = "payment_info";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CARD_HOLDER = "card_holder";
    private static final String COLUMN_CARD_NUMBER = "card_number";
    private static final String COLUMN_EXP_MONTH = "exp_month";
    private static final String COLUMN_EXP_YEAR = "exp_year";
    private static final String COLUMN_CCV = "ccv";
    private static final String TABLE_DONS = "dons";
    private static final String COLUMN_DON_ID = "don_id";
    private static final String COLUMN_ASSOCIATION = "association";
    private static final String COLUMN_MONTANT = "montant";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_ANONYME = "anonyme";
    private static final String TABLE_ADMINS = "admins";
    private static final String COLUMN_ADMIN_ID = "id";
    private static final String COLUMN_ADMIN_EMAIL = "email";
    private static final String COLUMN_ADMIN_NAME = "name";
    private static final String COLUMN_ADMIN_USERNAME = "username";
    private static final String COLUMN_ADMIN_PASSWORD = "password";
    private static final String COLUMN_ADMIN_ASSOCIATION = "association_name";

    // Creation des tables SQL
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT);";

    private static final String CREATE_TABLE_PAYMENT_INFO =
            "CREATE TABLE " + TABLE_PAYMENT_INFO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_CARD_HOLDER + " TEXT, " +
                    COLUMN_CARD_NUMBER + " TEXT, " +
                    COLUMN_EXP_MONTH + " TEXT, " +
                    COLUMN_EXP_YEAR + " TEXT, " +
                    COLUMN_CCV + " TEXT);";

    private static final String CREATE_TABLE_DONS =
            "CREATE TABLE " + TABLE_DONS + " (" +
                    COLUMN_DON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_ASSOCIATION + " TEXT, " +
                    COLUMN_MONTANT + " REAL, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_ANONYME + " INTEGER);";

    private static final String CREATE_TABLE_ADMINS =
            "CREATE TABLE " + TABLE_ADMINS + " (" +
                    COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ADMIN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_ADMIN_NAME + " TEXT, " +
                    COLUMN_ADMIN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_ADMIN_PASSWORD + " TEXT, " +
                    COLUMN_ADMIN_ASSOCIATION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PAYMENT_INFO);
        db.execSQL(CREATE_TABLE_DONS);
        db.execSQL(CREATE_TABLE_ADMINS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
        Log.d("DB", "Toutes les tables supprimées pour mise à jour");
        onCreate(db);
    }

    public boolean registerUser(String email, String username, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            // On hache le mot de passe avant de l'inserer
            String hashedPassword = hashPassword(password);

            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_USERNAME, username);
            values.put(COLUMN_NAME, name);
            values.put(COLUMN_PASSWORD, hashedPassword);

            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;

        } catch (Exception e) {
            Log.e("DatabaseError", "Erreur lors de l'enregistrement de l'utilisateur", e);
            return false;

        } finally {
            db.close();
        }
    }

    private void insertAdmin(SQLiteDatabase db, String name, String email, String username, String plainPassword, String associationName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_NAME, name);
        values.put(COLUMN_ADMIN_EMAIL, email);
        values.put(COLUMN_ADMIN_USERNAME, username);
        values.put(COLUMN_ADMIN_PASSWORD, BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)));
        values.put(COLUMN_ADMIN_ASSOCIATION, associationName);
        long result = db.insert(TABLE_ADMINS, null, values);
        Log.d("DB", "Admin ajouté ? " + (result != -1));
    }

    public void resetAdmins() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ADMINS);
        Log.d("DB", "Tous les admins supprimés");

        // Réinsertion d'exemple
        insertAdmin(db, "Amina Karmenova", "aminakarmen@gmail.com", "aminak", "amina", "Association nationale de défense des malades, invalides et handicapés");
        insertAdmin(db, "Nesrine Hajjem", "nesrinehajjem@gmail.com", "nesrineh", "nesrine", "Association B");
        insertAdmin(db, "Alya Ayinde", "alyaayinde@gmail.com", "alyaa", "alya", "Association C");

        db.close();
    }


    // Methode pour hacher le mot de passe avec bcrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // On genere un sel avec un facteur de coût de 12
    }

    public boolean enregistrerDon(int userId, String association, double montant, String type, boolean anonyme) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (anonyme) {
            values.putNull("user_id");
        } else {
            values.put("user_id", userId);
        }

        values.put("association", association);
        values.put("montant", montant);
        values.put("type", type);
        values.put("anonyme", anonyme ? 1 : 0);
        values.put("date", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

        long result = db.insert("dons", null, values);
        return result != -1;
    }

    public boolean checkUser(String identifier, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE (" +
                        COLUMN_EMAIL + "=? OR " + COLUMN_USERNAME + "=?)",
                new String[]{identifier, identifier});

        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            Log.d("LOGIN", "Password en base (hashé) = " + storedHashedPassword);
            Log.d("LOGIN", "Password saisi = " + password);

            boolean passwordMatches = BCrypt.checkpw(password, storedHashedPassword);
            Log.d("LOGIN", "Password match ? " + passwordMatches);

            cursor.close();
            db.close();
            return passwordMatches;
        }

        Log.d("LOGIN", "Aucun utilisateur trouvé");
        cursor.close();
        db.close();
        return false;
    }

    public User getUserByIdentifier(String identifier) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? OR username = ?", new String[]{identifier, identifier});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            db.close();
            return new User(id, username, email, name);
        }
        cursor.close();
        db.close();
        return null;
    }

    public Cursor getAllDonsByAssociationWithUsers(String associationName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Jointure avec table users (LEFT JOIN pour garder aussi les anonymes et admins)
        String query = "SELECT d.*, u.username " +
                "FROM dons d " +
                "LEFT JOIN users u ON d.user_id = u.id " +
                "WHERE d.association = ?";

        return db.rawQuery(query, new String[]{associationName});
    }

    // Méthode : Vérifier un admin (retourne Admin si trouvé, sinon null)
    public Admin getAdminIfExists(String identifier, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMINS +
                        " WHERE (" + COLUMN_ADMIN_EMAIL + "=? OR " + COLUMN_ADMIN_USERNAME + "=?)",
                new String[]{identifier, identifier});

        if (cursor.moveToFirst()) {
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_PASSWORD));
            if (password.equals(storedPassword)) { // À remplacer par BCrypt si besoin
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_EMAIL));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_USERNAME));
                String association = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ASSOCIATION));
                cursor.close();
                db.close();
                return new Admin(id, name, email, username, association);
            }
        }
        cursor.close();
        db.close();
        return null;
    }

    // Méthode pour récupérer les dons par association (pour admin)
    public Cursor getDonsByAssociation(String associationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_DONS + " WHERE " + COLUMN_ASSOCIATION + "=?",
                new String[]{associationName});
    }

    public Cursor getDonsRecurrentsByAssociation(String associationName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM dons WHERE association = ? AND type LIKE 'recurrent_%'", new String[]{associationName});
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

    public boolean savePaymentInfo(int userId, String cardHolder, String cardNumber, String expMonth, String expYear, String ccv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CARD_HOLDER, cardHolder);
        values.put(COLUMN_CARD_NUMBER, cardNumber);
        values.put(COLUMN_EXP_MONTH, expMonth);
        values.put(COLUMN_EXP_YEAR, expYear);
        values.put(COLUMN_CCV, ccv);

        try {

            long result = db.insert(TABLE_PAYMENT_INFO, null, values);
            db.close();
            return result != -1;

        } catch (Exception e) {

            db.close();
            Log.e("DatabaseError", "Erreur lors de l'insertion des informations de paiement", e);
            return false;
        }
    }

    public Admin checkAdmin(String identifier, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMINS +
                        " WHERE (" + COLUMN_ADMIN_EMAIL + "=? OR " + COLUMN_ADMIN_USERNAME + "=?)",
                new String[]{identifier, identifier});

        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_PASSWORD));
            if (BCrypt.checkpw(password, storedHashedPassword)) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_EMAIL));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_USERNAME));
                String assoc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_ASSOCIATION));

                cursor.close();
                db.close();
                return new Admin(id, name, email, username, assoc);
            }
        }

        cursor.close();
        db.close();
        return null;
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
