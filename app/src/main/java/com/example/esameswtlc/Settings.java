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
    private Integer theme;
    private final ArrayList<String> setting;

    final String SETTING_FILE = "Settings.txt";

    private final AppCompatActivity app;

    public Settings(AppCompatActivity appIn){
        this.setting = new ArrayList<>();
        this.app = appIn;

        // Leggo i parametri scritti sul file
        readFile();

        // Se il file non esiste ne creo uno nuovo con le impostazione di default
        if (this.setting.size() < 1){
            writeFile("true\n0");
            this.setting.add("true"); // radianti
            this.setting.add("0");    // tema
        }

        this.angleMode = this.setting.get(0).equals(DEG.toString()) ? DEG : RAD;
        this.theme = Integer.valueOf(this.setting.get(1));
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

    public Integer getTheme() {
        return this.theme;
    }

    public void setTheme(int themeNumber) {
        this.theme = themeNumber;
        this.setting.set(1, this.theme.toString());
        updateSettingsFile();
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
