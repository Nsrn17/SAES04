package iut.dam.sae_s04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final HashMap<TextView, Float> originalTextSizes = new HashMap<>(); // Stocke les tailles de base

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedColors();
        applyTextSizeToAllViews();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupBottomNavigation();

        // Charge le premier fragment par défaut
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new AccueilFragment())
                    .commit();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_home) {
                selectedFragment = new AccueilFragment();
            } else if (item.getItemId() == R.id.navigation_explore) {
                selectedFragment = new ExplorerFragment();
            }else if (item.getItemId() == R.id.navigation_add) {
                selectedFragment = new DonUniqueFragment();
            }else if (item.getItemId() == R.id.navigation_profile) {
                selectedFragment = new RegisterFragment();
            }else if (item.getItemId() == R.id.settings) {
                selectedFragment = new ParametresFragment();
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selectedFragment)
                        .commit();
            }

            return true;
        });
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
//    @Override
//    protected void onResume() {
//        super.onResume();
//        applyTextSizeToAllViews(); // Réapplique la mise à jour des textes
//    }
    public void applyTextSizeToFragment(View view) {
        float textSizeFactor = getSavedTextSizeFactor();
        updateTextViewSize((ViewGroup) view, textSizeFactor);
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

}
