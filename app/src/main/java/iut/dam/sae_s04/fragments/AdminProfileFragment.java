package iut.dam.sae_s04.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.utils.SessionManager;

public class AdminProfileFragment extends Fragment {

    private TextView textEmail, textName, textUsername, textAsso, textResume;
    private ImageView imageCadenas;
    private Button btnDeconnexion;

    public AdminProfileFragment() {
        // Constructeur par défaut
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_admin_profil, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Récupération de l'admin connecté
        Admin admin = SessionManager.getCurrentAdmin(getContext(), new iut.dam.sae_s04.database.DatabaseHelper(getContext()));

        // Initialisation des vues
        textEmail = rootView.findViewById(R.id.admin_profil_email);
        textName = rootView.findViewById(R.id.admin_profil_name);
        textUsername = rootView.findViewById(R.id.admin_profil_username);
        textAsso = rootView.findViewById(R.id.admin_profil_asso);
        textResume = rootView.findViewById(R.id.text_resume_dons);
        imageCadenas = rootView.findViewById(R.id.admin_cadenas);
        btnDeconnexion = rootView.findViewById(R.id.btn_deconnexion);

        if (admin != null) {
            textEmail.setText(admin.getEmail());
            textName.setText(admin.getName());
            textUsername.setText(admin.getUsername());
            textAsso.setText(admin.getAssociation());
        } else {
            textEmail.setText("Inconnu");
            textName.setText("Inconnu");
            textUsername.setText("Inconnu");
            textAsso.setText("Inconnue");
        }

        // Action : accéder au résumé des dons
        textResume.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ResumeFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Déconnexion
        btnDeconnexion.setOnClickListener(v -> {
            SessionManager.clear(getContext());
            ((MainActivity) requireActivity()).setCurrentAdmin(null);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())
                    .commit();
        });

        return rootView;
    }
}
