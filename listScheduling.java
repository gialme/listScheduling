/*
List-based scheduling implementation in Java
Author: Enrico Talmelli
*/

import java.io.*;
import java.util.*;

public class listScheduling {

    // dimensione massima di operazioni nello scheduling
    static final int DIM = 20;

    // Scheduling ASAP
    static int[] ASAP(Operation s[], int num_op) {
        int[] done = new int[num_op];
        int[] temp = new int[num_op];
        int clk;

        // inizializzo elenco operazioni eseguite a -1
        Arrays.fill(done, -1);
        Arrays.fill(temp, -1);

        for (clk=1; clk<=num_op; clk++) {
            for (int i=0; i<num_op; i++) {
                if (done[i] != -1) {
                    // operazione già eseguita in un altro ciclo di clock.
                    // passo a prossima operazione
                    continue;
                }

                int index1 = s[i].getIndex1();
                int index2 = s[i].getIndex2();

                // entrambi gli operandi sono variabili di ingresso, quindi si può eseguire
                if((index1 == -1) && (index2 == -1)) {
                    temp[i] = clk;
                    continue;
                }

                // operando1 e operando2 sono già stati eseguiti entrambi
                if((index1 != -1) && (done[index1] != -1)) {
                    if((index2 != -1) && (done[index2] != -1)) {
                        temp[i] = clk;
                        continue;
                    }

                    // operando1 è già stato eseguito, mentre operando2 è una variabile di ingresso
                    if (index2 == -1) {
                        temp[i] = clk;
                        continue;
                    }
                }

                // operando1 è una variabile di ingresso, mentre operando2 è già stato eseguito
                if ((index2 != -1) && (done[index2] != -1)) {
                    if (index1 == -1) {
                        temp[i] = clk;
                        continue;
                    }
                }
            }

            if(Arrays.equals(done, temp)) {
                // nell'ultimo ciclo di clock non ci sono stati aggiornamenti quindi esco
                clk--;
                break;
            }

            // aggiorno le operazioni eseguite solo alla fine di ogni ciclo di clock
            done = Arrays.copyOf(temp, temp.length);
        }

        return done;
    }

    // Scheduling ALAP
    static int[] ALAP(Operation s[], int asapScheduling[], int numOp, int clkMax) {
        int[] done = new int[numOp];
        int[] temp = new int[numOp];

        // inizializzo elenco operazioni eseguite a -1
        Arrays.fill(done, -1);

        // cerco la posizione dell'ultimo elemento dello scheduling ASAP
        int pos = Utilities.getArrayIndex(asapScheduling, clkMax);

        // l'ultimo elemento dell'ASAP è anche l'ultimo dell'ALAP
        done[pos] = clkMax;
        
        // array temporaneo
        temp = Arrays.copyOf(done, done.length);

        // parto con clk-1 procedendo a ritroso
        for (int clk = clkMax - 1; clk>0; clk--) {
            for (int i = 0; i<numOp; i++) {
                // cerco gli elementi già programmati dallo scheduler
                // (quelli non ancora programmati sono ancora a -1 nell'array)
                if(done[i] == clk + 1) {
                    // prendo gli indici dei 2 addendi
                    int index1 = s[i].getIndex1();
                    int index2 = s[i].getIndex2();

                    if(index1 != -1){
                        // se gli operandi non sono variabili di ingresso, li programmo al corrente ciclo di clock
                        temp[index1] = clk;
                    }

                    if(index2 != -1) {
                        temp[index2] = clk;
                    }
                }
            }

            // aggiorno array con le operazioni programmate
            done = Arrays.copyOf(temp, temp.length);
        }

        return done;
    }

    static int[] priorityScheduling(Operation s[], int numOp, int asap[], int[] priority, int nAdder, int nMolt) {
        int[] ready = new int[numOp];
        int[] temp = new int[numOp];

        int[] scheduling = new int[numOp];

        int[] allDone = new int[numOp];

        // inizializzo elenco operazioni eseguite a -1
        Arrays.fill(ready, 0);
        Arrays.fill(temp, 0);

        Arrays.fill(scheduling, -1);

        // riempio vettore allDone tutto di 2
        // mi servirà dopo per confrontarlo con ready e vedere se ho già eseguito tutte le operazioni
        Arrays.fill(allDone, 2);

        // creo array con le somme da eseguire a ogni ciclo di clock
        int add[] = new int[nAdder];

        // creo stesso array per le moltiplicazioni
        int molt[] = new int[nMolt];

        // vettore done (temp) con i valori:
        // 0 se l'operazione non è pronta
        // 1 se l'operazione è pronta per essere eseguita
        // 2 se è già stata eseguita

        System.out.println("\n");
        System.out.println("#########################");
        System.out.println("##   List scheduling   ##");
        System.out.println("#########################");

        for(int clk=1; clk<=numOp; clk++) {
            // all'inizio di ogni ciclo controllo le operazioni pronte
            // operazione pronta se ingressi = -1 (costanti)
            // oppure se ingressi = 2 (già eseguite)
            for(int i=0; i<numOp; i++) {
                if(ready[i] != 0) {
                    // operazione già pronta o già eseguita
                    // passo a operazione successiva
                    continue;
                }

                int index1 = s[i].getIndex1();
                int index2 = s[i].getIndex2();

                // se indici dell'operazione sono entrambi -1 allora può essere eseguita subito
                if ((index1 == -1) && (index2 == -1)){
                    temp[i] = 1;
                    continue;
                }

                // se un l'indice di un ingresso è -1 e l'altro ingresso è già stato eseguito (2 nel vettore ready)
                // allora l'operazione diventa pronta per l'esecuzione
                if((index1 != -1) && (ready[index1] == 2)) {
                    if(index2 == -1) {
                        temp[i] = 1;
                        continue;
                    }

                    if ((index2 != -1) && (ready[index2] == 2)) {
                        temp[i] = 1;
                        continue;
                    }
                }

                if((index2 != -1) && (ready[index2] == 2)) {
                    if(index1 == -1) {
                        temp[i] = 1;
                        continue;
                    }
                }
            }

            // aggiorno vettore ready
            ready = Arrays.copyOf(temp, temp.length);

            System.out.println("clk: " + clk);
            System.out.println("Ready operations: " + Arrays.toString(ready));

            // a ogni ciclo di clock azzero i vettori delle operazioni da eseguire a -1
            Arrays.fill(add, -1);
            Arrays.fill(molt, -1);
            
            
            // cerco tra tutte le somme quelle già pronte e le metto nel vettore add[]
            for (int i=0; i<nAdder; i++) {
                for (int j=0; j<numOp; j++) {
                    if ((s[j].getType().equals("+")) && (ready[j] == 1)) {
                        // se condizione vera allora è una somma pronta all'esecuzione

                        // ora devo confrontare la sua priorità con le altre

                        // se l'operazione è già presente nel vettore vado avanti
                        if (Utilities.isPresent(add, j)) {
                            continue;
                        }

                        else if (add[i] == -1) {
                            // al momento è vuoto quindi si può assegnare la somma j
                            add[i] = j;
                        }

                        // se la nuova somma ha priorità maggiore di quella già assegnata faccio un cambio
                        // altrimenti lascio l'operazione vecchia
                        else if (priority[add[i]] < priority[j]) {
                                add[i] = j;
                        }
                    }
                }
            }

            // stampo vettore add
            // non voglio stampare gli elementi vuoti del vettore
            System.out.print("adder: ");
            Utilities.printResource(add);

            // al termine devo segnare sul vettore ready le operazioni che ho eseguito
            for (int i=0; i<nAdder; i++) {
                if (add[i] != -1) {
                    temp[add[i]] = 2;
                    // e segno nel vettore scheduling a quale ciclo di clock le ho eseguite
                    scheduling[add[i]] = clk;
                }
            }


            // ora cerco tra le moltiplicazioni quelle già pronte nella stessa maniera
            for (int i=0; i<nMolt; i++) {
                for (int j=0; j<numOp; j++) {
                    if ((s[j].getType().equals("*")) && (ready[j] == 1)) {
                        // moltiplicazione pronta all'esecuzione

                        // se l'operazione è già presente nel vettore vado avanti
                        if (Utilities.isPresent(molt, j)) {
                            continue;
                        }

                        // cella vuota quindi posso assegnare la moltiplicazione j
                        else if (molt[i] == -1) {
                            molt[i] = j;
                        }

                        // guardo le priorità
                        else if (priority[molt[i]] < priority[j]) {
                            molt[i] = j;
                        }
                    }
                }
            }

            // stampo vettore molt
            // non voglio stampare gli elementi vuoti del vettore
            System.out.print("molt: ");
            Utilities.printResource(molt);

            // segno le moltiplicazioni eseguite sul vettore ready
            for (int i=0; i<nMolt; i++) {
                if(molt[i] != -1) {
                    temp[molt[i]] = 2;
                    scheduling[molt[i]] = clk;
                }
            }

            System.out.println();


            // aggiorno di nuovo vettore ready
            ready = Arrays.copyOf(temp, temp.length);


            // se tutti gli elementi del vettore sono 2 allora sono state eseguite
            // tutte le operazioni e posso uscire dal ciclo
            if(Arrays.equals(temp, allDone) == true) {
                break;
            }
            
        }

        return scheduling;
    }


    /* args[0]: numero di adder
       args[1]: numero di multiplier */

    public static void main(String[] args) {
        // start timer
        long startTime = System.currentTimeMillis();

        Operation[] list = new Operation[DIM];
        String fileName = "config.txt";
        int numOp;
        

        // controllo degli argomenti
        if (args.length != 2) {
            System.err.println("Error! Usage: java listBased N_adder N_multiplier");
            System.exit(1);
        }

        else if (Integer.parseInt(args[0]) <= 0) {
            System.err.println("Error! N_adder must be at least 1!");
            System.exit(1);
        }
        
        else if (Integer.parseInt(args[1]) <= 0) {
            System.err.println("Error! N_mult must be at least 1!");
            System.exit(1);
        }

        try {
            numOp = Utilities.loadFromFile(list, fileName);

            // stampo operazioni lette dal file
            System.out.println("\nImported " + numOp + " operations");

            /*for (int i = 0; i < numOp; i++) {
                System.out.println(list[i].toString());
            }*/

            // dichiaro array asapScheduling
            int[] asapScheduling = new int[numOp];

            asapScheduling = ASAP(list, numOp);

            System.out.println("\nASAP scheduling:");
            System.out.println(Arrays.toString(asapScheduling));
            System.out.println("Clock cycles: " + Utilities.maxValue(asapScheduling));

            // dichiaro array alapScheduling
            int[] alapScheduling = new int[numOp];

            alapScheduling = ALAP(list, asapScheduling, numOp, Utilities.maxValue(asapScheduling));

            System.out.println("\nALAP scheduling:");
            System.out.println(Arrays.toString(alapScheduling));
            System.out.println("Clock cycles: " + Utilities.maxValue(alapScheduling));

            // dichiato array con lista delle priorità delle operazioni
            int[] priority = new int[numOp];
            priority = Utilities.mobilityFunc(list, asapScheduling, alapScheduling, numOp);

            System.out.println("\nPriority list:");
            System.out.println(Arrays.toString(priority));

            // dichiaro array listScheduling
            int[] listScheduling = new int[numOp];
            listScheduling = priorityScheduling(list, numOp, asapScheduling, priority, Integer.parseInt(args[0]), Integer.parseInt(args[1]));

            System.out.println("\nList scheduling:");
            System.out.println(Arrays.toString(listScheduling));
            System.out.println("Clock cycles: "+ Utilities.maxValue(listScheduling));

            // stop timer
            long stopTime = System.currentTimeMillis();
            System.out.println("\nElapsed time: " + (stopTime-startTime) + " ms");
            
        }
        catch (FileNotFoundException e) {
            // non ho trovato il file config.txt
            System.err.println("Error! The file " + fileName + " must be in the same directory of this program");
            System.exit(1);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}