package com.example.esameswtlc;

import android.content.Context;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/*
    Classe HistoryHandler

        Una classe che "estende" ArrayList<String>, quindi di fatto quando si crea un'instanza
    si crea un ArrayList contenente stringhe. La differenza tra questo e un normale ArrayList è
    che questa classe ha metodi apposta per la gestione della cronologia ad esempio quelli
    per scrivere e leggere il file. L'oggetto HistoryHandler viene istanziato solamente nella
    showHistory, ma viene utilizzato anche nella MainActivity per la lettura del file
    con un metodo statico, quindi senza bisogno di creare unnuovo oggetto.

    * FUNZIONALITÀ STATICHE
        Il metodo readHistoryFromFile appunto, viene richiamato nella onCreate della MainActivity;
    ovvero quando si apre l'app, oppure quando si passa da un'activity e l'altra si richiama per
    aggiornare la cronologia leggendola direttamente dal file (che è sempre aggiornato).
    Per questo motivo il metodo è statico, ovvero non necessita di creare un'istanza per essere
    richiamato perché quello che fa è semplicemente andare a leggere il file riga per riga e
    salvare tutto in un'array list passata come parametro (che non è altro che la history della
    MainActivity). Per leggere / scrivere file c'è bisogno di un oggetto AppCompactActivity che
    contiene il metodo openFileInput (non so a cosa serva, ma funziona xd).
    Per questo il secondo parametro "app" è di quel tipo e nella MainActivity viene passata tutta
    l'istanza (ovvero this) che estende AppCompactActivity.

    * CREAZIONE DI UN OGGETTO
        Quando invece si instanzia un oggetto HistoryHandler, (nella showHistory)
    al costruttore vengono passati due parametri che sono l'ArrayList contenente la cronologia e
    la famosa classe AppCompatActiviy (che in questo caso è l'istanza di showHistory).
    In questo caso ho creato un'attributo in cui salvarla "this.app" per poi utilizzarla in altri
    metodi.
        Il costruttore di HistoryHandler tramite super(historyIn) crea una nuova ArrayList partendo
    da quella gia esistente (ovvero quella passata come extras da MainActivity a showHistory)
    che viene passata come parametro nella creazione dell'oggetto nella showHistory.
        Fatto questo viene richiamato writeOnFile che non fa altro che sovrascrivere il file della
    cronologia con quella aggiornata.
        METODI UTILI
            ->  reverse() ritorna la cronologia in ordine invertito in modo che quando si va a creare
            il layout dinamico le ultime operazioni eseguite risultino le prime della lista.

            ->  clear() è un'override, perché la classe ArrayList ha gia un medoto chiamato clear che
            pulisce la lista, ma in questo caso quello che deve fare è pulire il file. Infatti
            l'ArrayList contenente tutta la cronologia viene pulito nell MainActivity se il resultCode
            è showHistory.DELETE_HISTORY_CODE.
*/

public class HistoryHandler extends ArrayList<String>{

    public static final String HISTORY_FILE = "History.txt";

    public static void readHistoryFromFile(ArrayList<String> list, AppCompatActivity app) {
        try {
            InputStream is = app.openFileInput(HISTORY_FILE);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);

            String op = in.readLine();

            while(op != null){
                list.add(op);
                op = in.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final AppCompatActivity app;

    public HistoryHandler(ArrayList<String> historyIn, AppCompatActivity appIn) {
        super(historyIn);
        this.app = appIn;

        // Salvo la cronologia sul file
        this.writeOnFile();
    }


    private void writeOnFile() {
        StringBuilder data = new StringBuilder();
        for(String operation : this) {
            data.append(operation).append("\n");
        }
        this.writeFile(data.toString());
    }


    // Ritorna la lista al contrario
    public ArrayList<String> reverse() {
        ArrayList<String> reversed = new ArrayList<>(this.size());

        for(int i = this.size() - 1; i >= 0; i--) {
            reversed.add(this.get(i));
        }

        return reversed;
    }

    @Override
    public void clear() {
        // Pulisco il file della cronologia
        this.writeFile("");
    }

    /**
     * Metodo per sovrascrivere il file
     * @param text Testo da sovrascrivere nel file
     */
    private void writeFile(String text){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(this.app.openFileOutput(HISTORY_FILE, Context.MODE_PRIVATE));
            osw.write(text);
            osw.flush();
            osw.close();
        }catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
