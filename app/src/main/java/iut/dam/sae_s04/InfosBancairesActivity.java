package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import iut.dam.sae_s04.database.DatabaseHelper;

public class InfosBancairesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bancaires);

        dbHelper = new DatabaseHelper(this);

        Spinner spinnerMonth = findViewById(R.id.spinner_month);
        Spinner spinnerYear = findViewById(R.id.spinner_year);

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this, R.array.months, R.layout.custom_spinner_item);
        monthAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerMonth.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(
                this, R.array.years, R.layout.custom_spinner_item);
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        spinnerYear.setAdapter(yearAdapter);

        findViewById(R.id.btn_next).setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        EditText cardHolder = findViewById(R.id.edit_card_holder);
        EditText cardNumber = findViewById(R.id.edit_card_number);
        Spinner month = findViewById(R.id.spinner_month);
        Spinner year = findViewById(R.id.spinner_year);
        EditText ccv = findViewById(R.id.edit_ccv);
        CheckBox saveInfo = findViewById(R.id.check_save_info);

        if (saveInfo.isChecked()) {
            int userId = 1;
            dbHelper.savePaymentInfo(
                    userId,
                    cardHolder.getText().toString(),
                    cardNumber.getText().toString(),
                    month.getSelectedItem().toString(),
                    year.getSelectedItem().toString(),
                    ccv.getText().toString()
            );
        }
    }
}