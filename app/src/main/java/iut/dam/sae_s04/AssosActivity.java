package iut.dam.sae_s04;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class AssosActivity extends Fragment {

    public AssosActivity() {
        // Le constructeur par défaut
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout du fragment
        View rootView = inflater.inflate(R.layout.activity_assos, container, false);

//        setContentView(R.layout.activity_assos);
//        setupBottomNavigation();
        // Récupérer les vues
        ImageView imageView = rootView.findViewById(R.id.association_image);
        TextView titleView = rootView.findViewById(R.id.association_title);
        TextView descriptionView = rootView.findViewById(R.id.association_description);

        // Récupérer les données envoyées par l'intent
        Association association = AssociationData.getInstance().getAssociationByName(titleView.toString());

        if (association != null) {
            // Mettre à jour l'UI avec les données de l'association
            titleView.setText(association.getTitle());
            imageView.setImageResource(association.getLogoResId());
            descriptionView.setText(association.getDescription());
        }
        return rootView;
    }
}