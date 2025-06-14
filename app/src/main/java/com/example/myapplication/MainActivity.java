package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "LangPrefs";
    private static final String KEY_LANGUAGE = "language";
    private boolean isFrench; // Suivi de la langue actuelle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Charger la langue avant d'afficher l'UI
        loadLocale();

        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView3);
        Button buttonChangeLanguage = findViewById(R.id.buttonChangeLanguage);

        imageView.setOnClickListener(this::allerComptageActivity);

        buttonChangeLanguage.setOnClickListener(v -> {
            String newLang = isFrench ? "en" : "fr"; // Bascule entre les langues
            setLocale(newLang);
        });
    }

    public void allerComptageActivity(View view) {
        Intent intent = new Intent(MainActivity.this, ComptageActivity.class);
        startActivity(intent);
    }

    private void setLocale(String lang) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String currentLang = prefs.getString(KEY_LANGUAGE, "fr");

        if (currentLang.equals(lang)) {
            return; // Ne fait rien si la langue est déjà correcte
        }

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        // Sauvegarde la nouvelle langue et met à jour isFrench
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LANGUAGE, lang);
        editor.apply();

        isFrench = lang.equals("fr"); // Met à jour l'état de isFrench

        // Redémarrer proprement
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity(); // Ferme toutes les activités en arrière-plan

        Toast.makeText(this, getString(R.string.toast_language_changed), Toast.LENGTH_SHORT).show();
    }

    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String language = prefs.getString(KEY_LANGUAGE, "fr"); // Français par défaut
        isFrench = language.equals("fr"); // Initialise isFrench
        setLocale(language);
    }
}
