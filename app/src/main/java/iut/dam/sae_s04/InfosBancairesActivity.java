package iut.dam.sae_s04;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class InfosBancairesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_bancaires);

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
    }
}