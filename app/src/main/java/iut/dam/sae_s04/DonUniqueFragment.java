package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;


import iut.dam.sae_s04.database.DatabaseHelper;

public class DonUniqueFragment extends Fragment {
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_don_unique, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);
        dbHelper = new DatabaseHelper(getContext());

        RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);
        AppCompatButton btnRecurrent = rootView.findViewById(R.id.btn_recurrent);
        AppCompatButton btnConfirm = rootView.findViewById(R.id.btn_confirm);
        Spinner spinnerAssociation = rootView.findViewById(R.id.spinner_association);
        EditText editCustomAmount = rootView.findViewById(R.id.edit_custom_amount);
        CheckBox checkAnonymous = rootView.findViewById(R.id.check_anonymous);

        ArrayAdapter<Association> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, AssociationData.getInstance().getAssociations());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);

        btnRecurrent.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DonRecurrentFragment())
                    .addToBackStack(null)
                    .commit();
        });


        btnConfirm.setOnClickListener(v -> {
            String association = spinnerAssociation.getSelectedItem().toString();
            double montant = getSelectedAmount(radioGroup, editCustomAmount);
            boolean anonyme = checkAnonymous.isChecked();

            int userId = 1;

            boolean success = dbHelper.enregistrerDon(
                    userId,
                    association,
                    montant,
                    "unique",
                    anonyme
            );
        });

        return rootView;
    }

    private double getSelectedAmount(RadioGroup radioGroup, EditText customAmount) {
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radio_5) return 5.0;
        if (selectedId == R.id.radio_10) return 10.0;
        if (selectedId == R.id.radio_20) return 20.0;

        try {
            String amountText = customAmount.getText().toString();
            if (!amountText.isEmpty()) {
                return Double.parseDouble(amountText);
            }
        } catch (NumberFormatException e) {
            return 0.0;
        }
        return 0.0;
    }
}
