package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class DonRecurrentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_recurrent);

        AppCompatButton btnAnnuel = findViewById(R.id.btn_annuel);
        AppCompatButton btnMensuel = findViewById(R.id.btn_mensuel);
        AppCompatButton btnUnique = findViewById(R.id.btn_unique);

        btnAnnuel.setOnClickListener(v -> {
            btnAnnuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnAnnuel.setTextColor(getResources().getColor(android.R.color.white));
            btnMensuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnMensuel.setTextColor(getResources().getColor(R.color.blue));
        });

        btnMensuel.setOnClickListener(v -> {
            btnMensuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnMensuel.setTextColor(getResources().getColor(android.R.color.white));
            btnAnnuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnAnnuel.setTextColor(getResources().getColor(R.color.blue));
        });

        btnUnique.setOnClickListener(v -> {
            Intent intent = new Intent(DonRecurrentActivity.this, DonUniqueActivity.class);
            startActivity(intent);
        });
    }
}