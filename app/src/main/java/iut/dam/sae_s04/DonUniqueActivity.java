package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class DonUniqueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_unique);

        RadioGroup radioGroup = findViewById(R.id.radio_group);
        AppCompatButton btnRecurrent = findViewById(R.id.btn_recurrent);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedAmount = selectedRadioButton.getText().toString();
            }
        });

        btnRecurrent.setOnClickListener(v -> {
            Intent intent = new Intent(DonUniqueActivity.this, DonRecurrentActivity.class);
            startActivity(intent);
        });
    }
}