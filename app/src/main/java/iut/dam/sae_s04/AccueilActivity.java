package iut.dam.sae_s04;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.text.TextWatcher;

import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends  BaseActivity {

    private ViewPager2 viewPager;
    private CarouselAdapter carouselAdapter;
    private EditText searchInput;
    private ListView searchResults;
    private List<String> dataList; // Liste complète des données
    private List<String> filteredList; // Liste temporaire après filtrage
    private CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Configurer le ViewPager2
        viewPager = findViewById(R.id.viewPager);
        carouselAdapter = new CarouselAdapter();
        viewPager.setAdapter(carouselAdapter);


        searchInput = findViewById(R.id.search_input);
        searchResults = findViewById(R.id.search_results);

        // Initialiser la liste des données
        dataList = new ArrayList<>();
        dataList.add("Association A");
        dataList.add("Association B");
        dataList.add("Association C");
        dataList.add("Club de sport");
        dataList.add("Club de musique");

        // Initialiser la liste filtrée avec tous les éléments au début
        filteredList = new ArrayList<>(dataList);

        // Initialiser l'adaptateur avec la liste filtrée
        adapter = new CustomAdapter(this, filteredList);
        searchResults.setAdapter(adapter);

        setupSearch();
         //Barre de navigation
        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
       // Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);


        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
       // navigateToActivity(addRingButton, AddRingActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
    }
    private void setupSearch() {
        // Écouter les changements de texte dans l'EditText
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filtrer la liste en fonction du texte saisi
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void filter(String text) {
        filteredList.clear(); // Effacer la liste temporaire

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

        // Notifier l'adaptateur du changement de données
        adapter.notifyDataSetChanged();

        // Afficher ou masquer la ListView selon les résultats
        searchResults.setVisibility(filteredList.isEmpty() ? View.GONE : View.VISIBLE);



    }}