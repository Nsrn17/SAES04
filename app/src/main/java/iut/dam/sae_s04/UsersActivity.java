package iut.dam.sae_s04;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import iut.dam.sae_s04.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity {
    private ListView usersListView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersListView = findViewById(R.id.usersListView);
        dbHelper = new DatabaseHelper(this);

        // Récupérer la liste des adhérents
        List<User> userList = dbHelper.getAllUsers();

        // Convertir en liste de String (juste pour l'affichage)
        List<String> userNames = new ArrayList<>();
        for (User user : userList) {
            userNames.add(user.getFullName() + " (" + user.getEmail() + ")");
        }

        // Adapter pour afficher la liste
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        usersListView.setAdapter(adapter);
    }

//    private void loadUsers() {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.getTableUsers(), null);
//
//        userList.clear();
//        if (cursor.moveToFirst()) {
//            do {
//                int emailIndex = cursor.getColumnIndex(DatabaseHelper.getColumnEmail());
//                int usernameIndex = cursor.getColumnIndex(DatabaseHelper.getColumnUsername());
//                int fullNameIndex = cursor.getColumnIndex(DatabaseHelper.getColumnName());
//
//                String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "N/A";
//                String username = (usernameIndex != -1) ? cursor.getString(usernameIndex) : "N/A";
//                String fullName = (fullNameIndex != -1) ? cursor.getString(fullNameIndex) : "N/A";
//
//                userList.add("Nom : " + fullName + "\nEmail : " + email + "\nUsername : " + username);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        adapter.notifyDataSetChanged(); // Met à jour la liste
//    }
}