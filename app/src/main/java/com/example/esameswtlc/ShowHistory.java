package com.example.esameswtlc;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import static com.example.esameswtlc.R.attr.fastScrollHorizontalTrackDrawable;

public class ShowHistory extends AppCompatActivity {

    public static final String GET_OPERATION = "com.example.esameswtlc.GET_OPERATION";
    public static final int RESULT_CODE_GET_OPERATION = 1;
    public static final int DELETE_HISTORY_CODE = 2;

    /*
        La classe HistoryHandler è un arraylist con metodi
        che permettono di poter salvare il contenuto su un file.
        Diciamo che è un'array list sotto steroidi..
     */
    private HistoryHandler history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Settings settings = new Settings(this);

        switch (settings.getTheme()){
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
        setContentView(R.layout.activity_show_history);
        Intent intent = getIntent();

        // Creo l'oggetto HistoryHandler partendo dall'ArrayList passato come extra
        // In questo modo this.history è un'ArrayList che contiene anche dei
        // metodi per il salvataggio e cancellazione di file.
        // Il secondo paramentro, "this" è perché per la gestione dei file serve quella roba di
        // AppCompatActivity, quindi, essendo showHistory (ovvero questa classe) un'estensione di
        // AppCompatActivity, passo direttamente tutta l'istanza.
        history = new HistoryHandler(intent.getStringArrayListExtra(MainActivity.HISTORY), this);

        // Creazione della cronologia in ordine inverso, cosi che
        // l'ultima operazione della eseguita sia la prima della lista
        for (String operation : history.reverse()) {
            MaterialButton newOperationView = new MaterialButton(this, null, R.attr.materialButtonOutlinedStyle);

            if (operation.length() > 0 && operation.compareTo("Error") != 0) {
                String[] elements;
                elements = operation.split(" = ");
                newOperationView.setText(String.format("%s = \n%s", elements[0], elements[1]));
            }

            // Parametri grafici del pulsante che contiene l'operazione
            LinearLayout.LayoutParams parametri = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            parametri.weight = 1.0f;
            parametri.gravity = Gravity.START;
            newOperationView.setLayoutParams(parametri);
            newOperationView.setPadding(10, 10, 10, 10);
            newOperationView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            newOperationView.setLines(2);
            newOperationView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            newOperationView.setHorizontallyScrolling(true);

            // Aggiungo event listener
            newOperationView.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(GET_OPERATION, operation);
                setResult(RESULT_CODE_GET_OPERATION, resultIntent);
                finish();
            });

            // Aggiungo il pulsante al layout
            ((LinearLayout)findViewById(R.id.historyContainer)).addView(newOperationView);
        }
    }

    public void deleteHistory(View view) {
        Intent resultIntent = new Intent();
        // Pulisco il file
        history.clear();
        setResult(DELETE_HISTORY_CODE, resultIntent);
        finish();
    }
}