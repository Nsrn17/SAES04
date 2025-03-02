package iut.dam.sae_s04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class ParametresActivity extends BaseActivity {
    private Spinner spinnerDaltonien;
    private SeekBar seekBarTailleTexte;
    private Button btnAppliquer;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "ParametresActivity créée !");
        setContentView(R.layout.activity_parametres);

        // Barre de navigation
        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
        Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);
        Button settingButton = findViewById(R.id.settings);
        Button searchButton = findViewById(R.id.search);
        navigateToActivity(searchButton, ExplorerActivity.class);
        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
        navigateToActivity(addRingButton, DonUniqueActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
        navigateToActivity(settingButton, ParametresActivity.class);

        // Initialisation des vues
        spinnerDaltonien = findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = findViewById(R.id.btn_appliquer);

        // Initialisation de SharedPreferences
        sharedPreferences = getSharedPreferences("ParametresUtilisateur", MODE_PRIVATE);

        // Remplir le Spinner avec les modes daltoniens
        String[] modesDaltoniens = {"Normal", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);

        // Charger la taille du texte sauvegardée
        int tailleTexteProgress = sharedPreferences.getInt("tailleTexteProgress", 2);
        seekBarTailleTexte.setProgress(tailleTexteProgress);

        // Gestion de la SeekBar pour la taille du texte
        seekBarTailleTexte.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float tailleTexte = 16 + (progress * 4);
                applyTextSize(tailleTexte);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Gestion du bouton "Appliquer"
        btnAppliquer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sauvegarder les préférences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("tailleTexteProgress", seekBarTailleTexte.getProgress());
                editor.apply();

                // Appliquer les changements
                applyDaltonienMode(spinnerDaltonien.getSelectedItemPosition());
                applyTextSize(16 + (seekBarTailleTexte.getProgress() * 4));
            }
        });
    }

    private void applyDaltonienMode(int mode) {
        switch (mode) {
            case 1: // Protanopie
                break;
            case 2: // Deutéranopie
                break;
            case 3: // Tritanopie
                break;
            default: // Normal
                break;
        }
    }

    private void applyTextSize(float tailleTexte) {
        // Cette méthode peut être utilisée pour appliquer la taille du texte à toutes les vues
    }
}