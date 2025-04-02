package iut.dam.sae_s04.fragments;

import android.annotation.SuppressLint;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_profil, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Initialisation des vues
        textEmail = rootView.findViewById(R.id.admin_profil_email);
        textName = rootView.findViewById(R.id.admin_profil_name);
        textUsername = rootView.findViewById(R.id.admin_profil_username);
        textAsso = rootView.findViewById(R.id.admin_profil_asso);
        textResume = rootView.findViewById(R.id.text_resume_dons);
        //imageCadenas = rootView.findViewById(R.id.admin_cadenas);
        btnDeconnexion = rootView.findViewById(R.id.btn_deconnexion);

        // Récupération de l'admin connecté
        Admin admin = SessionManager.getCurrentAdmin(getContext(), new iut.dam.sae_s04.database.DatabaseHelper(getContext()));

        if (admin != null) {
            textEmail.setText("Email : " + admin.getEmail());
            textName.setText("Nom : " + admin.getName());
            textUsername.setText("Nom d'utilisateur : " + admin.getUsername());
            textAsso.setText("Association : " + admin.getAssociation());
        } else {
            textEmail.setText("Email : inconnu");
            textName.setText("Nom : inconnu");
            textUsername.setText("Nom d'utilisateur : inconnu");
            textAsso.setText("Association : inconnue");
        }

        // Accès au résumé des dons
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
