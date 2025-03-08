package iut.dam.sae_s04;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends Fragment {

    private ViewPager2 viewPager;
    private CarouselAdapter carouselAdapter;
    private EditText searchInput;
    private ListView searchResults;
    private List<String> dataList; // Liste complète des données
    private List<String> filteredList; // Liste temporaire après filtrage
    private CustomAdapter adapter;

    // Le constructeur par défaut
    public AccueilActivity() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfle la vue du fragment
        View rootView = inflater.inflate(R.layout.activity_accueil, container, false);

        // Référence aux vues dans le fragment
        viewPager = rootView.findViewById(R.id.viewPager);
        carouselAdapter = new CarouselAdapter();
        viewPager.setAdapter(carouselAdapter);

        searchInput = rootView.findViewById(R.id.search_input);
        searchResults = rootView.findViewById(R.id.search_results);

        // Liste d'associations à utiliser pour le filtrage
        List<Association> associations = AssociationData.getInstance().getAssociations();
        dataList = new ArrayList<>();
        for (Association association : associations) {
            dataList.add(association.getTitle());
        }

        filteredList = new ArrayList<>(dataList);
        adapter = new CustomAdapter(getContext(), filteredList);
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
                NavigationUtils.openDetailActivity(getActivity(), selectedAssociation);
            }
        });

        setupSearch();

        return rootView;  // Retourne la vue gonflée
    }

    // Configuration de la recherche
    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    // Méthode de filtrage des résultats de recherche
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
    }
}
