package iut.dam.sae_s04.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.Map;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.models.Admin;

public class ResumeFragment extends Fragment {

    private TextView textTotalDons, textTotalCount, textAssociation;
    private TableLayout tableParAnnee;
    private DatabaseHelper dbHelper;
    private TableLayout tableDonsRecurrents;
    private String adminAssociation;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);

        textTotalDons = view.findViewById(R.id.text_total_dons);
        textTotalCount = view.findViewById(R.id.text_total_count);
        textAssociation = view.findViewById(R.id.text_nom_association);
        tableParAnnee = view.findViewById(R.id.table_par_annee);
        tableDonsRecurrents = view.findViewById(R.id.table_recurrents);

        dbHelper = new DatabaseHelper(getContext());

        Admin admin = MainActivity.getInstance().getCurrentAdmin();
        if (admin != null) {
            adminAssociation = admin.getAssociation();
            textAssociation.setText("Association : " + adminAssociation);
        } else {
            adminAssociation = "Inconnue";
            textAssociation.setText("Association non trouvée");
        }

        afficherDonsParAssociation();
        afficherDonsRecurrents();

        return view;
    }

    private void afficherDonsParAssociation() {
        Cursor cursor = dbHelper.getDonsByAssociation(adminAssociation);

        double total = 0;
        int count = 0;

        // Maps pour stocker les données par année
        Map<String, Integer> donsParAnnee = new HashMap<>();
        Map<String, Double> montantParAnnee = new HashMap<>();

        tableParAnnee.removeViews(1, tableParAnnee.getChildCount() - 1);

        while (cursor.moveToNext()) {
            double montant = cursor.getDouble(cursor.getColumnIndexOrThrow("montant"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String year = date.substring(0, 4);

            donsParAnnee.put(year, donsParAnnee.getOrDefault(year, 0) + 1);
            montantParAnnee.put(year, montantParAnnee.getOrDefault(year, 0.0) + montant);

            total += montant;
            count++;
        }

        cursor.close();

        // On compte le total
        textTotalDons.setText(String.format("%.2f €", total));
        textTotalCount.setText(String.valueOf(count));

        // On ajoute les lignes au tableau
        for (String year : donsParAnnee.keySet()) {
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            // Année
            TextView cellYear = new TextView(getContext());
            cellYear.setText(year);
            cellYear.setLayoutParams(cellParams);
            cellYear.setPadding(8, 8, 8, 8);
            cellYear.setGravity(Gravity.CENTER);

            // Nombre de dons
            TextView cellCount = new TextView(getContext());
            cellCount.setText(String.valueOf(donsParAnnee.get(year)));
            cellCount.setLayoutParams(cellParams);
            cellCount.setPadding(8, 8, 8, 8);
            cellCount.setGravity(Gravity.CENTER);

            // Montant total
            TextView cellTotal = new TextView(getContext());
            cellTotal.setText(String.format("%.2f €", montantParAnnee.get(year)));
            cellTotal.setLayoutParams(cellParams);
            cellTotal.setPadding(8, 8, 8, 8);
            cellTotal.setGravity(Gravity.CENTER);

            row.addView(cellYear);
            row.addView(cellCount);
            row.addView(cellTotal);

            tableParAnnee.addView(row);
        }
    }

    private void afficherDonsRecurrents() {
        Cursor cursor = dbHelper.getDonsRecurrentsByAssociation(adminAssociation);

        Map<String, Integer> countByFreq = new HashMap<>();
        Map<String, Double> totalByFreq = new HashMap<>();

        tableDonsRecurrents.removeViews(1, tableDonsRecurrents.getChildCount() - 1); // Garde l'en-tête

        while (cursor.moveToNext()) {
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type")); // ex: recurrent_mensuel
            String freq = type.replace("recurrent_", "");
            double montant = cursor.getDouble(cursor.getColumnIndexOrThrow("montant"));

            countByFreq.put(freq, countByFreq.getOrDefault(freq, 0) + 1);
            totalByFreq.put(freq, totalByFreq.getOrDefault(freq, 0.0) + montant);
        }

        cursor.close();

        for (String freq : countByFreq.keySet()) {
            TableRow row = new TableRow(getContext());
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);

            TextView tfreq = new TextView(getContext());
            tfreq.setText(freq);
            tfreq.setGravity(Gravity.CENTER);
            tfreq.setLayoutParams(params);
            tfreq.setPadding(8, 8, 8, 8);

            TextView tcount = new TextView(getContext());
            tcount.setText(String.valueOf(countByFreq.get(freq)));
            tcount.setGravity(Gravity.CENTER);
            tcount.setLayoutParams(params);
            tcount.setPadding(8, 8, 8, 8);

            TextView tmontant = new TextView(getContext());
            tmontant.setText(String.format("%.2f €", totalByFreq.get(freq)));
            tmontant.setGravity(Gravity.CENTER);
            tmontant.setLayoutParams(params);
            tmontant.setPadding(8, 8, 8, 8);

            row.addView(tfreq);
            row.addView(tcount);
            row.addView(tmontant);

            tableDonsRecurrents.addView(row);
        }
    }
}
