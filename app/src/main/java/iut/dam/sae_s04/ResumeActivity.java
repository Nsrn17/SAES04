package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeActivity extends  BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resume);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner spinner = findViewById(R.id.spinner_association);

// Charger les éléments depuis le fichier XML
        String[] originalItems = getResources().getStringArray(R.array.associations);
        List<String> filteredItems = new ArrayList<>(Arrays.asList(originalItems));

// Créer l'adaptateur personnalisé
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, filteredItems);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

// Bloquer la sélection de "Sélectionnez une association"
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK); // Griser l'option
//                } else {
//                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); // Texte blanc pour les autres options
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        setupBottomNavigation();
    }
}