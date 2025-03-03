package iut.dam.sae_s04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

public class ParametresActivity extends BaseActivity {
    private Spinner spinnerDaltonien;
    private SeekBar seekBarTailleTexte;
    private Button btnAppliquer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //applySavedColors(); // Appliquer les couleurs avant l'affichage
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        spinnerDaltonien = findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = findViewById(R.id.btn_appliquer);

        // Définition des options du spinner
        String[] modesDaltoniens = {"Normal", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);

        // Sélectionner le mode enregistré
        spinnerDaltonien.setSelection(getSavedMode());

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


        // Action sur le bouton Appliquer
        btnAppliquer.setOnClickListener(v -> {
            int selectedMode = spinnerDaltonien.getSelectedItemPosition();
            saveMode(selectedMode); // Sauvegarder la sélection
            recreate(); // Recharger l'activité pour appliquer les couleurs
        });
    }

    /**
     * Sauvegarde le mode daltonien choisi dans SharedPreferences.
     */
    private void saveMode(int mode) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("daltonien_mode", mode);
        editor.apply();
    }

    /**
     * Récupère le mode daltonien enregistré dans SharedPreferences.
     */
    private int getSavedMode() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("daltonien_mode", 0); // 0 = Mode normal par défaut
    }



}
