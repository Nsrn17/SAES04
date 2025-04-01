package iut.dam.sae_s04.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.database.DatabaseHelper;

public class InfosBancairesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String association;
    private double montant;
    private String type;
    private boolean anonyme;
    private int userId;
    private EditText editCardHolder, editCardNumber, editCCV;
    private Spinner spinnerMonth, spinnerYear;
    private CheckBox checkSaveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bancaires);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        association = intent.getStringExtra("association");
        montant = intent.getDoubleExtra("montant", 0.0);
        type = intent.getStringExtra("type");
        anonyme = intent.getBooleanExtra("anonyme", false);
        userId = intent.getIntExtra("userId", -1);

        //Patch sécurité : si userId == -1 alors c'est forcément un don anonyme
        if (userId == -1) {
            anonyme = true;
        }


        // Initialisation des vues
        editCardHolder = findViewById(R.id.edit_card_holder);
        editCardNumber = findViewById(R.id.edit_card_number);
        editCCV = findViewById(R.id.edit_ccv);
        spinnerMonth = findViewById(R.id.spinner_month);
        spinnerYear = findViewById(R.id.spinner_year);
        checkSaveInfo = findViewById(R.id.check_save_info);

        setupSpinners();

        Button btnValider = findViewById(R.id.btn_next);
        btnValider.setOnClickListener(v -> processPayment());
    }

    @SuppressLint("DefaultLocale")
    private void setupSpinners() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
    }

    private void processPayment() {
        String cardHolder = editCardHolder.getText().toString().trim();
        String cardNumber = editCardNumber.getText().toString().trim();
        String ccv = editCCV.getText().toString().trim();
        String month = spinnerMonth.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();

        // Vérification des champs
        if (TextUtils.isEmpty(cardHolder) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(ccv)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardNumber.length() < 12 || cardNumber.length() > 19 || !cardNumber.matches("\\d+")) {
            Toast.makeText(this, "Numéro de carte invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ccv.length() < 3 || ccv.length() > 4 || !ccv.matches("\\d+")) {
            Toast.makeText(this, "Code de sécurité invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkSaveInfo.isChecked()) {
            dbHelper.savePaymentInfo(userId, cardHolder, cardNumber, month, year, ccv);
        }

        boolean success = dbHelper.enregistrerDon(userId, association, montant, type, anonyme);

        if (success) {
            Toast.makeText(this, "Don enregistré avec succès !", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }
}
