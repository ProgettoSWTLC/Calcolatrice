package com.example.esameswtlc;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Settings {
    final static Boolean RAD = true;
    final static Boolean DEG = false;

    private Boolean angleMode;
    int colore;
    private ArrayList<String> setting;

    final String SETTING_FILE = "Settings.txt";

    public Settings(AppCompatActivity app){
        this.setting = new ArrayList<>();
        // leggo i parametri scritti sul file
        readFile(app);

        this.angleMode = Boolean.getBoolean(this.setting.get(0));
    }

    private void readFile(AppCompatActivity app){
        try {
            InputStream is = app.openFileInput(SETTING_FILE);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);

            String op = in.readLine();

            while(op != null){
                this.setting.add(op);
                op = in.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean getAngleMode(){
        return this.angleMode;
    }

    public void setAngleMode(Boolean mode, AppCompatActivity app) {
        this.angleMode = mode;
        this.setting.set(0,this.angleMode.toString());
        writeOnFile(app);
    }

    private void writeOnFile(AppCompatActivity app) {
        StringBuilder data = new StringBuilder();
        for(String operation : this.setting) {
            data.append(operation).append("\n");
        }
        this.writeFile(data.toString(), app);
    }

    private void writeFile(String text, AppCompatActivity app){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(app.openFileOutput(SETTING_FILE, Context.MODE_PRIVATE));
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
