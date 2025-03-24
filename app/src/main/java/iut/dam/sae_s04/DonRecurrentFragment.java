package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.List;


import iut.dam.sae_s04.database.DatabaseHelper;

public class DonRecurrentFragment extends Fragment {
    private Spinner spinnerAssociation;
    private DatabaseHelper dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_don_recurrent, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);
        dbHelper = new DatabaseHelper(getContext());

        Button btnUnique = rootView.findViewById(R.id.btn_unique);
        Button btnConfirm = rootView.findViewById(R.id.btn_confirm);
        spinnerAssociation = rootView.findViewById(R.id.spinner_association);
        RadioGroup radioGroupMontant = rootView.findViewById(R.id.radio_group);
        RadioButton radioAnnuel = rootView.findViewById(R.id.radio_annuel);
        RadioButton radioMensuel = rootView.findViewById(R.id.radio_mensuel);
        EditText editCustomAmount = rootView.findViewById(R.id.edit_custom_amount);
        CheckBox checkAnonymous = rootView.findViewById(R.id.check_anonymous);

        remplirSpinnerAssociations();

        btnUnique.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DonUniqueFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnConfirm.setOnClickListener(v -> {
            if (!radioAnnuel.isChecked() && !radioMensuel.isChecked()) {
                Toast.makeText(getContext(), "Veuillez sélectionner un type de don", Toast.LENGTH_SHORT).show();
                return;
            }

            String typeDon = radioMensuel.isChecked() ? "mensuel" : "annuel";
            Association association = (Association) spinnerAssociation.getSelectedItem();
            double montant = getSelectedAmount(radioGroupMontant, editCustomAmount);
            boolean anonyme = checkAnonymous.isChecked();

            if (montant <= 0) {
                Toast.makeText(getContext(), "Veuillez entrer un montant valide", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = 1;
            boolean success = dbHelper.enregistrerDon(
                    userId,
                    association.toString(),
                    montant,
                    typeDon,
                    anonyme
            );

            if (success) {
                Toast.makeText(getContext(), "Don enregistré avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private void remplirSpinnerAssociations() {
        AssociationData associationData = AssociationData.getInstance();
        List<Association> associations = associationData.getAssociations();

        ArrayAdapter<Association> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                associations
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);
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
