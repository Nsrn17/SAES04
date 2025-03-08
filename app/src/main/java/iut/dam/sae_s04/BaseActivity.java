package iut.dam.sae_s04;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {
    private final HashMap<TextView, Float> originalTextSizes = new HashMap<>(); // Stocke les tailles de base

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedColors();
        super.onCreate(savedInstanceState);
        setupBottomNavigation();
    }

    public void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(item -> {

                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home ) {
                    navigateToActivity(AccueilActivity.class);
                    return true;
                } else if (itemId == R.id.navigation_explore ) {
                    navigateToActivity(ExplorerActivity.class);
                    return true;
                } else if (itemId == R.id.navigation_add ) {
                    navigateToActivity(DonUniqueActivity.class);
                    return true;
                } else if (itemId == R.id.navigation_profile ) {
                    navigateToActivity(LoginActivity.class);
                    return true;
                }

                return false;
            });
        }
    }

    public void navigateToActivity(final Class<?> destination) {
        Intent intent = new Intent(this, destination);
        startActivity(intent);
        finish();
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
//    public int getSelectedNavItem() {
//        if (this instanceof AccueilActivity) return R.id.navigation_home;
//        if (this instanceof ExplorerActivity) return R.id.navigation_explore;
//        if (this instanceof DonUniqueActivity) return R.id.navigation_add;
//        if (this instanceof LoginActivity) return R.id.navigation_profile;
//        return R.id.navigation_home; // Par défaut, on met Accueil si on ne sait pas
//    }
}
