package iut.dam.sae_s04.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Arrays;
import java.util.List;
import iut.dam.sae_s04.models.Association;
import iut.dam.sae_s04.models.AssociationData;
import iut.dam.sae_s04.adapters.CarouselAdapter;
import iut.dam.sae_s04.adapters.CustomAdapter;
import iut.dam.sae_s04.utils.NavigationUtils;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;

public class AccueilFragment extends Fragment {

    private ViewPager2 viewPager;
    private CarouselAdapter carouselAdapter;
    private EditText searchInput;
    private ListView searchResults;
    private List<String> dataList; // Liste complète des données
    private List<String> filteredList; // Liste temporaire après filtrage
    private CustomAdapter adapter;
    private CarouselAdapter cadapter;
    private int currentPage = 0;
    private static final int DELAY_MS = 2000;

    public AccueilFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfle la vue du fragment
        View rootView = inflater.inflate(R.layout.activity_accueil, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

       //Debut carrousel
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
       cadapter = new CarouselAdapter(requireContext(), images, urls);
        viewPager.setAdapter(cadapter);
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

        rootView.findViewById(R.id.btn_partner).setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ExplorerFragment())
                    .addToBackStack(null)
                    .commit();
        });

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
        adapter = new CustomAdapter(getContext(), filteredList);
        searchResults.setAdapter(adapter);
// Quand un élément est sélectionné dans la recherche
        searchResults.setOnItemClickListener((parent, view, position, id) -> {
            String selectedTitle = filteredList.get(position);
            Association selectedAssociation = null;

            // Recherche de l'association correspondante
            for (Association association : AssociationData.getInstance().getAssociations()) {
                if (association.getTitle().equals(selectedTitle)) {
                    selectedAssociation = association;
                    break;
                }
            }

            // Si l'association est trouvée, on passe à l'étape suivante
            if (selectedAssociation != null) {
                // On passe la liste d'associations et la position à la méthode openDetailFragment
                ArrayList<Association> associationsList = new ArrayList<>(AssociationData.getInstance().getAssociations());
                NavigationUtils.openDetailFragment(requireActivity(), selectedAssociation, associationsList, position);
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
            // Réinitialiser la liste complète
            filteredList.addAll(dataList);
        } else {
            for (String item : dataList) {
                if (item.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        Log.d("FILTER", "Résultats après filtrage : " + filteredList.toString());

        adapter.notifyDataSetChanged();

        // Afficher ou masquer les résultats de recherche (mais rien d’autre ne change)
        searchResults.setVisibility(filteredList.isEmpty() || text.isEmpty() ? View.GONE : View.VISIBLE);
    }

}
