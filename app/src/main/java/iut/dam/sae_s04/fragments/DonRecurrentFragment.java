package iut.dam.sae_s04.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.InfosBancairesActivity;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.models.Association;
import iut.dam.sae_s04.models.AssociationData;
import iut.dam.sae_s04.models.User;
import iut.dam.sae_s04.utils.SessionManager;

public class DonRecurrentFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_don_recurrent, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        dbHelper = new DatabaseHelper(getContext());

        // Récupération de l'utilisateur ou admin
        currentUser = SessionManager.getCurrentUser(getContext(), dbHelper);
        if (currentUser == null) {
            Admin admin = SessionManager.getCurrentAdmin(getContext(), dbHelper);
            if (admin != null) {
                currentUser = new User(-2, admin.getUsername(), admin.getEmail(), admin.getName());
            }
        }

        Spinner spinnerAssociation = rootView.findViewById(R.id.spinner_association);
        EditText editCustomAmount = rootView.findViewById(R.id.edit_custom_amount);
        RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            editCustomAmount.setText("");
        });
        RadioGroup radioGroupFrequence = rootView.findViewById(R.id.radio_annuel).getParent() instanceof RadioGroup ?
                (RadioGroup) rootView.findViewById(R.id.radio_annuel).getParent() : null;
        CheckBox checkAnonymous = rootView.findViewById(R.id.check_anonymous);
        AppCompatButton btnConfirm = rootView.findViewById(R.id.btn_confirm);
        AppCompatButton btnUnique = rootView.findViewById(R.id.btn_unique);

        editCustomAmount.setOnClickListener(v -> radioGroup.clearCheck());
        editCustomAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) radioGroup.clearCheck();
        });

        ArrayAdapter<Association> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, AssociationData.getInstance().getAssociations());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);

        btnUnique.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DonUniqueFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnConfirm.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(getContext(), "Veuillez vous connecter", Toast.LENGTH_SHORT).show();
                return;
            }

            String association = spinnerAssociation.getSelectedItem().toString();
            double montant = getSelectedAmount(radioGroup, editCustomAmount);
            boolean anonyme = checkAnonymous.isChecked();
            String frequence = getSelectedFrequence(radioGroupFrequence);

            if (montant <= 0 || frequence == null) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = anonyme ? -1 : currentUser.getId();

            Intent intent = new Intent(getActivity(), InfosBancairesActivity.class);
            intent.putExtra("association", association);
            intent.putExtra("montant", montant);
            intent.putExtra("type", "recurrent_" + frequence);
            intent.putExtra("anonyme", anonyme);
            intent.putExtra("userId", userId);
            startActivity(intent);
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

    private String getSelectedFrequence(RadioGroup radioGroupFrequence) {
        if (radioGroupFrequence == null) return null;

        int selectedId = radioGroupFrequence.getCheckedRadioButtonId();
        if (selectedId == R.id.radio_mensuel) return "mensuel";
        if (selectedId == R.id.radio_annuel) return "annuel";

        return null;
    }
}
