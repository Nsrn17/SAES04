package iut.dam.sae_s04.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import iut.dam.sae_s04.R;
import iut.dam.sae_s04.activities.MainActivity;
import iut.dam.sae_s04.database.DatabaseHelper;

public class InfosBancairesFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private String association;
    private double montant;
    private String type;
    private boolean anonyme;
    private int userId;

    private EditText editCardHolder, editCardNumber, editCCV;
    private Spinner spinnerMonth, spinnerYear;
    private CheckBox checkSaveInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_infos_bancaires, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getContext());

        Bundle args = getArguments();
        if (args != null) {
            association = args.getString("association");
            montant = args.getDouble("montant", 0.0);
            type = args.getString("type");
            anonyme = args.getBoolean("anonyme", false);
            userId = args.getInt("userId", -1);

            if (userId == -1) {
                anonyme = true;
            }
        }

        editCardHolder = view.findViewById(R.id.edit_card_holder);
        editCardNumber = view.findViewById(R.id.edit_card_number);
        editCCV = view.findViewById(R.id.edit_ccv);
        spinnerMonth = view.findViewById(R.id.spinner_month);
        spinnerYear = view.findViewById(R.id.spinner_year);
        checkSaveInfo = view.findViewById(R.id.check_save_info);

        setupSpinners();

        Button btnValider = view.findViewById(R.id.btn_next);
        btnValider.setOnClickListener(v -> processPayment());

        AppCompatButton btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            String fragmentOrigine = getArguments() != null ? getArguments().getString("fragmentOrigine") : null;

            if ("DonUniqueFragment".equals(fragmentOrigine)) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DonUniqueFragment())
                        .commit();
            } else if ("DonRecurrentFragment".equals(fragmentOrigine)) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DonRecurrentFragment())
                        .commit();
            } else {
                // Cas par défaut (optionnel, retourne sur DonUnique par exemple)
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DonUniqueFragment())
                        .commit();
            }
        });

    }

    private void setupSpinners() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.format("%02d", i + 1);
        }
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, months);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[10];
        for (int i = 0; i < 10; i++) {
            years[i] = String.valueOf(currentYear + i);
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
    }

    private void processPayment() {
        String cardHolder = editCardHolder.getText().toString().trim();
        String cardNumber = editCardNumber.getText().toString().trim();
        String ccv = editCCV.getText().toString().trim();
        String month = spinnerMonth.getSelectedItem().toString();
        String year = spinnerYear.getSelectedItem().toString();

        if (TextUtils.isEmpty(cardHolder) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(ccv)) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardNumber.length() < 12 || cardNumber.length() > 19 || !cardNumber.matches("\\d+")) {
            Toast.makeText(getContext(), "Numéro de carte invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ccv.length() < 3 || ccv.length() > 4 || !ccv.matches("\\d+")) {
            Toast.makeText(getContext(), "Code de sécurité invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkSaveInfo.isChecked()) {
            dbHelper.savePaymentInfo(userId, cardHolder, cardNumber, month, year, ccv);
        }

        boolean success = dbHelper.enregistrerDon(userId, association, montant, type, anonyme);

        if (success) {
            Toast.makeText(getContext(), "Don enregistré avec succès !", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), MainActivity.class));
            requireActivity().finish();
        } else {
            Toast.makeText(getContext(), "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }
}