package com.example.esameswtlc;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

    private AppCompatActivity app;

    public Settings(AppCompatActivity appIn){
        this.setting = new ArrayList<String>();
        this.app = appIn;
        // leggo i parametri scritti sul file
        readFile();
        this.angleMode = this.setting.get(0).equals(DEG.toString()) ? DEG : RAD;

    }

    private void readFile(){
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

    public void setAngleMode(Boolean mode) {
        this.angleMode = mode;
        this.setting.set(0,this.angleMode.toString());
        updateSettingsFile();
    }

    private void updateSettingsFile() {
        StringBuilder data = new StringBuilder();
        for(String operation : this.setting) {
            data.append(operation).append("\n");
        }
        this.writeFile(data.toString());
    }

    private void writeFile(String text){
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
