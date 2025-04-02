package iut.dam.sae_s04.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.util.HashMap;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;

public class ParametresFragment extends Fragment {
    private Spinner spinnerDaltonien;
    private SeekBar seekBarTailleTexte;
    private Button btnAppliquer;
    private ViewGroup rootLayout;
    private HashMap<TextView, Float> originalTextSizes = new HashMap<>();// Stocke les tailles originales

    private Switch switchDyslexie;

    public ParametresFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout du fragment
        View rootView = inflater.inflate(R.layout.activity_parametres, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Initialisation des vues
        spinnerDaltonien = rootView.findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = rootView.findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = rootView.findViewById(R.id.btn_appliquer);
        rootLayout = rootView.findViewById(R.id.main); // ID du layout parent
        switchDyslexie = rootView.findViewById(R.id.sw_status);

        // Charger les préférences enregistrées
        boolean isDyslexicMode = getSavedDyslexicMode(); // Récupère l'état du switch
        int savedMode = getSavedMode();
        float savedTextSizeFactor = getSavedTextSizeFactor();

        // Appliquer les préférences
        switchDyslexie.setChecked(isDyslexicMode); // Le switch est éteint par défaut (false)
        spinnerDaltonien.setSelection(savedMode);
        saveOriginalTextSizes(rootLayout);
        applyTextSizeFactor(savedTextSizeFactor);
        seekBarTailleTexte.setProgress((int) savedTextSizeFactor);

        // Définir les options du spinner pour les modes daltonien
        String[] modesDaltoniens = {"Par Défaut", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);

        // Écouteur sur le Switch (applique immédiatement sans redémarrer toute l'activité)
        switchDyslexie.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveDyslexicMode(isChecked);
            requireActivity().recreate();
            Log.d("e","fonction use");// Redémarre l'activité pour appliquer la police immédiatement
        });

        // Écouteur sur le bouton "Appliquer" (seul ce bouton redémarre la page)
        btnAppliquer.setOnClickListener(v -> {
            int selectedMode = spinnerDaltonien.getSelectedItemPosition();
            float textSizeFactor = seekBarTailleTexte.getProgress();

            savePreferences(selectedMode, textSizeFactor);

            // Redémarrer l'activité pour appliquer tous les changements
            Intent intent = requireActivity().getIntent();
            requireActivity().finish();
            requireActivity().startActivity(intent);
        });

        // Bouton pour réinitialiser les admins
        Button btnResetAdmins = rootView.findViewById(R.id.btn_reset_admins);
        btnResetAdmins.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbHelper.resetAdmins();
            Toast.makeText(getContext(), "Admins réinitialisés", Toast.LENGTH_SHORT).show();
        });

        return rootView;
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
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("daltonien_mode", mode);
        editor.putFloat("text_size_factor", textSizeFactor);
        editor.apply();
    }

    private int getSavedMode() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getInt("daltonien_mode", 0);  // Valeur par défaut 0 (Normal)
    }

    private float getSavedTextSizeFactor() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getFloat("text_size_factor", 0);  // Valeur par défaut = 0 (taille normale)
    }

    private void saveDyslexicMode(boolean isEnabled) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("dyslexic_mode", isEnabled);
        editor.apply();
    }

    private boolean getSavedDyslexicMode() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getBoolean("dyslexic_mode", false); // false par défaut (éteint)
    }



}
