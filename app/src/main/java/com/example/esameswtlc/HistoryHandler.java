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
