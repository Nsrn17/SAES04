package iut.dam.sae_s04;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ExplorerActivity extends BaseActivity {
    private EditText searchInput;
    private ListView searchResults;
    private List<String> dataList; // Liste complète des données
    private List<String> filteredList; // Liste temporaire après filtrage
    private CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        setupBottomNavigation();


        LinearLayout logosSante = findViewById(R.id.logos_sante);
        LinearLayout logosMentale = findViewById(R.id.logos_mentale);
        LinearLayout logosFamille = findViewById(R.id.logos_famille);
        //Barre de navigation

        searchInput = findViewById(R.id.search_input);
        searchResults = findViewById(R.id.search_results);

        List<Association> associations = AssociationData.getInstance().getAssociations();


        dataList = new ArrayList<>();
        for (Association association : associations) {
            dataList.add(association.getTitle());
        }

        filteredList = new ArrayList<>(dataList);
        adapter = new CustomAdapter(this, filteredList);
        searchResults.setAdapter(adapter);
        searchResults.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTitle = filteredList.get(position);
            Association selectedAssociation = null;
            for (Association association : AssociationData.getInstance().getAssociations()) {
                if (association.getTitle().equals(selectedTitle)) {
                    selectedAssociation = association;
                    break;
                }
            }
            if (selectedAssociation != null) {
                NavigationUtils.openDetailActivity(this, selectedAssociation);
            }
        });

        setupSearch();




        for (Association assoc : associations) {
            ImageView logo = new ImageView(this);
            logo.setImageResource(assoc.getLogoResId());


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
            params.setMargins(8, 0, 8, 0);
            logo.setLayoutParams(params);
            logo.setOnClickListener(v -> NavigationUtils.openDetailActivity(this, assoc));

            switch (assoc.getCat().toLowerCase()) {
                case "santé":
                    logosSante.addView(logo);
                    break;
                case "mentale":
                    logosMentale.addView(logo);
                    break;
                case "famille":
                    logosFamille.addView(logo);
                    break;
            }
        }
    }
    private void setupSearch() {

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void filter(String text) {
        filteredList.clear();

        if (text.isEmpty()) {
            filteredList.addAll(dataList);
        } else {
            for (String item : dataList) {
                if (item.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        Log.d("FILTER", "Résultats après filtrage : " + filteredList.toString()); // Debug


        adapter.notifyDataSetChanged();
        searchResults.setVisibility(filteredList.isEmpty() ? View.GONE : View.VISIBLE);



    }}

