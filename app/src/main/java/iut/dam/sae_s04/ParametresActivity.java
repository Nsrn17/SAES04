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

import java.util.HashMap;

public class ParametresActivity extends BaseActivity {
    private Spinner spinnerDaltonien;
    private SeekBar seekBarTailleTexte;
    private Button btnAppliquer;
    private ViewGroup rootLayout;
    private HashMap<TextView, Float> originalTextSizes = new HashMap<>(); // Stocke les tailles originales

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        spinnerDaltonien = findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = findViewById(R.id.btn_appliquer);
        rootLayout = findViewById(R.id.main); // ID du layout parent

        setupBottomNavigation();
        // Définir les options du spinner pour les modes daltonien
        String[] modesDaltoniens = {"Normal", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);

        // Récupérer et appliquer les préférences
        int savedMode = getSavedMode();
        float savedTextSizeFactor = getSavedTextSizeFactor();

        spinnerDaltonien.setSelection(savedMode);

        // Sauvegarder les tailles originales des TextView
        saveOriginalTextSizes(rootLayout);

        // Appliquer la taille de texte en fonction du facteur sauvegardé
        applyTextSizeFactor(savedTextSizeFactor);

        // Ajuster la SeekBar en fonction du facteur sauvegardé (0 = taille normale)
        seekBarTailleTexte.setProgress((int) savedTextSizeFactor);

        // Écouteur SeekBar pour mise à jour en temps réel
//        seekBarTailleTexte.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                applyTextSizeFactor(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {}
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {}
//        });

        // Écouteur sur le bouton Appliquer
        btnAppliquer.setOnClickListener(v -> {
            int selectedMode = spinnerDaltonien.getSelectedItemPosition();
            float textSizeFactor = seekBarTailleTexte.getProgress();

            savePreferences(selectedMode, textSizeFactor);

            // Redémarrer l'activité pour appliquer le mode daltonien
            recreate();
        });
    }

    /**
     * Sauvegarde les tailles originales des TextView
     */
    private void saveOriginalTextSizes(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                originalTextSizes.put(textView, textView.getTextSize()); // Stocker la taille originale
            } else if (child instanceof ViewGroup) {
                saveOriginalTextSizes((ViewGroup) child); // Explorer les sous-vues
            }
        }
    }

    /**
     * Applique un facteur d'agrandissement/réduction basé sur les tailles originales.
     */
    private void applyTextSizeFactor(float factor) {
        for (HashMap.Entry<TextView, Float> entry : originalTextSizes.entrySet()) {
            TextView textView = entry.getKey();
            float originalSize = entry.getValue();
            textView.setTextSize(originalSize / getResources().getDisplayMetrics().scaledDensity + factor);
        }
    }

    /**
     * Sauvegarde le mode daltonien et le facteur de taille du texte dans SharedPreferences.
     */
    private void savePreferences(int mode, float textSizeFactor) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("daltonien_mode", mode);
        editor.putFloat("text_size_factor", textSizeFactor);
        editor.apply();
    }

    private int getSavedMode() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("daltonien_mode", 0);  // Valeur par défaut 0 (Normal)
    }

    private float getSavedTextSizeFactor() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getFloat("text_size_factor", 0);  // Valeur par défaut = 0 (taille normale)
    }

}
