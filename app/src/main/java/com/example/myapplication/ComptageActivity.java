package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class ComptageActivity extends AppCompatActivity {

    private ListView listViewMateriaux;
    private EditText editTextSurface, editTextPrix;
    private Button buttonAjouter, buttonSuivant;
    private TextView textViewSelection;
    private Spinner spinnerType, spinnerPieces;
    private ArrayList<String> selections = new ArrayList<>();
    private String pieceSelectionnee = "", materiauSelectionne = "", typeSelectionnee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptage);

        // Initialisation des vues
        spinnerPieces = findViewById(R.id.spinner_pieces);
        listViewMateriaux = findViewById(R.id.listView_materiaux);
        editTextSurface = findViewById(R.id.editTextSurface);
        editTextPrix = findViewById(R.id.editTextPrix);
        buttonAjouter = findViewById(R.id.buttonAjouter);
        buttonSuivant = findViewById(R.id.buttonSuivant);
        textViewSelection = findViewById(R.id.textViewSelection);
        spinnerType = findViewById(R.id.spinner_type);

        // Charger les types (Maison/Appartement) depuis strings.xml
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(
                this, R.array.types, android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelectionnee = getResources().getStringArray(R.array.types)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Charger les pièces depuis strings.xml
        ArrayAdapter<CharSequence> adapterPieces = ArrayAdapter.createFromResource(
                this, R.array.pieces, android.R.layout.simple_spinner_item);
        adapterPieces.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPieces.setAdapter(adapterPieces);

        // Gestion de la sélection des pièces
        spinnerPieces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceSelectionnee = getResources().getStringArray(R.array.pieces)[position];

                int materialArrayId;
                switch (position) {
                    case 0: materialArrayId = R.array.materials_kitchen; break;
                    case 1: materialArrayId = R.array.materials_bathroom; break;
                    case 2: materialArrayId = R.array.materials_living_room; break;
                    case 3: materialArrayId = R.array.materials_basement; break;
                    case 4: materialArrayId = R.array.materials_bedroom; break;
                    case 5: materialArrayId = R.array.materials_stairs; break;
                    case 6: materialArrayId = R.array.materials_exterior; break;
                    default: materialArrayId = R.array.materials_other; break;
                }

                // Mise à jour des matériaux dans la ListView
                String[] materials = getResources().getStringArray(materialArrayId);
                ArrayAdapter<String> adapterMateriaux = new ArrayAdapter<>(ComptageActivity.this,
                        android.R.layout.simple_list_item_single_choice, Arrays.asList(materials));
                listViewMateriaux.setAdapter(adapterMateriaux);
                listViewMateriaux.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Gestion de la sélection des matériaux
        listViewMateriaux.setOnItemClickListener((parent, view, position, id) -> {
            materiauSelectionne = (String) listViewMateriaux.getItemAtPosition(position);
        });

        // Bouton Ajouter
        buttonAjouter.setOnClickListener(v -> {
            String surface = editTextSurface.getText().toString();
            String prix = editTextPrix.getText().toString();

            if (!pieceSelectionnee.isEmpty() && !materiauSelectionne.isEmpty() && !surface.isEmpty() && !prix.isEmpty()) {
                String selection = pieceSelectionnee + " - " + materiauSelectionne + " : " + surface + "m² à " + prix + "$/m²";
                selections.add(selection);
                textViewSelection.setText(String.join("\n", selections));

                // Réinitialiser les champs
                editTextSurface.setText("");
                editTextPrix.setText("");
            } else {
                Toast.makeText(this, getString(R.string.toast_select_piece_material), Toast.LENGTH_SHORT).show();
            }
        });

        // Bouton Suivant
        buttonSuivant.setOnClickListener(v -> {
            if (selections.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_add_element), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ComptageActivity.this, ComptageFinalActivity.class);
            intent.putStringArrayListExtra("selections", selections);
            intent.putExtra("type_logement", typeSelectionnee);
            startActivity(intent);
        });
    }
}




