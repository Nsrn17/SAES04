package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AssosFragment extends Fragment {

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

            // Mettre à jour l'interface utilisateur
            titleView.setText(title);
            imageView.setImageResource(logoResId);
            descriptionView.setText(description);
        }

        return rootView;
    }



}