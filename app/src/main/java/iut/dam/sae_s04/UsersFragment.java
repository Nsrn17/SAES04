package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import iut.dam.sae_s04.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    private ListView usersListView;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_users);
        View rootView = inflater.inflate(R.layout.activity_users, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);
        usersListView = rootView.findViewById(R.id.usersListView);
        dbHelper = new DatabaseHelper(getContext());

        // Récupérer la liste des adhérents
        List<User> userList = dbHelper.getAllUsers();

        // Convertir en liste de String (juste pour l'affichage)
        List<String> userNames = new ArrayList<>();
        for (User user : userList) {
            userNames.add(user.getFullName() + " (" + user.getEmail() + ")");
        }

        // Adapter pour afficher la liste
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userNames);
        usersListView.setAdapter(adapter);


        return rootView;
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