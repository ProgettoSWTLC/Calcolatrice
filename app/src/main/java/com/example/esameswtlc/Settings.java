package com.example.esameswtlc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Settings {
    final static Boolean RAD = true;
    final static Boolean DEG = false;

    private Boolean angleMode;
    int colore;

    final String FILE = "Settings.txt";

    public Settings(){
        // TODO leggere il file e assegnare i valori letti agli attributi
        this.angleMode = RAD;
    }

    public Boolean getAngleMode(){
        return this.angleMode;
    }

    public void setAngleMode(Boolean mode) {
        this.angleMode = mode;
        // TODO riscrivere il file
    }
}
