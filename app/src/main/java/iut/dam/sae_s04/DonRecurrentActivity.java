package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class DonRecurrentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_recurrent);

        AppCompatButton btnAnnuel = findViewById(R.id.btn_annuel);
        AppCompatButton btnMensuel = findViewById(R.id.btn_mensuel);
        AppCompatButton btnUnique = findViewById(R.id.btn_unique);

        btnAnnuel.setOnClickListener(v -> {
            btnAnnuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnAnnuel.setTextColor(getResources().getColor(android.R.color.white));
            btnMensuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnMensuel.setTextColor(getResources().getColor(R.color.blue));
        });

        btnMensuel.setOnClickListener(v -> {
            btnMensuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnMensuel.setTextColor(getResources().getColor(android.R.color.white));
            btnAnnuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnAnnuel.setTextColor(getResources().getColor(R.color.blue));
        });

        btnUnique.setOnClickListener(v -> {
            Intent intent = new Intent(DonRecurrentActivity.this, DonUniqueActivity.class);
            startActivity(intent);
        });

        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
        Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);
        Button settingButton =findViewById(R.id.settings);
        Button searchButton =findViewById(R.id.search);
        navigateToActivity(searchButton, ExplorerActivity.class);

        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
        navigateToActivity(addRingButton, DonUniqueActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
        navigateToActivity(settingButton, ParametresActivity.class);
    }
}