package com.example.esameswtlc;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class HistoryHandler extends ArrayList<String>{

    public static final String HISTORY_FILE = "History.txt";

    public static void readHistoryFromFile(ArrayList<String> list) {
        String operation;
        try(
                FileReader historyFile = new FileReader(HISTORY_FILE);
                BufferedReader historyStream = new BufferedReader(historyFile);
        )
        {
            operation = historyStream.readLine();
            while (operation != null) {
                list.add(operation);
                operation = historyStream.readLine();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found\n");
        }
        catch (IOException e) {
            System.out.println("There was a problem reading the file");
        }
    }

    public HistoryHandler(ArrayList<String> historyIn) {
        super(historyIn);

        this.writeOnFile();
    }

    private void writeOnFile() {

        try (
            FileWriter historyFile = new FileWriter(HISTORY_FILE);
            PrintWriter historyWriter = new PrintWriter(historyFile);
            )
        {
            for(String operation : this) {
                historyWriter.println(operation);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was a problem writing the file");
        }
    }


    // Ritorna la lista al contrario
    public ArrayList<String> reverse() {
        ArrayList<String> reversed = new ArrayList<>(this.size());

        for(int i = this.size() - 1; i >= 0; i--) {
            reversed.add(this.get(i));
        }

        return reversed;
    }

}
