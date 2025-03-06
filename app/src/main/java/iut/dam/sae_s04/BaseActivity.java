package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Méthode générique pour gérer la navigation
    protected void navigateToActivity(View button, final Class<?> targetActivity) {
        if (button != null) {
            button.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), targetActivity);
                v.getContext().startActivity(intent);
            });
        } else {
            throw new NullPointerException("Le bouton passé à navigateToActivity() est null !");
        }
    }

}
