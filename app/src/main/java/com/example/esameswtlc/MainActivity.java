package com.example.esameswtlc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final Boolean CLEAR = false;
    public static final String HISTORY = "com.example.esameswtlc.HISTORY";
    public static final int GET_OPERATION_CODE = 1;

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


    private Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new Settings();

        setContentView(R.layout.activity_main);

        this.screenView = findViewById(R.id.screenView);
        this.fullOperationView = findViewById(R.id.fullOperationView);
        this.history = new ArrayList<>();
        HistoryHandler.readHistoryFromFile(this.history);
        this.init();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.history = savedInstanceState.getStringArrayList("history");
        this.x = savedInstanceState.getDouble("x");
        this.y = savedInstanceState.getDouble("y");

        this.xString = savedInstanceState.getString("x_string");
        this.yString = savedInstanceState.getString("y_string");

        this.ans = savedInstanceState.getDouble("ans");
        this.ansString = savedInstanceState.getString("ans_string");

        this.operator = savedInstanceState.getString("operator");

        this.fullOperationText = savedInstanceState.getString("full_text");
        this.screenText = savedInstanceState.getString("screen_text");

        this.isSpecial = savedInstanceState.getBoolean("is_special");
        this.done = savedInstanceState.getBoolean("done");

        this.firstNumber = savedInstanceState.getBoolean("first_number");
        this.secondNumber = savedInstanceState.getBoolean("second_number");

        this.fullOperationView.setText(this.fullOperationText);
        this.screenView.setText(this.screenText);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putStringArrayList("history", this.history);
        savedInstanceState.putDouble("x", this.x);
        savedInstanceState.putDouble("y", this.y);
        savedInstanceState.putString("operator", this.operator);
        savedInstanceState.putString("x_string", this.xString);
        savedInstanceState.putString("y_string", this.yString);
        savedInstanceState.putDouble("ans", this.ans);
        savedInstanceState.putString("ans_string", this.ansString);
        savedInstanceState.putString("full_text", this.fullOperationView.getText().toString());
        savedInstanceState.putString("screen_text", this.screenView.getText().toString());
        savedInstanceState.putBoolean("is_special", this.isSpecial);
        savedInstanceState.putBoolean("done", this.done);
        savedInstanceState.putBoolean("first_number", this.firstNumber);
        savedInstanceState.putBoolean("second_number", this.secondNumber);


    }

    // Supporto alla cronologia
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // TODO Aggiornare le impostazioni ..


        if (requestCode == GET_OPERATION_CODE) {
            if (resultCode == showHistory.RESULT_CODE_GET_OPERATION) {
                // Ottengo l'operazione che è stata cliccata nell'activity showHistory
                String operation = data.getStringExtra(showHistory.GET_OPERATION);
                this.done = true;

                // Aggiorno la grafica
                this.updateViews(operation, CLEAR);

                // Ottengo i vari elementi dell'operazione cliccata
                String[] elements = operation.split("=");
                String resultString = elements[1].trim();

                this.screenView.setText(resultString);

                // Salvo il risultato in ans
                this.ansString = resultString;
                this.ans = Double.valueOf(resultString);

            } else if (resultCode == showHistory.DELETE_HISTORY_CODE) {
                this.history.clear();
                this.init();
                // Messaggio a schermo che conferma la cancellazione della cronologia
                Context context = getApplicationContext();
                CharSequence text = "History has been deleted";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    }


    /**
     * Richiamata quando viene premuto un operatore (+ - x /)
     *
     * @param view: Button che contiene un operatore
     */
    public void setOperator(View view) {

        // Se premo un'operatore dopo aver terminato un'operazione
        // Utilizzo l'ultimo risultato come primo elemento della
        // prossima operazione, e richiedo il secondo numero
        if(this.done) {
            this.init();
            // Memorizzo l'operatore
            this.operator = ((Button) view).getText().toString();
            // Se l'operatore è x^n lo rimpiazzo this.operator con ^
            this.operator = view.getId() == R.id.buttonPowerN ? "^" : this.operator;
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
                    // Se l'operatore è x^n lo rimpiazzo this.operator con ^
                    this.operator = view.getId() == R.id.buttonPowerN ? "^" : this.operator;
                    this.fullOperationText = (this.xString + " " + this.operator + " " + this.yString);
                    this.fullOperationView.setText(this.fullOperationText);
                    return;
                }

                return;
            }

            // Memorizzo l'operatore
            this.operator = ((Button) view).getText().toString();

            // Se l'operatore è x^n lo rimpiazzo this.operator con ^
            this.operator = view.getId() == R.id.buttonPowerN ? "^" : this.operator;
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
        switch (input) {
            case "π":
                value = Double.toString(Math.PI);
                break;
            case "ANS":
                value = this.ansString;
                break;
            case "e":
                value = Double.toString(Math.E);
                break;
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
        startActivityForResult(intent, GET_OPERATION_CODE);
    }

    /**
     * Alla pressione dell'uguale il metodo restituisce l'operazione in base all'operatore inserito
     * Legge inoltre se è stato inserito un secondo numero
     * (NOTE al momento del commit:
     *      - non si assicura che io abbia inserito un numero
     *      - non resetta il primo numero
     *      - se si spamma l'operazione crea tanti uguali)
     * @param view: Pulsante uguale
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
            /*this.secondNumber = false;
            this.y = 0.0;
            this.yString = "0";*/
            return;
        } else {
            this.secondNumber = true;
            this.y = Double.parseDouble(this.yString);
        }

        this.done = true;
        boolean error = false;

        // Se sono stati inseriti entrambi i numeri, smisto l'operazione ad uno o due numeri
        if (secondNumber) {
            //smisto l'operazione da fare ad un solo operatore
            if ((operator.compareTo("^") == 0)) {
                this.r = Math.pow(this.x, this.y);
            } else if ((operator.compareTo("×") == 0)) {
                this.r = this.x * this.y;
            } else if ((operator.compareTo("+") == 0)) {
                this.r = this.x + this.y;
            } else if ((operator.compareTo("-") == 0)) {
                this.r = this.x - this.y;
            } else if ((operator.compareTo("÷") == 0)) {
                if (this.y != 0) {
                    this.r = this.x / this.y;
                }else {
                    error = true;
                }

            } else {
                // L'operazione in corso non è gestita da questo metodo, perciò salva il secondo numero ed esce
                return;
            }
        }
        // se non si sono verificati errori di calcolo mostro il risultato e salvo in memoria l'operazione
        if (!error){
            this.rString = Func.formatOutput(this.r);

            this.fullOperationText = this.fullOperationText + " = ";
            this.screenText = "";

            updateViews(this.rString);

            this.ans = this.r;
            this.ansString = rString;

            newOperation();
        } else {
            updateViews("Error", CLEAR);
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
     * @param view: Button per cambiare segno dell'operando
     */
    public void changeSign(View view) {
        String input = screenText;

        if (input.equals("")){
            input = "-";
        } else if (input.charAt(0) == '-') {
            input = input.substring(1);
            this.isSpecial = true;
        } else {
            this.isSpecial = true;
            input = '-' + input;
        }

        deleteDigit(view);

        // è stato premuto il pulsante ans precedentemente e mantiene lo stato special
        // evita perciò che tolga lo stato special se si cambia di segno al ANS
        this.isSpecial = input.compareTo("-" + ansString) == 0 || input.compareTo(ansString) == 0;


        this.updateViews(input);
    }

    // TODO Questo direi di metterlo nella pagina delle impostazioni
    // Oppure no? Boh, cioè, è bello anche qui, ma poi la pagina delle impostazioni
    // rimane un po' vuota?
    public void changeAngle(View view) {
        this.settings.setAngleMode(!this.settings.getAngleMode());

        ((Button)view).setText(
                this.settings.getAngleMode() == Settings.DEG
                     ? "DEG"
                     : "RAD"
        );
    }


    // ---------------------------------- OPERATORI AD UN SOLO NUMERO ----------------------------------
    public void sqrt(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }
        this.done = true;
        if (this.x < 0){
            updateViews("Error", CLEAR);
        } else {
            this.r = Math.sqrt(this.x);
            this.rString = Func.formatOutput(this.r);

            this.fullOperationText = "√(" + this.xString + ") = ";
            //this.screenText = "";

            updateOneOperatorView(false);
        }

    }

    public void cbrt(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.r = Math.cbrt(this.x);
        this.rString = Func.formatOutput(this.r);

        this.fullOperationText = "³√(" + this.xString + ") = ";
        //this.screenText = "";
        this.done = true;
        updateOneOperatorView();

    }

    public void pow2(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.r = Math.pow(this.x,2);
        this.rString = Func.formatOutput(this.r);

        this.fullOperationText =  "("+ this.xString + ")² = ";
        //this.screenText = "";
        this.done = true;
        updateOneOperatorView();

    }

    public void pow3(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.r = Math.pow(this.x,3);
        this.rString = Func.formatOutput(this.r);

        this.fullOperationText =  "("+ this.xString + ")³ = ";
        //this.screenText = "";
        this.done = true;
        updateOneOperatorView();

    }

    public void tan(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.x = this.settings.getAngleMode() == Settings.DEG
                ? Math.toRadians(this.x) // se la modalità è DEG, converto l'input in radianti
                : this.x;

        this.r = Math.tan(this.x);

        this.rString = Func.formatOutput(this.r);

        this.fullOperationText = String.format("tan(%s) = ", this.xString);
        this.done = true;
        updateOneOperatorView();
    }

    public void cos(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.x = this.settings.getAngleMode() == Settings.DEG
               ? Math.toRadians(this.x) // se la modalità è DEG, converto l'input in radianti
               : this.x;

        this.r =  Math.cos(this.x);

        this.rString = Func.formatOutput(this.r);

        this.fullOperationText = String.format("cos(%s) = ", this.xString);
        this.done = true;
        updateOneOperatorView();
    }

    public void sin(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.x = this.settings.getAngleMode() == Settings.DEG
                ? Math.toRadians(this.x) // se la modalità è DEG, converto l'input in radianti
                : this.x;

        this.r = Math.sin(this.x);

        this.rString = Func.formatOutput(this.r);

        this.fullOperationText = String.format("sin(%s) = ", this.xString);
        this.done = true;
        updateOneOperatorView();
    }

    public void factorial(View  view){
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }
        boolean error = false;
        if (!Func.hasDecimal(this.x)){
            if (this.x > 171){
                error = true;
            } else {
                this.r = Double.parseDouble(String.valueOf(Func.factorialOp(this.x)));
            }
        } else {
            error = true;
        }

        this.done = true;
        if (!error) {
            this.rString = Func.formatOutput(this.r);

            this.fullOperationText = this.xString + "! = ";
            //this.screenText = "";

            updateOneOperatorView(true);

        } else {
            updateViews("Error", CLEAR);
        }

    }

    public void ln(View view){
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }
        this.r = Math.log(this.x);
        this.rString = Func.formatOutput(this.r);

        this.fullOperationText = String.format("ln(%s) = ", this.xString);
        this.done = true;
        updateOneOperatorView();
    }

    public void log(View view){
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }
        this.r = Math.log10(this.x);
        this.rString = Func.formatOutput(this.r);

        this.fullOperationText = String.format("log(%s) = ", this.xString);
        this.done = true;
        updateOneOperatorView();
    }

    public void ePowX(View view) {
        setOperator(view);
        if (!this.firstNumber) {
            return;
        }

        this.r = Math.pow(Math.E,this.x);
        this.rString = Func.formatOutput(this.r);

        this.fullOperationText =  "e^("+ this.xString + ") = ";
        //this.screenText = "";
        this.done = true;
        updateOneOperatorView();
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

    /**
     * Pulisce la textView screenView
     */
    private void clearScreen() {
        this.screenText = "";
        this.screenView.setText("");
    }

    /**
     * Richiamato per mostrare il risultato delle operazioni ad un solo parametro
     * @param newOp: Parametro opzionale che se false non esegue newOperation();
     */
    private void updateOneOperatorView(boolean... newOp){

        this.clearScreen();
        this.done = true;

        updateViews(this.rString);

        this.ans = this.r;
        this.ansString = rString;

        // nel caso passo false come parametro non eseguo newOperation
        if(newOp.length > 0){
            if (!newOp[0]){
                return;
            }
        }
        newOperation();
    }

}