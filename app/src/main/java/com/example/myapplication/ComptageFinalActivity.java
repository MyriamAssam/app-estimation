package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ComptageFinalActivity extends AppCompatActivity {

    private EditText editTextMainOeuvre, editTextAutresCouts;
    private TextView textViewResultat, textViewDetails, tv_main, tv_autre;
    private Button buttonCalculer, button_new;
    private ArrayList<String> selections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptage_final);


        editTextMainOeuvre = findViewById(R.id.editTextMainOeuvre);
        editTextAutresCouts = findViewById(R.id.editTextText3);
        textViewResultat = findViewById(R.id.textViewResultat);
        textViewDetails = findViewById(R.id.textViewDetails);
        tv_main = findViewById(R.id.tv_main);
        tv_autre = findViewById(R.id.textView7);
        buttonCalculer = findViewById(R.id.buttonCalculer);
        button_new = findViewById(R.id.button_new);
        ImageView imageViewShare = findViewById(R.id.imageViewShare);


        selections = getIntent().getStringArrayListExtra("selections");


        if (selections != null) {
            textViewDetails.setText(String.join("\n", selections));
        }


        button_new.setOnClickListener(v -> {
            Intent intent = new Intent(ComptageFinalActivity.this, MainActivity.class);
            startActivity(intent);
        });

        buttonCalculer.setOnClickListener(v -> calculerCoutTotal());

        imageViewShare.setOnClickListener(v -> partagerResultat());
    }


    private void partagerResultat() {
        String resultat = textViewResultat.getText().toString();
        String details = textViewDetails.getText().toString();
        String mainOeuvreText = editTextMainOeuvre.getText().toString();
        String autresCoutsText = editTextAutresCouts.getText().toString();
        String typeLogement = getIntent().getStringExtra("type_logement");

        if (resultat.isEmpty() || details.isEmpty()) {
            Toast.makeText(this, getString(R.string.share_no_result), Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append(getString(R.string.share_type_logement)).append(typeLogement).append("\n\n");
        message.append(getString(R.string.share_estimation)).append("\n\n")
                .append(details)
                .append("\n\n")
                .append(getString(R.string.share_total))
                .append(resultat);

        if (!mainOeuvreText.isEmpty()) {
            message.append("\n\n").append(getString(R.string.share_cout_main_oeuvre)).append(mainOeuvreText).append("$/m²");
        }

        if (!autresCoutsText.isEmpty()) {
            message.append("\n\n").append(getString(R.string.share_autres_couts)).append(autresCoutsText).append("$");
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, message.toString());

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)));
    }



    private void calculerCoutTotal() {
        String mainOeuvreText = editTextMainOeuvre.getText().toString();
        String autresCoutsText = editTextAutresCouts.getText().toString();
        double total = 0;

        if (!mainOeuvreText.isEmpty() && !autresCoutsText.isEmpty()) {
            double coutMainOeuvre = Double.parseDouble(mainOeuvreText);
            double autresCouts = Double.parseDouble(autresCoutsText);

            for (String selection : selections) {
                String[] parts = selection.split(" : ");
                String[] surfaceEtPrix = parts[1].split("m² à ");
                double surface = Double.parseDouble(surfaceEtPrix[0]);
                double prixMateriau = Double.parseDouble(surfaceEtPrix[1].replace("$/m²", "").trim());

                total += (prixMateriau * surface) + (coutMainOeuvre *  surface);
            }

            total += autresCouts;
            textViewResultat.setText("Total : " + total + "$");
        } else {
            textViewResultat.setText(getString(R.string.error_fill_fields));

        }
    }
}

