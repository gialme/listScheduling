// File con diverse funzioni utilizzate nel progetto

import java.io.*;

public class Utilities {

    static final int DIM = 20;

    /*
    Questa funzione legge da un file di testo le operazioni da eseguire e restituisce sia un array
    dell'oggetto Operation che il numero di righe lette dal file
    */
    static int loadFromFile(Operation s[], String fileName) throws FileNotFoundException, IOException {
        // creo oggetto di tipo BufferedReader per poter leggere righe del file di configurazione
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int i = 0;

        while(true) {
            // leggo riga
            String line = reader.readLine();

            // se readLine restituisce null sono al termine del file (EOF)
            if (line == null) {
                // esco dal ciclo
                break;
            }

            // ignoro i commenti nel file (righe precedute da #) e righe vuote
            if (line.startsWith("#") || line.isEmpty()) {
                // passo a prossima iterazione
                continue;
            }

            // altrimenti carico riga letta nell'array parts
            String parts[] = line.split(" ");

            s[i] = new Operation();
            /*  ogni riga è composta da:
                parts[0]: u0
                parts[1]: := (non mi serve, quindi non lo considero)
                parts[2]: a
                parts[3]: +
                parts[4]: b */
            s[i].load(parts[0], parts[3], parts[2], parts[4]);

            // controllo se è stato saltato un numero in un'operazione
            if (i>0) {
                if(Utilities.validateNum(s[i-1], s[i]) == false) {
                    System.err.println("Error! Wrong sequence of operations!\nTerminating");
                    System.exit(1);
                    break;
                }
            }

            i++;

            // controllo se ho raggiunto limite dimensione dell'array
            if (i > DIM) {
                System.err.println("Error! Exceeded the limit of" + DIM + " operations!");
                break;
            }
        }

        reader.close();

        return i;
    }

    // funzione che calcola le priorità delle operazioni (b-t+1)
    static int[] mobilityFunc(Operation s[], int asap[], int alap[], int numOp) {
        int[] mobility = new int[numOp];
    
        for(int i=0; i<numOp; i++) {
            mobility[i] = alap[i] - asap[i] + 1;
        }
    
        return mobility;
    }

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

    // funzione che stampa tutti gli elementi di un array diversi da -1
    public static void printResource (int[] arr) {
        System.out.print(" [ ");
        for (int i=0; i<arr.length; i++) {
            if(arr[i] != -1) {
                System.out.print("u" +arr[i] + " ");
            }
        }
        System.out.print("]");
        System.out.println();
    }
}
