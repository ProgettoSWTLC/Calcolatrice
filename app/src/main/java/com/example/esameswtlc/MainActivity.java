package com.example.esameswtlc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final Boolean CLEAR = false;
    static final String HISTORY = "com.example.esameswtlc.HISTORY";

    // Riferimento alle text view della main activity
    private TextView screenView;
    private TextView fullOperationView;

    private Double r; // Contiene il risultato dell'operazione
    private Double x; // Contiene il primo elemento dell'operazione
    private Double y; // Contiene il secondo elemento dell'operazione
    private Double ans; //Contiene l'ultimo risultato ottenuto

    private String xString;
    private String yString;
    private String rString;
    private String ansString;

    private String operator; // Contiene l'operatore selezionato
    private Boolean firstNumber; // Indica se è stata inserito un valore e definisce se sono in un operazione
    private Boolean secondNumber;
    private Boolean isSpecial; // Parametro che definisce se il parametro inserito è speciale (ANS, PI..)


    private String fullOperationText;
    private String screenText;

    private ArrayList<String> history;

    private Boolean done;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.screenView = findViewById(R.id.screenView);
        this.fullOperationView = findViewById(R.id.fullOperationView);
        this.history = new ArrayList<>();
        this.init();
    }


    /**
     * Richiamata quando viene premuto un operatore (+ - x /)
     *
     * @param view
     */
    public void setOperator(View view) {

        // Se premo un'operatore dopo aver terminato un'operazione
        // Utilizzo l'ultimo risultato come primo elemento della
        // prossima operazione, e richiedo il secondo numero
        if(this.done) {
            this.init();
            // Memorizzo l'operatore
            this.operator = ((Button) view).getText().toString();
            this.x = this.ans;
            this.xString = this.ansString;
            this.firstNumber = true;
            this.updateViews(this.xString + " " + this.operator + " ", CLEAR);
            this.clearScreen();
        } else {
            /*
                Quando viene premuto un operatore, memorizzo il valore presente nello screen in x.
                Successivamente pulisco screenView e aggiorno la view fullOperationView
             */
            if(this.screenText.equals("-")){
                return;
            }

            // Gestione nel caso di input nullo
            if (this.screenText.equals("")) {

                String selectedOp = ((Button) view).getText().toString();

                // Consento di inserire il - come primo carattere per i numeri
                // negativi se l'input è nullo
                if(selectedOp.equals("-")) {
                    this.isSpecial = false;
                    if(!firstNumber) {
                        this.xString = selectedOp;
                    } else {
                        this.yString = selectedOp;
                    }
                    updateViews(selectedOp);
                    return;
                }

                // Se premo un operatore e l'input è nullo, allora costruisco la stringa fullOperation
                // in modo che venga aggiornato l'operatore senza modificare i valori
                if (this.firstNumber) {
                    this.operator = selectedOp;
                    this.fullOperationText = (this.xString + " " + this.operator + " " + this.yString);
                    this.fullOperationView.setText(this.fullOperationText);
                    return;
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
                // Memorizzo il primo valore inserito
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
    }

    /**
     * Elimina l'ultimo numero inserito
     *
     * @param view: Pulsante DEL
     */
    public void deleteDigit(View view) {
        if (this.done) {
            init();
        }
        // Se l'input è nullo, esco dalla funzione
        if(this.screenText.equals("")) { return; }

        // Tolgo l'ultimo elemento della stringa inserita
        if (!isSpecial) {
            this.screenText = this.screenText.substring(0, this.screenText.length() - 1);
        } else {
            // Se è stato premuta una cifra speciale cancella tutto
            this.screenText = "";
        }

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
        // Se ho appena terminato un'operazione e inserisco un numero,
        // inizializzo il tutto
        if (this.done) {
            this.init();
        }
        //Se è già stato inserito un carattere speciale cancellalo
        if(this.isSpecial){ deleteDigit(view); }

        // Ottengo il testo del pulsante premuto
        String input = ((Button) view).getText().toString();

        this.isSpecial = false;

        // Aggiorno le TextView
        this.updateViews(input);

    }

    /**
     * Funzionamento identico alla inputNumber solo che funziona per i tasti speciali
     *
     * @param view tasto premuto
     */
    public void inputSpecialCharacter(View view){
        // Se ho appena terminato un'operazione e inserisco un numero,
        // inizializzo il tutto
        if (this.done) {
            this.init();
        }
        // Ottengo il testo del pulsante premuto
        String input = ((Button) view).getText().toString();
        String value = "";

        // Pulisco lo schermo nel caso io abbia inserito altri numeri precedentemente
        this.isSpecial = true;
        deleteDigit(view);

        //smisto i vari casi di caratteri speciali
        if (input.equals("π")) {
            value = "3.14159265";
        } else if (input.equals("ANS")) {
            value = this.ansString;
        }
        // Aggiorno le TextView
        this.updateViews(value);
    }

    public void inputPoint(View view){
        if (this.done) {
            this.init();
        }

        if(!this.screenText.contains(".")){
            if(this.screenText.equals("") || this.screenText.equals("-")) {
                updateViews("0.");
            } else {
                updateViews(".");
            }
        }
    }

    /**
     * Al click della Textview apre la cronologia delle operazioni
     * @param view: fullOperationView
     */
    public void showHistory(View view) {
        Intent intent = new Intent(this, showHistory.class);
        intent.putExtra(HISTORY, history);
        startActivity(intent);
    }

    /**
     * Alla pressione dell'uguale il metodo restituisce l'operazione in base all'operatore inserito
     * Legge inoltre se è stato inserito un secondo numero
     * (NOTE al momento del commit:
     *      - non si assicura che io abbia inserito un numero
     *      - non resetta il primo numero
     *      - se si spamma l'operazione crea tanti uguali)
     * @param view
     */
    public void getResult(View view) {
        // Se è stato inserito solo un numero, oppure ripremo
        // uguale dopo aver eseguito un'operazione, esco
        if (!firstNumber || this.done) {
            return;
        }


        this.yString = this.screenView.getText().toString();
        //Mi assicuro che ci sia un contenuto nel secondo numero
        if (this.yString.compareTo("")==0){
            this.secondNumber = false;
            this.y = 0.0;
            this.yString = "0";
        } else {
            this.secondNumber = true;
            this.y = Double.parseDouble(this.yString);
        }

        boolean error = false;

        // Se sono stati inseriti entrambi i numeri, smisto l'operazione ad uno o due numeri
        if (secondNumber) {
            //smisto l'operazione da fare ad un solo operatore
            if ((operator.compareTo("×") == 0)) {
                this.r = this.x * this.y;
            } else if ((operator.compareTo("+") == 0)) {
                this.r = this.x + this.y;
            } else if ((operator.compareTo("-") == 0)) {
                this.r = this.x - this.y;
            } else if ((operator.compareTo("÷") == 0)) {
                if (this.y != 0) {
                    this.r = this.x / this.y;
                } else {
                    error = true;
                }
            }
        } else {
            //lista di operazioni con un solo numero
        }
        // se non si sono verificati errori di calcolo mostro il risultato e salvo in memoria l'operazione
        if (!error){
            this.rString = r.toString();
            this.fullOperationText = this.fullOperationText + " = ";
            this.screenText = "";
            updateViews(this.rString);
            this.ans = this.r;
            this.ansString = rString;
            newOperation();
        } else {
            updateViews("Error", false);
        }
    }

    /**
     * Metodo che dopo aver premuto uguale crea una nuova operazione e salva in cronologia la
     * vecchia
     */
    private void newOperation() {
        this.history.add(this.fullOperationText);
        this.done = true;
    }

    /**
     * Metodo per cambiare segno del numero attualmente inserito nella
     * @param view
     */
    public void changeSign(View view) {
        Double inputValue = 0.0;
        String input = screenText;
        if (!input.equals("") && !input.equals("-")) {
            inputValue = Double.parseDouble(input) * -1;
            input = inputValue.toString();
            this.isSpecial = true;
        } else if (input.equals("-")){
            input = "";
        } else {
            input = "-";
        }

        deleteDigit(view);

        this.isSpecial = false;

        if (!hasDecimal(inputValue)){
            // Dovuto al cast il programma crea in automatica .0 alla fine del numero se è intero, cancello tali caratteri
            input = input.split("\\.")[0];
        }

        this.updateViews(input);
    }

    // ___________________________________ Metodi utili ___________________________________

    /**
     *
     * @param value:    Numero da controllare se è intero oppure no
     * @return True  ->  Il numero ha un decimale
     *         False ->  Il numero è intero
     */
    public boolean hasDecimal(Double value){
        if ((value % 1)==0){
            return false;
        }
        return true;
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
        this.updateViews("", CLEAR);
        this.x = 0.0;
        this.y = 0.0;
        this.r = 0.0;
        this.firstNumber = false;
        this.secondNumber = false;
        this.isSpecial = false;
        this.operator = "";
        this.xString = "";
        this.yString = "";
        this.rString = "";
        this.done = false;
        if (this.ans == null) {
            this.ans = 0.0;
            this.ansString = "0.0";
        }

    }

    private void clearScreen() {
        this.screenText = "";
        this.screenView.setText("");
    }

}