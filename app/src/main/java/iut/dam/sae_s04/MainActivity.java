package iut.dam.sae_s04;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//
//            if (itemId == R.id.navigation_home) {
//                // Charger le fragment ou l'activité Accueil
//                return true;
//            } else if (itemId == R.id.navigation_explore) {
//                // Charger le fragment ou l'activité Explorer
//
//                return true;
//            } else if (itemId == R.id.navigation_add) {
//                // Charger le fragment ou l'activité Ajouter
//
//                return true;
//            } else if (itemId == R.id.navigation_profile) {
//                // Charger le fragment ou l'activité Profil
//
//                return true;
//            }
//
//            return false;
//        });
    }
}