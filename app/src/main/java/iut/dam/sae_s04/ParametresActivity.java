package iut.dam.sae_s04;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


public class ParametresActivity extends AppCompatActivity {
    private Spinner spinnerDaltonien;
    private SeekBar seekBarTailleTexte;
    private Button btnAppliquer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        spinnerDaltonien = findViewById(R.id.spinner_daltonien);
        seekBarTailleTexte = findViewById(R.id.seekbar_taille_texte);
        btnAppliquer = findViewById(R.id.btn_appliquer);

        String[] modesDaltoniens = {"Normal", "Protanopie", "Deutéranopie", "Tritanopie"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modesDaltoniens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDaltonien.setAdapter(adapter);

        seekBarTailleTexte.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float tailleTexte = 16 + (progress * 4);
                applyTextSize(tailleTexte);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnAppliquer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyDaltonienMode(spinnerDaltonien.getSelectedItemPosition());
                applyTextSize(16 + (seekBarTailleTexte.getProgress() * 4));
            }
        });
    }

    private void applyDaltonienMode(int mode) {

        switch (mode) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            default: // coulzurs normales
                break;
        }
    }

    private void applyTextSize(float tailleTexte) {
    }
}