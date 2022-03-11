/*
List-based scheduling implementation in Java
Author: Enrico Talmelli
*/

/*
name: nome dell'operazione
type: somma/sottrazione (+) o prodotto/divisione (*)
input1 e input2: i 2 operandi
index1 e index2: indice dei 2 operandi (-1 se è una variabile d'ingresso)
*/
public class Operation {
    private String name;
    private char type;
    private String input1;
    private String input2;
    private int index1;
    private int index2;

    // restituisce una stringa con le informazioni sull'operazione (nome, tipo di operazione e operandi)
    public String toString(){
        return (this.name + " := " + this.input1 + " " + this.type + " " + this.input2);
    }

    // vengono assegnate le variabili di istanza
    public void load(String nameOp, String typeOp, String input1Op, String input2Op){ 
        this.name = nameOp;
        this.input1 = input1Op;
        this.input2 = input2Op;


        // se il nome dell'operando non inizia con 'u' allora è una variabile di ingresso
        if(input1Op.charAt(0) != 'u') {
            this.index1 = -1;
        }
        // altrimenti gli assegno l'indice pari al numero dell'operazione
        // es. se operando 1 è "u4" allora è di indice 4
        else {
            this.index1 = Integer.parseInt(this.input1.substring(1));
        }

        // stessa cosa per il secondo operando
        if(input2Op.charAt(0) != 'u') {
            this.index2 = -1;
        }
        else {
            this.index2 = Integer.parseInt(this.input2.substring(1));
        }

        // se è una somma o una sottrazione viene salvato come '+' (adder)
        if((typeOp.equals("+")) || (typeOp.equals("-"))) {
            this.type = '+';
        }

        // se è una moltiplicazione o una divisione viene salvato come '*' (molt)
        else if((typeOp.equals("*")) || (typeOp.equals("/"))) {
            this.type = '*';
        }

        // è stato letto un carattere non ammesso come tipo di operazione
        else {
            System.err.println(typeOp + " is an illegal character");
            System.exit(1);
        }
    }

    // restituisce l'indice dell'operazione
    public int getNumber(){
        String sub = this.name.substring(1);
        return Integer.parseInt(sub);
    }

    // restituisce l'indice del primo operando
    public int getIndex1(){
        return this.index1;
    }

    // restituisce l'indice del secondo operando
    public int getIndex2() {
        return this.index2;
    }

    // restituisce il tipo di operazione (+ o *)
    public String getType() {
        String s = String.valueOf(this.type);
        return s;
    }
}
