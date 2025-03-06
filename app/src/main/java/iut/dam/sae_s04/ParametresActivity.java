package iut.dam.sae_s04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ParametresActivity extends BaseActivity {
    private Spinner spinnerDaltonien;
    private SeekBar seekBarTailleTexte;
    private Button btnAppliquer;
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        spinnerDaltonien = findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = findViewById(R.id.btn_appliquer);
        rootLayout = findViewById(R.id.main); // Remplacez par l'ID du layout parent principal

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

        // Définir les options du spinner pour les modes daltonien
        String[] modesDaltoniens = {"Normal", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);

        // Récupérer et appliquer les préférences
        int savedMode = getSavedMode();
        float savedTextSize = getSavedTextSize();

        spinnerDaltonien.setSelection(savedMode);

        // Appliquer la taille du texte sauvegardée dès la création de l'activité
        updateTextViewSize(rootLayout, savedTextSize);

        // Ajuster la position de la SeekBar en fonction de la taille du texte sauvegardée
       // seekBarTailleTexte.setProgress((int) Math.max(0, Math.min(100, savedTextSize - 12))); // Assurez-vous que la progress bar reste dans un intervalle valide

        // Écouteur sur le bouton Appliquer
        btnAppliquer.setOnClickListener(v -> {
            int selectedMode = spinnerDaltonien.getSelectedItemPosition();
            float textSize = 12 + seekBarTailleTexte.getProgress(); // Valeur par défaut basée sur la SeekBar

            // Si la seekbar est à 2, applique une règle spéciale
            if (seekBarTailleTexte.getProgress() == 2) {
                textSize = 15;
            }

            // Sauvegarder les préférences (mode + taille du texte)
            savePreferences(selectedMode, textSize);

            // Appliquer la nouvelle taille du texte
            updateTextViewSize(rootLayout, textSize);

            // Recharger l'activité pour appliquer le mode daltonien
            recreate();
        });

    }


    /**
     * Sauvegarde le mode daltonien et la taille du texte dans SharedPreferences.
     */
    private void savePreferences(int mode, float textSize) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("daltonien_mode", mode);
        editor.putFloat("text_size", textSize);
        editor.apply();
    }


    /**
     * Met à jour la taille de toutes les TextView dans un ViewGroup.
     */
    private void updateTextViewSize(ViewGroup parent, float textSize) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                ((TextView) child).setTextSize(textSize);
            } else if (child instanceof ViewGroup) {
                updateTextViewSize((ViewGroup) child, textSize);
            }
        }
    }

    private int getSavedMode() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("daltonien_mode", 0);  // Valeur par défaut 0 (Normal)
    }

    private float getSavedTextSize() {

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getFloat("text_size" , 16);  // Valeur par défaut 16
    }

}
