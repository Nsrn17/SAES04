package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import iut.dam.sae_s04.database.DatabaseHelper;

public class LoginActivity extends Fragment {

    private EditText idInput, passwordInput;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    public LoginActivity() {
        // Constructeur vide
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout du fragment
        View rootView = inflater.inflate(R.layout.activity_login, container, false);

        // Initialisation des vues
        idInput = rootView.findViewById(R.id.et_id_login);
        passwordInput = rootView.findViewById(R.id.et_password_login);
        loginButton = rootView.findViewById(R.id.btn_login);

        // Initialiser la base de données
        dbHelper = new DatabaseHelper(getActivity());

        // Gérer le clic du bouton login
        loginButton.setOnClickListener(v -> {
            String identifier = idInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Vérifier les identifiants
            if (dbHelper.checkUser(identifier, password)) {
                Toast.makeText(getActivity(), "Connexion réussie", Toast.LENGTH_SHORT).show();
                // Rediriger vers la MainActivity
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();  // Terminer l'activité après la connexion
            } else {
                Toast.makeText(getActivity(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });

        // Gestion du bouton inscription
        Button signup = rootView.findViewById(R.id.btn_page_signup);
        signup.setOnClickListener(v -> {
            // Passer à l'écran d'inscription (RegisterFragment ou autre)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new RegisterActivity())  // Remplacer par le fragment d'inscription
                    .addToBackStack(null)  // Ajouter à la pile arrière
                    .commit();
        });

//        // Gestion du bouton "voir les utilisateurs" (si besoin)
//        Button viewUsersButton = rootView.findViewById(R.id.btn_view_users);
//        viewUsersButton.setOnClickListener(v -> {
//            // Passer à l'écran des utilisateurs (UsersFragment ou autre)
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frame_container, new UsersFragment())  // Remplacer par le fragment des utilisateurs
//                    .addToBackStack(null)  // Ajouter à la pile arrière
//                    .commit();
//        });

        return rootView;
    }
}
