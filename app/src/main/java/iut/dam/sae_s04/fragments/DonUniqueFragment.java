package iut.dam.sae_s04.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import iut.dam.sae_s04.models.Admin;
import iut.dam.sae_s04.models.Association;
import iut.dam.sae_s04.models.AssociationData;
import iut.dam.sae_s04.models.User;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;
import iut.dam.sae_s04.utils.SessionManager;

public class DonUniqueFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private User currentUser;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_don_unique, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        dbHelper = new DatabaseHelper(getContext());

        // Récupération de l'utilisateur ou admin connecté
        currentUser = SessionManager.getCurrentUser(getContext(), dbHelper);
        if (currentUser == null) {
            Admin admin = SessionManager.getCurrentAdmin(getContext(), dbHelper);
            if (admin != null) {
                currentUser = new User(-2, admin.getUsername(), admin.getEmail(), admin.getName());
            }
        }

        TextView userStatus = rootView.findViewById(R.id.user_status);
        RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);
        AppCompatButton btnRecurrent = rootView.findViewById(R.id.btn_recurrent);
        AppCompatButton btnConfirm = rootView.findViewById(R.id.btn_confirm);
        Spinner spinnerAssociation = rootView.findViewById(R.id.spinner_association);
        EditText editCustomAmount = rootView.findViewById(R.id.edit_custom_amount);
        CheckBox checkAnonymous = rootView.findViewById(R.id.check_anonymous);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            editCustomAmount.setText("");
        });

        // Désélection automatique du radio button si l'utilisateur saisit un montant manuel
        editCustomAmount.setOnClickListener(v -> radioGroup.clearCheck());
        editCustomAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) radioGroup.clearCheck();
        });

        if (currentUser != null) {
            String label = currentUser.getId() == -2 ? "admin" : "utilisateur";
            userStatus.setText("Connecté en tant que " + label + " : " + currentUser.getUsername());
        } else {
            userStatus.setText("Vous êtes en mode invité (non connecté)");
        }

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

            if (montant <= 0) {
                Toast.makeText(getContext(), "Montant invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = (currentUser != null && !anonyme) ? currentUser.getId() : -1;

            InfosBancairesFragment infosBancairesFragment = new InfosBancairesFragment();
            Bundle args = new Bundle();
            args.putString("association", association);
            args.putDouble("montant", montant);
            args.putString("type", "unique");
            args.putBoolean("anonyme", anonyme);
            args.putInt("userId", userId);
            infosBancairesFragment.setArguments(args);

            args.putString("fragmentOrigine", "DonUniqueFragment");

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, infosBancairesFragment)
                    .addToBackStack(null)
                    .commit();
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
