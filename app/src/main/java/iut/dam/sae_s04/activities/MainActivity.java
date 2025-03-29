package iut.dam.sae_s04.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

import iut.dam.sae_s04.R;
import iut.dam.sae_s04.fragments.AccueilFragment;
import iut.dam.sae_s04.fragments.DonUniqueFragment;
import iut.dam.sae_s04.fragments.ExplorerFragment;
import iut.dam.sae_s04.fragments.LoginFragment;
import iut.dam.sae_s04.fragments.ParametresFragment;
import iut.dam.sae_s04.fragments.RegisterFragment;
import iut.dam.sae_s04.fragments.ResumeFragment;
import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.models.User;

public class MainActivity extends AppCompatActivity {

    private final HashMap<TextView, Float> originalTextSizes = new HashMap<>();
    private static MainActivity instance;
    private User currentUser = null;
    private Admin currentAdmin = null;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        applySavedColors();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        applyTextSizeToAllViews();

        setupBottomNavigation();

        if (currentAdmin != null) {
            MainActivity.getInstance().setCurrentAdmin(currentAdmin);

            // Admin connecté → affiche directement ResumeFragment
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new ResumeFragment())
                        .commit();
            }
        } else {
            // Affichage AccueilFragment pour un utilisateur classique
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new AccueilFragment())
                        .commit();
            }
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
            } else if (item.getItemId() == R.id.navigation_add) {
                selectedFragment = new DonUniqueFragment();
            } else if (item.getItemId() == R.id.navigation_profile) {
                if (currentAdmin != null) {
                    selectedFragment = new ResumeFragment();
                } else {
                    selectedFragment = new LoginFragment();
                }
            } else if (item.getItemId() == R.id.settings) {
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

    public void setCurrentAdmin(Admin admin) {
        this.currentAdmin = admin;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
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

    public void applyTextSizeToFragment(View view) {
        float textSizeFactor = getSavedTextSizeFactor();
        updateTextViewSize((ViewGroup) view, textSizeFactor);
    }

    private void applyTextSizeToAllViews() {
        float textSizeFactor = getSavedTextSizeFactor();
        ViewGroup rootLayout = findViewById(android.R.id.content);
        if (rootLayout != null) {
            saveOriginalTextSizes(rootLayout);
            updateTextViewSize(rootLayout, textSizeFactor);
        }
    }

    private void saveOriginalTextSizes(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (!originalTextSizes.containsKey(textView)) {
                    originalTextSizes.put(textView, textView.getTextSize());
                }
            } else if (child instanceof ViewGroup) {
                saveOriginalTextSizes((ViewGroup) child);
            }
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
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        return prefs.getFloat("text_size_factor", 0);
    }
}
