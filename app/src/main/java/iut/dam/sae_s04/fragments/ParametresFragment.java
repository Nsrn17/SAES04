package iut.dam.sae_s04.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
    private HashMap<TextView, Float> originalTextSizes = new HashMap<>();
    private HashMap<TextView, Typeface> originalTypefaces = new HashMap<>();
    private Switch switchDyslexie;

    public ParametresFragment() {
        // Constructeur vide requis
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parametres, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        // Initialisation des vues
        initViews(rootView);

        // Charger et appliquer les préférences
        loadAndApplyPreferences();

        // Configuration des composants
        setupSpinner();
        setupSwitchListener();
        setupApplyButton();
        setupResetAdminsButton(rootView);

        return rootView;
    }

    private void initViews(View rootView) {
        spinnerDaltonien = rootView.findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = rootView.findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = rootView.findViewById(R.id.btn_appliquer);
        rootLayout = rootView.findViewById(R.id.main);
        switchDyslexie = rootView.findViewById(R.id.sw_status);
    }

    private void loadAndApplyPreferences() {
        boolean isDyslexicMode = getSavedDyslexicMode();
        int savedMode = getSavedMode();
        float savedTextSizeFactor = getSavedTextSizeFactor();

        switchDyslexie.setChecked(isDyslexicMode);
        spinnerDaltonien.setSelection(savedMode);

        saveOriginalSettings(rootLayout);
        applyTextSizeFactor(savedTextSizeFactor);
        seekBarTailleTexte.setProgress((int) savedTextSizeFactor);

        if (isDyslexicMode) {
            applyDyslexicFont(rootLayout);
        }
    }

    private void saveOriginalSettings(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                originalTextSizes.put(textView, textView.getTextSize());
                originalTypefaces.put(textView, textView.getTypeface());
            } else if (child instanceof ViewGroup) {
                saveOriginalSettings((ViewGroup) child);
            }
        }
    }

    private void setupSpinner() {
        String[] modesDaltoniens = {"Par Défaut", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);
    }

    private void setupSwitchListener() {
        switchDyslexie.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveDyslexicMode(isChecked);
            if (isChecked) {
                applyDyslexicFont(rootLayout);
            } else {
                restoreOriginalFonts(rootLayout);
            }
            // Optionnel: recréer l'activité pour une application complète
            // requireActivity().recreate();
        });
    }

    private void setupApplyButton() {
        btnAppliquer.setOnClickListener(v -> {
            savePreferences(
                    spinnerDaltonien.getSelectedItemPosition(),
                    seekBarTailleTexte.getProgress()
            );
            restartActivity();
        });
    }

    private void setupResetAdminsButton(View rootView) {
        Button btnResetAdmins = rootView.findViewById(R.id.btn_reset_admins);
        btnResetAdmins.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(getContext());
            dbHelper.resetAdmins();
            Toast.makeText(getContext(), "Admins réinitialisés", Toast.LENGTH_SHORT).show();
        });
    }

    private void applyDyslexicFont(ViewGroup parent) {
        try {
            Typeface dyslexicFont = Typeface.createFromAsset(
                    requireActivity().getAssets(),
                    "fonts/opendyslexic3_regular.ttf"
            );

            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child instanceof TextView) {
                    TextView textView = (TextView) child;
                    Typeface original = originalTypefaces.get(textView);
                    textView.setTypeface(dyslexicFont,
                            original != null ? original.getStyle() : Typeface.NORMAL);
                } else if (child instanceof ViewGroup) {
                    applyDyslexicFont((ViewGroup) child);
                }
            }
        } catch (Exception e) {
            Log.e("Dyslexie", "Erreur chargement police", e);
            Toast.makeText(getContext(), "Erreur chargement police", Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreOriginalFonts(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                Typeface original = originalTypefaces.get(textView);
                if (original != null) {
                    textView.setTypeface(original);
                }
            } else if (child instanceof ViewGroup) {
                restoreOriginalFonts((ViewGroup) child);
            }
        }
    }

    private void applyTextSizeFactor(float factor) {
        for (HashMap.Entry<TextView, Float> entry : originalTextSizes.entrySet()) {
            TextView textView = entry.getKey();
            float originalSize = entry.getValue();
            textView.setTextSize(originalSize / getResources().getDisplayMetrics().scaledDensity + factor);
        }
    }

    private void restartActivity() {
        Intent intent = requireActivity().getIntent();
        requireActivity().finish();
        startActivity(intent);
    }

    private void savePreferences(int mode, float textSizeFactor) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        prefs.edit()
                .putInt("daltonien_mode", mode)
                .putFloat("text_size_factor", textSizeFactor)
                .apply();
    }

    private int getSavedMode() {
        return requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getInt("daltonien_mode", 0);
    }

    private float getSavedTextSizeFactor() {
        return requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getFloat("text_size_factor", 0);
    }

    private void saveDyslexicMode(boolean isEnabled) {
        requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("dyslexic_mode", isEnabled)
                .apply();
    }

    private boolean getSavedDyslexicMode() {
        return requireActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getBoolean("dyslexic_mode", false);
    }
}