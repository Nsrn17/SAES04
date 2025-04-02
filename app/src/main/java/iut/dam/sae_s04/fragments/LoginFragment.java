package iut.dam.sae_s04.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.models.User;
import iut.dam.sae_s04.utils.SessionManager;

public class LoginFragment extends Fragment {

    private EditText idInput, passwordInput;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);


        idInput = rootView.findViewById(R.id.et_id_login);
        idInput.requestFocus();
        idInput.post(() -> {
            idInput.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(idInput, InputMethodManager.SHOW_IMPLICIT);
        });
        passwordInput = rootView.findViewById(R.id.et_password_login);
        loginButton = rootView.findViewById(R.id.btn_login);
        dbHelper = new DatabaseHelper(getActivity());

        Bundle args = getArguments();
        if (args != null) {
            String email = args.getString("email");
            if (email != null) {
                idInput.setText(email);
            }
        }

        loginButton.setOnClickListener(v -> {
            String identifier = idInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Connexion admin
            Admin admin = dbHelper.checkAdmin(identifier, password);
            if (admin != null) {
                SessionManager.setCurrentAdmin(getActivity(), admin);
                Toast.makeText(getActivity(), "Connexion admin réussie", Toast.LENGTH_SHORT).show();

                MainActivity.getInstance().setCurrentAdmin(admin);

                // Affiche immédiatement le fragment Resume après connexion
                MainActivity.getInstance().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new AdminProfileFragment())
                        .commit();

                return;
            }

            // Connexion utilisateur normal
            if (dbHelper.checkUser(identifier, password)) {
                Toast.makeText(getActivity(), "Connexion réussie", Toast.LENGTH_SHORT).show();

                User user = dbHelper.getUserByIdentifier(identifier);
                SessionManager.setCurrentUser(getActivity(), user);
                MainActivity.getInstance().setCurrentUser(user);


                // Affiche immédiatement le fragment Resume après connexion
                MainActivity.getInstance().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new ProfileFragment())
                        .commit();

                return;
            }

            // Cas d'erreur
            Toast.makeText(getActivity(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();
        });

        rootView.findViewById(R.id.btn_page_signup).setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return rootView;
    }
}
