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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_don_recurrent, container, false);
        ((MainActivity) requireActivity()).applyTextSizeToFragment(rootView);

        Button btnUnique = rootView.findViewById(R.id.btn_unique);

        btnUnique.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new DonUniqueFragment())
                    .addToBackStack(null)
                    .commit();
        });

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.associations,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssociation.setAdapter(adapter);
    }
}