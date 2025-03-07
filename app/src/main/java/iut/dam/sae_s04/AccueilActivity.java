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

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        setupBottomNavigation();
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        if (bottomNavigationView != null) {
//            bottomNavigationView.setSelectedItemId(getSelectedNavItem());
//        }
        viewPager = findViewById(R.id.viewPager);
        carouselAdapter = new CarouselAdapter();
        viewPager.setAdapter(carouselAdapter);
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