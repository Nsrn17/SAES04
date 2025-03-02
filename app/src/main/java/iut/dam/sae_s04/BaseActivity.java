package iut.dam.sae_s04;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.ViewGroup;
import android.widget.TextView;
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Méthode générique pour gérer la navigation
    protected void navigateToActivity(View button, final Class<?> targetActivity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseActivity.this, targetActivity);
                startActivity(intent);
            }
        });
    }

    protected void applyTextSizeToViews(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("ParametresUtilisateur", MODE_PRIVATE);
        int tailleTexteProgress = sharedPreferences.getInt("tailleTexteProgress", 2);
        float tailleTexte = 16 + (tailleTexteProgress * 4);

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyTextSizeToViews(viewGroup.getChildAt(i));
            }
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextSize(tailleTexte);
        }
    }
}
