package iut.dam.sae_s04.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;

public class RegisterFragment extends Fragment {
    private EditText nameInput, emailInput, usernameInput, passwordInput;
    private Button registerButton , btn_login;
    private DatabaseHelper dbHelper;

    public RegisterFragment() {
        // Constructeur par défaut
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout du fragment
        View rootView = inflater.inflate(R.layout.activity_register, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Initialiser les vues
        nameInput = rootView.findViewById(R.id.et_name_register);
        emailInput = rootView.findViewById(R.id.et_id_register);
        usernameInput = rootView.findViewById(R.id.et_username_register);
        passwordInput = rootView.findViewById(R.id.et_password_register);
        registerButton = rootView.findViewById(R.id.btn_signup);
        btn_login = rootView.findViewById(R.id.btn_page_login);
        dbHelper = new DatabaseHelper(getContext()); // Utilisation de getContext() ici

        btn_login.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new LoginFragment())  // Remplacer le fragment dans le FrameLayout
                    .addToBackStack(null)  // Ajoute la transaction à la pile arrière si tu veux pouvoir revenir en arrière
                    .commit();
        });
        // Définir le comportement du bouton d'inscription
        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                boolean success = dbHelper.registerUser(email, username, name, password);
                if (success) {
                    Toast.makeText(getContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();

                    // Prépare les données à envoyer
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email);  // ou "username", selon ce que tu préfères

                    LoginFragment loginFragment = new LoginFragment();
                    loginFragment.setArguments(bundle);

                    // Affiche le LoginFragment avec les données
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, loginFragment)
                            .addToBackStack(null)
                            .commit();

                } else {
                    Toast.makeText(getContext(), "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;  // Retourner la vue gonflée
    }
}
