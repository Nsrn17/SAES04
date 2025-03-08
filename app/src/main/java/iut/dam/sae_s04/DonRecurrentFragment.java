package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

public class DonRecurrentFragment extends Fragment {

    private Spinner spinnerAssociation;


    public DonRecurrentFragment() {
        // Le constructeur par défaut
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_don_recurrent, container, false);
//        setContentView(R.layout.activity_don_recurrent);
//        setupBottomNavigation();
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        Button btnAnnuel = rootView.findViewById(R.id.btn_annuel);
        Button btnMensuel = rootView.findViewById(R.id.btn_mensuel);
       Button btnUnique = rootView.findViewById(R.id.btn_unique);

        btnAnnuel.setOnClickListener(v -> {
            btnAnnuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnAnnuel.setTextColor(getResources().getColor(android.R.color.white));
            btnMensuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnMensuel.setTextColor(getResources().getColor(R.color.blue));
        });

        btnMensuel.setOnClickListener(v -> {
            btnMensuel.setBackgroundResource(R.drawable.bouton_mensuel);
            btnMensuel.setTextColor(getResources().getColor(android.R.color.white));
            btnAnnuel.setBackgroundResource(R.drawable.bouton_annuel);
            btnAnnuel.setTextColor(getResources().getColor(R.color.blue));
        });
        btnUnique.setOnClickListener(v -> {
            // Passer à l'écran d'inscription (RegisterFragment ou autre)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DonUniqueFragment())  // Remplacer par le fragment d'inscription
                    .addToBackStack(null)  // Ajouter à la pile arrière
                    .commit();
        });
//        btnUnique.setOnClickListener(v -> {
//            Intent intent = new Intent(DonRecurrentActivity.this, DonUniqueActivity.class);
//            startActivity(intent);
//        });

//        Button walletButton = findViewById(R.id.wallet_button);
//        Button homeButton = findViewById(R.id.home_button);
//        Button addRingButton = findViewById(R.id.add_ring_button);
//        Button userButton = findViewById(R.id.user_button);
//        Button settingButton =findViewById(R.id.settings);
//        Button searchButton =findViewById(R.id.search);
//        navigateToActivity(searchButton, ExplorerActivity.class);
//
//        navigateToActivity(walletButton, ResumeActivity.class);
//        navigateToActivity(homeButton, AccueilActivity.class);
//        navigateToActivity(addRingButton, DonUniqueActivity.class);
//        navigateToActivity(userButton, LoginActivity.class);
//        navigateToActivity(settingButton, ParametresActivity.class);

        //setContentView(R.layout.activity_don_recurrent);

        spinnerAssociation = rootView.findViewById(R.id.spinner_association);

        remplirSpinnerAssociations();

        spinnerAssociation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAssociation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return rootView;
    }
    private void remplirSpinnerAssociations() {
        // Utiliser un ArrayAdapter avec un tableau de ressources (R.array.associations)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),  // Utiliser getActivity() dans un Fragment pour obtenir le contexte
                R.array.associations,  // Assurez-vous que R.array.associations existe dans res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);
    }
}