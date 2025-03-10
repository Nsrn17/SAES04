package iut.dam.sae_s04;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccueilFragment extends Fragment {

    private ViewPager2 viewPager;
    private CarouselAdapter carouselAdapter;
    private EditText searchInput;
    private ListView searchResults;
    private List<String> dataList; // Liste complète des données
    private List<String> filteredList; // Liste temporaire après filtrage
    private CustomAdapter adapter;
    private int currentPage = 0;
    private static final int DELAY_MS = 2000;

    // Le constructeur par défaut
    public AccueilFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfle la vue du fragment
        View rootView = inflater.inflate(R.layout.activity_accueil, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Référence aux vues dans le fragment
       //Debut cariusel
        ViewPager2 viewPager = rootView.findViewById(R.id.viewPager);

        List<Integer> images = Arrays.asList(
                R.drawable.work,
                R.drawable.droitau,
                R.drawable.comit,
                R.drawable.uneloi
        );

        List<String> urls = Arrays.asList(
                "https://www.france-assos-sante.org/communique_presse/soigne-tes-droits-3-mois-pour-sensibiliser-les-usagers-de-la-sante-a-leurs-droits/",
                "https://www.france-assos-sante.org/communique_presse/droit-au-sejour-pour-soins-sa-suppression-par-les-deputes-mettrait-des-vies-en-danger/",
                "https://www.france-assos-sante.org/communique_presse/comite-interministeriel-metiers-interdits-les-associations-de-patients-claquent-la-porte/",
                "https://www.france-assos-sante.org/communique_presse/une-loi-trans-partisane-pour-interdire-la-promotion-de-lalcool-aupres-des-jeunes-france-assos-sante-signe/"
        );
        CarouselAdapter adapter = new CarouselAdapter(requireContext(), images, urls);
        viewPager.setAdapter(adapter);
        Handler handler = new Handler(Looper.getMainLooper());
       Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == images.size()) {
                    currentPage = 0; // Retour au début
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, DELAY_MS);
            }
        };

        // Lancer le défilement automatique
        handler.postDelayed(runnable, DELAY_MS);
        ///fincarousel
        searchInput = rootView.findViewById(R.id.search_input);
        searchResults = rootView.findViewById(R.id.search_results);

        // Liste d'associations à utiliser pour le filtrage
        List<Association> associations = AssociationData.getInstance().getAssociations();
        dataList = new ArrayList<>();
        for (Association association : associations) {
            dataList.add(association.getTitle());
        }

        filteredList = new ArrayList<>(dataList);


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
                NavigationUtils.openDetailFragment(getActivity(), selectedAssociation);
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
