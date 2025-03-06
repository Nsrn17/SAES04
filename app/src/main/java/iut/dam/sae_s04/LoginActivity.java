package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iut.dam.sae_s04.database.DatabaseHelper;

public class LoginActivity extends BaseActivity {

    private EditText idInput, passwordInput;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Barre de navigation
        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
        Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);
        Button signup= findViewById(R.id.btn_page_signup);
        Button viewUsersButton =findViewById(R.id.btn_view_users);
        Button settingButton =findViewById(R.id.settings);
        Button searchButton =findViewById(R.id.search);
        navigateToActivity(searchButton, ExplorerActivity.class);

        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
        navigateToActivity(addRingButton, DonUniqueActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
        navigateToActivity(signup, RegisterActivity.class);
        navigateToActivity(viewUsersButton, UsersActivity.class);
        navigateToActivity(settingButton, ParametresActivity.class);

        dbHelper = new DatabaseHelper(this);

        idInput = findViewById(R.id.et_id_login);
        passwordInput = findViewById(R.id.et_password_login);
        loginButton = findViewById(R.id.btn_login);

        loginButton.setOnClickListener(v -> {
            String identifier = idInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (dbHelper.checkUser(identifier, password)) {
                Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
            }
        });
    }
}