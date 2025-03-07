package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class DonUniqueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_unique);
        setupBottomNavigation();
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        if (bottomNavigationView != null) {
//            bottomNavigationView.setSelectedItemId(getSelectedNavItem());
//        }
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        AppCompatButton btnRecurrent = findViewById(R.id.btn_recurrent);
        Spinner spinnerAssociation = findViewById(R.id.spinner_association);

        AssociationData associationData = AssociationData.getInstance();
        List<Association> associations = associationData.getAssociations();

        ArrayAdapter<Association> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                associations
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);

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