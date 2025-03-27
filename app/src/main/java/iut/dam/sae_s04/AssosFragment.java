package iut.dam.sae_s04;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class AssosFragment extends Fragment {
    private ProgressAdapter padapter;
    public AssosFragment() {
        // Le constructeur par défaut
    }

    public static AssosFragment newInstance(String name, int logoResId, String description) {
        AssosFragment fragment = new AssosFragment();
        Bundle args = new Bundle();
        args.putString("association_name", name);
        args.putInt("association_logo", logoResId);
        args.putString("association_description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_assos, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        ImageView imageView = rootView.findViewById(R.id.association_image);
        TextView titleView = rootView.findViewById(R.id.association_title);
        TextView descriptionView = rootView.findViewById(R.id.association_description);

//        // Récupérer les données envoyées par le Bundle
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("association_name", "Nom inconnu");
            int logoResId = args.getInt("association_logo", R.drawable.logoassos);
            String description = args.getString("association_description", "Pas de description");
            int pos=args.getInt("selected_position");
            // Mettre à jour l'interface utilisateur
            titleView.setText(title);
            imageView.setImageResource(logoResId);
            descriptionView.setText(description);
        }
        //Debut cariusel
       RecyclerView viewPager = rootView.findViewById(R.id.recycler_progress_bar);

        viewPager.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)); // Exemple pour un carrousel horizontal


        List<Association> associations = AssociationData.getInstance().getAssociations();
        int n=0;
        for (Association association : associations) {
            for (int i=0; i<associations.size();i++) {
                if (association.getTitle().equals(args.getString("association_name", "Nom inconnu"))) {
                    n = i;
                }

            }}
        padapter = new ProgressAdapter(associations.size(), n, position -> {
            // Affiche la position cliquée
            Log.d("RecyclerView", "Item cliqué à la position : " + position);

            // Change de fragment avec la nouvelle association
            Association selectedAssociation = associations.get(position);
            AssosFragment newFragment = AssosFragment.newInstance(
                    selectedAssociation.getTitle(),
                    selectedAssociation.getLogoResId(),
                    selectedAssociation.getDescription()
            );

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, newFragment)  // Remplace le fragment actuel
                    .addToBackStack(null)
                    .commit();
        });

        viewPager.setAdapter(padapter);
        Log.d("Error","cc");
        Log.d("AssosFragment", "Adapter set with size: " + associations.size());

        ///fincarousel

        return rootView;
    }



}