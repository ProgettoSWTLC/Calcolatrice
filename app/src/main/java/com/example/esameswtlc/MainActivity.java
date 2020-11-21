package com.example.esameswtlc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Riferimento alle text view della main activity
    private TextView screenView;
    private TextView fullOperationView;

    private Double r; // Contiene il risultato dell'operazione
    private Double x; // Contiene il primo elemento dell'operazione
    private Double y; // Contiene il secondo elemento dell'operazione

    private String fullOperationText;
    private String screenText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.screenView = findViewById(R.id.screenView);
        this.fullOperationView = findViewById(R.id.fullOperationView);

        this.init();
    }

    /**
     * Pulisce ed inizializza tutte le variabili. Richamato quando viene premuto ClearAll.
     *
     * @param v: Pulsante clearAll
     */
    public void clearAll(View v) {
        this.init();
    }

    /**
     * Richiamato quando viene premuto un numero. Aggiorna le TV fullOperation e screen aggiungendo
     * in append il numero premuto.
     *
     * @param v: Componente che è stato premuto
     */
    public void inputNumber(View v) {
        // Ottengo il testo del pulsante premuto
        String input = ((Button) v).getText().toString();

        // Aggiorno le TextView
        this.updateViews(input);
    }

    /**
     * Metodo che, presa una stringa in input, aggiorna il contentuto delle TextView aggiungendo in
     * append una stringa passata come parametro.
     *
     * Se viene passato false come secondo parametro, allora tutto il contenuto delle TextView viene
     * rimpiazzato dalla striga passata come parametro.
     *
     * @param input: Stringa che viene aggiunta alle TextViews
     * @param append: Parametro opzionale. Se true  -> funzionamento in append
     *                                     Se false -> funzionamento rimpiazza
     */
    private void updateViews(String input, Boolean... append) {
        if (append.length > 0) {
            if(!append[0]){
                // Funzionameto in replace all
                this.fullOperationText = input;
                this.screenText = input;
            }
        }else{
            // Funzionamento in append
            this.fullOperationText += input;
            this.screenText += input;
        }

        this.fullOperationView.setText(this.fullOperationText);
        this.screenView.setText(this.screenText);
    }

    /**
     * Metodo per inizializzare variabili. Richiamato all'onCreate e quando viene premuto ClearAll.
     */
    private void init() {
        this.updateViews("", false);
        this.x = 0.0;
        this.y = 0.0;
        this.r = 0.0;
    }



}