package iut.dam.sae_s04.utils;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.core.content.res.ResourcesCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import iut.dam.sae_s04.fragments.AccueilFragment;
import iut.dam.sae_s04.fragments.DonUniqueFragment;
import iut.dam.sae_s04.fragments.ExplorerFragment;
import iut.dam.sae_s04.fragments.LoginFragment;
import iut.dam.sae_s04.R;

public class BaseActivity extends AppCompatActivity {
    private final HashMap<TextView, Float> originalTextSizes = new HashMap<>(); // Stocke les tailles de base

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedColors();

        super.onCreate(savedInstanceState);
        applyDyslexicFont();
        setupBottomNavigation();
    }

    public void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.navigation_home ) {
                selectedFragment = new AccueilFragment();
            } else if (itemId == R.id.navigation_explore ) {
                selectedFragment = new ExplorerFragment();
            } else if (itemId == R.id.navigation_add ) {
                selectedFragment = new DonUniqueFragment();
            } else if (itemId == R.id.navigation_profile ) {
                selectedFragment = new LoginFragment();
            }

            if(selectedFragment != null){
                navigateToFragment(selectedFragment);
                return true;
            }

            return false;
        });
    }

    public void navigateToFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    private void applySavedColors() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int modeDaltonien = prefs.getInt("daltonien_mode", 0);

        switch (modeDaltonien) {
            case 1:
                setTheme(R.style.Colors_Protanopie);
                break;
            case 2:
                setTheme(R.style.Colors_Deuteranopie);
                break;
            case 3:
                setTheme(R.style.Colors_Tritanopie);
                break;
            default:
                setTheme(R.style.Colors_Normal);
        }
    }

    private void applyTextSizeToAllViews() {
        float textSizeFactor = getSavedTextSizeFactor();
        ViewGroup rootLayout = findViewById(android.R.id.content);
        if (rootLayout != null) {
            saveOriginalTextSizes(rootLayout); // S'assure qu'on a les tailles originales
            updateTextViewSize(rootLayout, textSizeFactor);
        }
    }

    /**
     * Sauvegarde les tailles originales des TextView
     */
    private void saveOriginalTextSizes(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (!originalTextSizes.containsKey(textView)) { // On ne l'ajoute qu'une fois
                    originalTextSizes.put(textView, textView.getTextSize());
                }
            } else if (child instanceof ViewGroup) {
                saveOriginalTextSizes((ViewGroup) child);
            }
        }
    }

    /**
     * Applique un facteur de mise à l'échelle aux TextView
     */
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
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getFloat("text_size_factor", 0); // 0 = pas de changement
    }

    private void applyDyslexicFont() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        boolean isDyslexicMode = prefs.getBoolean("dyslexic_mode", false);
        Log.d("DYSLEXIE", "Mode dyslexique lu: " + isDyslexicMode);

        if (isDyslexicMode) {
            Log.d("DYSLEXIE", "Tentative de chargement de la police");
            Typeface dyslexicFont = ResourcesCompat.getFont(this, Typeface.ITALIC);

            if (dyslexicFont == null) {
                Log.e("DYSLEXIE", "Échec du chargement de la police");
                return;
            }

            Log.d("DYSLEXIE", "Police chargée avec succès");
            View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

            if (rootView == null) {
                Log.e("DYSLEXIE", "RootView est null");
                return;
            }

            if (rootView instanceof ViewGroup) {
                Log.d("DYSLEXIE", "Application de la police aux vues");
                setFontToAllTextViews((ViewGroup) rootView, dyslexicFont);
            }
        } else {
            Log.d("DYSLEXIE", "Mode dyslexique désactivé - aucune action");
        }
    }
//    private void applyDyslexicFont() {
//        boolean isDyslexicMode = getSavedDyslexicMode();
//        Log.d("DyslexicMode", "Mode dyslexique: " + isDyslexicMode);
//
////        Typeface dyslexicFont = isDyslexicMode
////                ? ResourcesCompat.getFont(this, R.font.poppinsbold)
////                : Typeface.DEFAULT;
//
//        Typeface dyslexicFont = Typeface.MONOSPACE;
//        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        if (rootView instanceof ViewGroup) {
//            setFontToAllTextViews((ViewGroup) rootView, dyslexicFont);
//        }
//    }



    private void setFontToAllTextViews(ViewGroup parent, Typeface typeface) {
        if (parent == null || typeface == null) return;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTypeface(typeface);
            } else if (child instanceof ViewGroup) {
                setFontToAllTextViews((ViewGroup) child, typeface);
            }
        }
    }



    private boolean getSavedDyslexicMode() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getBoolean("dyslexic_mode", false);
    }

//    public int getSelectedNavItem() {
//        if (this instanceof AccueilActivity) return R.id.navigation_home;
//        if (this instanceof ExplorerActivity) return R.id.navigation_explore;
//        if (this instanceof DonUniqueActivity) return R.id.navigation_add;
//        if (this instanceof LoginActivity) return R.id.navigation_profile;
//        return R.id.navigation_home; // Par défaut, on met Accueil si on ne sait pas
//    }
}
