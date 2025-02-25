package iut.dam.sae_s04;

import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends  BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Barre de navigation
        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
        // Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);


        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
        // navigateToActivity(addRingButton, AddRingActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
    }
}