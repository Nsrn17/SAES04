package iut.dam.sae_s04.utils;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import iut.dam.sae_s04.R;
import iut.dam.sae_s04.fragments.AccueilFragment;
import iut.dam.sae_s04.fragments.DonUniqueFragment;
import iut.dam.sae_s04.fragments.ExplorerFragment;
import iut.dam.sae_s04.fragments.LoginFragment;

public class BaseActivity extends AppCompatActivity {
    private final HashMap<TextView, Float> originalTextSizes = new HashMap<>();
    private final HashMap<TextView, Typeface> originalTypefaces = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Appliquer les couleurs avant super.onCreate()
        applySavedColors();
        super.onCreate(savedInstanceState);

        // Initialisation des polices et tailles de texte
        initFontAndTextSize();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // Configuration après le setContentView
        setupBottomNavigation();
        applyCurrentSettings();
    }

    private void initFontAndTextSize() {
        View decorView = getWindow().getDecorView();
        if (decorView instanceof ViewGroup) {
            saveOriginalSettings((ViewGroup) decorView);
        }
    }

    private void saveOriginalSettings(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                // Sauvegarde taille texte
                if (!originalTextSizes.containsKey(textView)) {
                    originalTextSizes.put(textView, textView.getTextSize());
                }
                // Sauvegarde police
                if (!originalTypefaces.containsKey(textView)) {
                    originalTypefaces.put(textView, textView.getTypeface());
                }
            } else if (child instanceof ViewGroup) {
                saveOriginalSettings((ViewGroup) child);
            }
        }
    }

    private void applyCurrentSettings() {
        applyTextSizeToAllViews();
        applyDyslexicFont();
    }

    // Gestion de la navigation
    public void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (bottomNavigationView == null) return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                selectedFragment = new AccueilFragment();
            } else if (itemId == R.id.navigation_explore) {
                selectedFragment = new ExplorerFragment();
            } else if (itemId == R.id.navigation_add) {
                selectedFragment = new DonUniqueFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new LoginFragment();
            }

            if (selectedFragment != null) {
                navigateToFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    public void navigateToFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    // Gestion des couleurs (daltonisme)
    private void applySavedColors() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int modeDaltonien = prefs.getInt("daltonien_mode", 0);

        switch (modeDaltonien) {
            case 1: setTheme(R.style.Colors_Protanopie); break;
            case 2: setTheme(R.style.Colors_Deuteranopie); break;
            case 3: setTheme(R.style.Colors_Tritanopie); break;
            default: setTheme(R.style.Colors_Normal);
        }
    }

    // Gestion de la taille du texte
    private void applyTextSizeToAllViews() {
        float textSizeFactor = getSavedTextSizeFactor();
        ViewGroup rootLayout = findViewById(android.R.id.content);
        if (rootLayout != null) {
            updateTextViewSize(rootLayout, textSizeFactor);
        }
    }

    private void updateTextViewSize(ViewGroup parent, float factor) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                float originalSize = originalTextSizes.getOrDefault(textView, textView.getTextSize());
                textView.setTextSize(originalSize / getResources().getDisplayMetrics().scaledDensity + factor);
            } else if (child instanceof ViewGroup) {
                updateTextViewSize((ViewGroup) child, factor);
            }
        }
    }

    private float getSavedTextSizeFactor() {
        return getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getFloat("text_size_factor", 0);
    }

    // Gestion des polices dyslexiques
    private void applyDyslexicFont() {
        if (getSavedDyslexicMode()) {
            Typeface dyslexicFont = ResourcesCompat.getFont(this, R.font.opendyslexic3_regular);
            if (dyslexicFont != null) {
                applyFontToAllTextViews(dyslexicFont);
            }
        } else {
            restoreOriginalFonts();
        }
    }

    private void applyFontToAllTextViews(Typeface typeface) {
        View rootView = getWindow().getDecorView();
        if (rootView instanceof ViewGroup) {
            applyFontToView((ViewGroup) rootView, typeface);
        }
    }

    private void applyFontToView(ViewGroup parent, Typeface typeface) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                Typeface original = originalTypefaces.get(textView);
                textView.setTypeface(typeface, original != null ? original.getStyle() : Typeface.NORMAL);
            } else if (child instanceof ViewGroup) {
                applyFontToView((ViewGroup) child, typeface);
            }
        }
    }

    private void restoreOriginalFonts() {
        View rootView = getWindow().getDecorView();
        if (rootView instanceof ViewGroup) {
            restoreFonts((ViewGroup) rootView);
        }
    }

    private void restoreFonts(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                Typeface original = originalTypefaces.get(textView);
                if (original != null) {
                    textView.setTypeface(original);
                }
            } else if (child instanceof ViewGroup) {
                restoreFonts((ViewGroup) child);
            }
        }
    }

    // Méthodes publiques pour les fragments
    public void setDyslexicModeEnabled(boolean enabled) {
        getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("dyslexic_mode", enabled)
                .apply();
        applyDyslexicFont();
    }

    public boolean getSavedDyslexicMode() {
        return getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getBoolean("dyslexic_mode", false);
    }

    public void applySettingsToFragment(View fragmentView) {
        if (fragmentView instanceof ViewGroup) {
            saveOriginalSettings((ViewGroup) fragmentView);
            applyCurrentSettingsToView((ViewGroup) fragmentView);
        }
    }

    private void applyCurrentSettingsToView(ViewGroup parent) {
        float textSizeFactor = getSavedTextSizeFactor();
        Typeface currentFont = getSavedDyslexicMode()
                ? ResourcesCompat.getFont(this, R.font.opendyslexic3_regular)
                : Typeface.DEFAULT;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                // Taille du texte
                float originalSize = originalTextSizes.getOrDefault(textView, textView.getTextSize());
                textView.setTextSize(originalSize / getResources().getDisplayMetrics().scaledDensity + textSizeFactor);

                // Police
                Typeface original = originalTypefaces.get(textView);
                textView.setTypeface(currentFont, original != null ? original.getStyle() : Typeface.NORMAL);
            } else if (child instanceof ViewGroup) {
                applyCurrentSettingsToView((ViewGroup) child);
            }
        }
    }
}