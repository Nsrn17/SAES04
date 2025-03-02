package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AssosActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assos);
        // Récupérer les vues
        ImageView imageView = findViewById(R.id.association_image);
        TextView titleView = findViewById(R.id.association_title);
        TextView descriptionView = findViewById(R.id.association_description);

        // Récupérer les données envoyées par l'intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            int imageResId = intent.getIntExtra("imageResId", 0); // 0 par défaut si pas trouvé
            String description = intent.getStringExtra("description");

            // Mettre à jour l'UI
            titleView.setText(name);
            imageView.setImageResource(imageResId);
            descriptionView.setText(description);
        };
        //Barre de navigation
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