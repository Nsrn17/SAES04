package iut.dam.sae_s04.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.models.User;
import iut.dam.sae_s04.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Récupérer l'utilisateur connecté
        User currentUser = SessionManager.getCurrentUser(getContext(), dbHelper);

        TextView textEmail = rootView.findViewById(R.id.profil_email);
        TextView textNom = rootView.findViewById(R.id.profil_name);
        TextView textUsername = rootView.findViewById(R.id.profil_username);
        Button btnDeconnexion = rootView.findViewById(R.id.btn_deconnexion);
        TextView textResume = rootView.findViewById(R.id.text_appuyez);

        if (currentUser != null) {
            textEmail.setText("Votre e-mail : " + currentUser.getEmail());
            textNom.setText("Votre nom : " + currentUser.getName());
            textUsername.setText("Votre nom d'utilisateur : " + currentUser.getUsername());
        } else {
            textEmail.setText("Non connecté");
            textNom.setText("-");
            textUsername.setText("-");
        }

        btnDeconnexion.setOnClickListener(v -> {
            SessionManager.clear(getContext());
            Toast.makeText(getContext(), "Déconnecté avec succès", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        textResume.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new UserResumeFragment())
                    .addToBackStack(null)
                    .commit();
        });


        return rootView;
    }
}
