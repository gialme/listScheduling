public class Utilities {

    // restituisce la posizione di value nell'array
    public static int getArrayIndex(int[] array, int value) {
        for(int i=0; i<array.length; i++)
            if(array[i]==value) 
                return i;
        
        // se non lo trova restituisce -1
        return -1;
    }

    // restituisce il valore massimo di un array
    public static int maxValue(int[] array) {
        int max = 0;

        for(int i=0; i<array.length; i++) {
            if (array[i]>max) {
                max = array[i];
            }
        }

        return max;
    }

    public static boolean isPresent(int[] array, int value) {
        for (int i=0; i<array.length; i++){
            if (array[i] == value) {
                return true;
            }
        }

        return false;
    }

    // funzione per controllare che le operazioni siano inserite nell'ordine corretto
    public static boolean validateNum(Operation sPrev, Operation sNext) {
        if ((sPrev.getNumber()+1) == sNext.getNumber()) {
            return true;
        }

        else {
            return false;
        }
    }
}
