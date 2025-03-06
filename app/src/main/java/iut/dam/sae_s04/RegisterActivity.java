package iut.dam.sae_s04;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iut.dam.sae_s04.database.DatabaseHelper;


public class RegisterActivity extends BaseActivity {
    private EditText nameInput, emailInput, usernameInput, passwordInput;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Barre de navigation
        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
        Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);
        Button settingButton =findViewById(R.id.settings);
        Button sigin=findViewById(R.id.btn_page_login);


        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
        navigateToActivity(addRingButton, DonUniqueActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
        navigateToActivity(sigin , LoginActivity.class);
        navigateToActivity(settingButton, ParametresActivity.class);

        dbHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.et_name_register);
        emailInput = findViewById(R.id.et_id_register);
        usernameInput = findViewById(R.id.et_username_register);
        passwordInput = findViewById(R.id.et_password_register);
        registerButton = findViewById(R.id.btn_signup);

        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String email = emailInput.getText().toString();
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                boolean success = dbHelper.registerUser(name, email, username, password);
                if (success) {
                    Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterActivity", "Erreur : pas de mess !");
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
        });

    }
}