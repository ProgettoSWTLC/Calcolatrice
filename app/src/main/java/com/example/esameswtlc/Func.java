package com.example.esameswtlc;

public class Func {
    /**
     * Ricevuta un valore, verifica che non sia troppo grande e se è un valore intero, lo formatta
     * togliendo la parte decimale.
     *
     * @param output: valore numerico da formattare
     * @return output -> output convertito in stringa e formattato nel caso non sia eccessivamente
     *                   grande
     */
    public static String formatOutput(Double output){
        if(!hasDecimal(output) && output < 1e7){
            return output.toString().split("\\.")[0];
        }
        return output.toString();
    }

    /**
     *
     * @param value:    Numero da controllare se è intero oppure no
     * @return True  ->  Il numero ha un decimale
     *         False ->  Il numero è intero
     */
    public static boolean hasDecimal(Double value){
        return (value % 1) != 0;
    }

    /**
     * Calcola il fattoriale di un numero
     * @param x numero di cui calcolare il fattoriale
     * @return fattoriale di x
     */
    public static double factorialOp(Double x) {
        if (x == 0.0) {
            return 1;
        }
        double sign = x / Math.abs(x);
        double result = 1.0;
        for (int i = 2; i <= Math.abs(x); i++){
            result = result * i;
        }
        return result * sign;
    }

}
