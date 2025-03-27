package iut.dam.sae_s04;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ExplorerFragment extends Fragment {

    private EditText searchInput;
    private ListView searchResults;
    private List<String> dataList; // Liste complète des données
    private List<String> filteredList; // Liste temporaire après filtrage
    private CustomAdapter adapter;

    public ExplorerFragment() {
        // Le constructeur par défaut
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfle la vue du fragment
        View rootView = inflater.inflate(R.layout.activity_explorer, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Référence aux vues dans le fragment
        searchInput = rootView.findViewById(R.id.search_input);
        searchResults = rootView.findViewById(R.id.search_results);

        LinearLayout logosSante = rootView.findViewById(R.id.logos_sante);
        LinearLayout logosMentale = rootView.findViewById(R.id.logos_mentale);
        LinearLayout logosFamille = rootView.findViewById(R.id.logos_famille);

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

            // Recherche de l'association correspondante
            for (Association association : AssociationData.getInstance().getAssociations()) {
                if (association.getTitle().equals(selectedTitle)) {
                    selectedAssociation = association;
                    break;
                }
            }

            if (selectedAssociation != null) {
                // Créer une instance de la liste complète d'associations
                ArrayList<Association> associationsList = new ArrayList<>(AssociationData.getInstance().getAssociations());

                // Appel de la méthode openDetailFragment avec la nouvelle signature
                NavigationUtils.openDetailFragment(requireActivity(), selectedAssociation, associationsList, position);
            } else {
                Log.e("ExplorerFragment", "Association introuvable !");
            }
        });





        setupSearch();

        for (Association assoc : associations) {
            ImageView logo = new ImageView(getContext());
            logo.setImageResource(assoc.getLogoResId());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
            params.setMargins(8, 0, 8, 0);
            logo.setLayoutParams(params);
            ArrayList<Association> associationsList = new ArrayList<>(AssociationData.getInstance().getAssociations());
            int position = associations.indexOf(assoc);
            logo.setOnClickListener(v -> NavigationUtils.openDetailFragment(getActivity(), assoc, associationsList, position));


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

        return rootView;  // Retourne la vue gonflée
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
    }
}
