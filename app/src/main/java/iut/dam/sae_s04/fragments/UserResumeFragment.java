package iut.dam.sae_s04.fragments;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.models.User;
import iut.dam.sae_s04.utils.SessionManager;

public class UserResumeFragment extends Fragment {

    private TextView totalDonsText;
    private ListView listDons;
    private DatabaseHelper dbHelper;

    public UserResumeFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_resume, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        totalDonsText = rootView.findViewById(R.id.total_dons_text);
        listDons = rootView.findViewById(R.id.list_dons_user);
        dbHelper = new DatabaseHelper(getContext());

        User currentUser = SessionManager.getCurrentUser(getContext(), dbHelper);
        if (currentUser != null) {
            loadDonsData(currentUser.getId());
        } else {
            totalDonsText.setText("Utilisateur non connecté");
        }

        return rootView;
    }

    private void loadDonsData(int userId) {
        Cursor cursor = dbHelper.getAllDonsByUserId(userId);
        List<HashMap<String, String>> data = new ArrayList<>();
        double total = 0;

        if (cursor.moveToFirst()) {
            do {
                String association = cursor.getString(cursor.getColumnIndexOrThrow("association"));
                double montant = cursor.getDouble(cursor.getColumnIndexOrThrow("montant"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                total += montant;

                HashMap<String, String> map = new HashMap<>();
                map.put("line1", association);
//                map.put("line2", "Montant : " + montant + "€ | Date : " + date);
                map.put("line2", "Montant : " + montant + "€");
                data.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();

        totalDonsText.setText("Total des dons : " + total + " €");

        SimpleAdapter adapter = new SimpleAdapter(
                getContext(),
                data,
                android.R.layout.simple_list_item_2,
                new String[]{"line1", "line2"},
                new int[]{android.R.id.text1, android.R.id.text2}
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                Typeface customFont = getResources().getFont(R.font.poppinsmedium);

                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                text1.setTypeface(customFont);
                text2.setTypeface(customFont);

                return view;
            }
        };

        listDons.setAdapter(adapter);
    }
}
