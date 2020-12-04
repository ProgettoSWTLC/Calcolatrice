package com.example.esameswtlc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    public static final int PURPLE_THEME_CODE = 0;
    public static final int GREEN_THEME_CODE = 1;
    public static final int RED_THEME_CODE = 2;

    private RadioGroup themeSelectorGroup;
    private Settings settings;
    private Integer currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.settings = new Settings(this);

        switch (this.settings.getTheme()){
            case 1:
                setTheme(R.style.Theme_Green);
                break;
            case 2:
                setTheme(R.style.Theme_Red);
                break;
            default:
                setTheme(R.style.Theme_Purple);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        this.themeSelectorGroup = findViewById(R.id.theme_selector);


        this.currentTheme = this.settings.getTheme(); // Ottengo il tema corrente

        switch (this.currentTheme) {
            case 1:
                this.checkButton(R.id.greenTheme);
                break;
            case 2:
                this.checkButton(R.id.redTheme);
                break;
            default:
                this.checkButton(R.id.purpleTheme);
        }
    }

    public void onThemeSelected(View view) {
        int id = this.themeSelectorGroup.getCheckedRadioButtonId();
        int selected = 0;

        if (id == R.id.purpleTheme){
            selected = 0;
        } else if (id == R.id.greenTheme){
            selected = 1;
        } else if (id == R.id.redTheme){
            selected = 2;
        }

        if (selected != this.currentTheme) {
            this.settings.setTheme(selected);
        }

        Intent resultIntent = new Intent();

        setResult(selected, resultIntent);
        finish();
    }

    private void checkButton(int idView) {
        ((RadioButton) findViewById(idView)).setChecked(true);
    }

}