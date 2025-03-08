package iut.dam.sae_s04;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import java.util.List;

public class DonUniqueActivity extends Fragment {

    public DonUniqueActivity() {
        // Le constructeur par défaut
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Gonfler le layout du fragment
        View rootView = inflater.inflate(R.layout.activity_don_unique, container, false);

        // Initialisation des vues
        RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);
        AppCompatButton btnRecurrent = rootView.findViewById(R.id.btn_recurrent);
        Spinner spinnerAssociation = rootView.findViewById(R.id.spinner_association);

        // Obtenir les associations
        AssociationData associationData = AssociationData.getInstance();
        List<Association> associations = associationData.getAssociations();

        // Adapter pour le spinner
        ArrayAdapter<Association> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, associations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);

        // Listener pour le groupe de radio boutons
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = rootView.findViewById(checkedId);
            String selectedAmount = selectedRadioButton.getText().toString();
        });

        // Listener pour le bouton de don récurrent
        btnRecurrent.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DonRecurrentActivity())  // Remplacer par le fragment d'inscription
                    .addToBackStack(null)  // Ajouter à la pile arrière
                    .commit();
        });

        return rootView;  // Retourner la vue gonflée
    }
}
