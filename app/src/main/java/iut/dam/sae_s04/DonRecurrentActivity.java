package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class DonRecurrentActivity extends BaseActivity {

    private Spinner spinnerAssociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_recurrent);

        Button btnAnnuel = findViewById(R.id.btn_annuel);
        Button btnMensuel = findViewById(R.id.btn_mensuel);
       Button btnUnique = findViewById(R.id.btn_unique);

        btnAnnuel.setOnClickListener(v -> {
            btnAnnuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnAnnuel.setTextColor(getResources().getColor(R.color.blue));
            btnMensuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnMensuel.setTextColor(getResources().getColor(R.color.white));
        });

        btnMensuel.setOnClickListener(v -> {
            btnMensuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnMensuel.setTextColor(getResources().getColor(android.R.color.white));
            btnAnnuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnAnnuel.setTextColor(getResources().getColor(R.color.blue));
        });

//        btnUnique.setOnClickListener(v -> {
//            Intent intent = new Intent(DonRecurrentActivity.this, AccueilActivity.class);
//            startActivity(intent);
//
//        });
        navigateToActivity(btnUnique , DonUniqueActivity.class);
        navigateToActivity(btnAnnuel , AccueilActivity.class);

        Button walletButton = findViewById(R.id.wallet_button);
        Button homeButton = findViewById(R.id.home_button);
        Button addRingButton = findViewById(R.id.add_ring_button);
        Button userButton = findViewById(R.id.user_button);
        Button settingButton =findViewById(R.id.settings);
        Button searchButton =findViewById(R.id.search);
        navigateToActivity(searchButton, ExplorerActivity.class);

        navigateToActivity(walletButton, ResumeActivity.class);
        navigateToActivity(homeButton, AccueilActivity.class);
        navigateToActivity(addRingButton, DonUniqueActivity.class);
        navigateToActivity(userButton, LoginActivity.class);
        navigateToActivity(settingButton, ParametresActivity.class);

        setContentView(R.layout.activity_don_recurrent);

        spinnerAssociation = findViewById(R.id.spinner_association);

        remplirSpinnerAssociations();

        spinnerAssociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAssociation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
    private void remplirSpinnerAssociations() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.associations,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);
    }
}