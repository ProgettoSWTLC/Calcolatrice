package com.example.esameswtlc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final Boolean CLEAR = false;

    // Riferimento alle text view della main activity
    private TextView screenView;
    private TextView fullOperationView;

    private Double r; // Contiene il risultato dell'operazione
    private Double x; // Contiene il primo elemento dell'operazione
    private Double y; // Contiene il secondo elemento dell'operazione

    private String xString;
    private String yString;

    private String operator; // Contiene l'operatore selezionato
    private Boolean firstNumber;

    private String fullOperationText;
    private String screenText;

    private String[] history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.screenView = findViewById(R.id.screenView);
        this.fullOperationView = findViewById(R.id.fullOperationView);

        this.init();
    }


    /**
     * Richiamata quando viene premuto un operatore (+ - x /)
     *
     * @param view
     */
    public void setOperator(View view) {
        /*
            Quando viene premuto un operatore, memorizzo il valore presente nello screen in x.
            Successivamente pulisco screenView e aggiorno la view fullOperationView
         */

        // Esco nel caso non sia stato inserito nessun numero
        if (this.screenText.equals("")) {
            // Se premo un operatore e l'input è nullo, allora costruisco la stringa fullOperation
            // in modo che venga aggiornato l'operatore senza modificare i valori

            if (this.firstNumber) {
                // Memorizzo l'operatore
                this.operator = ((Button) view).getText().toString();
                this.fullOperationText = (this.xString + " " + this.operator + " " + this.yString);
                this.fullOperationView.setText(this.fullOperationText);
            }
            return;
        }

        // Memorizzo l'operatore
        this.operator = ((Button) view).getText().toString();

        if (this.firstNumber) {
            // Se è gia stato inserito un numero, allora aggiorno l'operatore
            // senza cambiare i valori gia inseriti

            this.yString = this.screenText;
            this.y = Double.valueOf(yString);
            // Ricostruisco la striga fullOperationText in modo che venga aggiornato solo
            // l'operatore e non vada a modificare i valori inseriti

            this.fullOperationText = (this.xString + " " + this.operator + " " + this.yString);
        } else {
            // Memorizzo l'operatore selezionato e il valore inserito
            String inputNumber = this.screenText;

            // Memorizzo il valore inserito nella variabile x
            this.x = Double.valueOf(inputNumber);

            // Aggiorno la fullOperationView
            this.updateViews(this.operator);

            // Pulisco lo screen
            this.clearScreen();

            // Se non è stato ancora stato selezionato un operatore
            this.firstNumber = true;
            this.xString = inputNumber;
            this.fullOperationText = (inputNumber + " " + this.operator + " ");
        }

        // Infine aggiorno la fullOperationView
        this.fullOperationView.setText(this.fullOperationText);

    }

    /**
     * Elimina l'ultimo numero inserito
     *
     * @param view: Pulsante DEL
     */
    public void deleteDigit(View view) {
        // Se l'input è nullo, esco dalla funzione
        if(this.screenText.equals("")) { return; }

        // Tolgo l'ultimo elemento della stringa inserita
        this.screenText = this.screenText.substring(0, this.screenText.length() - 1);

        // Salvo il contenuto di fullOperation
        String prevFull = this.fullOperationText;

        // Aggiorno lo screen
        this.updateViews(this.screenText, CLEAR);

        // Se sto cancellando il secondo elemento dell'operazione
        if (firstNumber) {
            String[] elements = prevFull.split(" ");
            this.fullOperationText = elements[0] + " " + elements[1] + " " + this.screenText;
            this.fullOperationView.setText(this.fullOperationText);
        }

    }

    /**
     * Pulisce ed inizializza tutte le variabili. Richamato quando viene premuto ClearAll.
     *
     * @param view: Pulsante clearAll
     */
    public void clearAll(View view) {
        this.init();
    }

    /**
     * Richiamato quando viene premuto un numero. Aggiorna le TV fullOperation e screen aggiungendo
     * in append il numero premuto.
     *
     * @param view: Componente che è stato premuto
     */
    public void inputNumber(View view) {
        // Ottengo il testo del pulsante premuto
        String input = ((Button) view).getText().toString();

        // Aggiorno le TextView
        this.updateViews(input);
    }


    /**
     * Al click della Textview apre la cronologia delle operazioni
     * @param view
     */
    public void showHistory(View view) {
        Intent intent = new Intent(this, showHistory.class);
        startActivity(intent);
    }


    // ___________________________________ Metodi utili ___________________________________

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
        this.updateViews("", CLEAR);
        this.x = 0.0;
        this.y = 0.0;
        this.r = 0.0;
        this.firstNumber = false;
        this.operator = "";
        this.xString = "";
        this.yString = "";
    }

    private void clearScreen() {
        this.screenText = "";
        this.screenView.setText("");
    }

}