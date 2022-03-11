/*
List-based scheduling implementation in Java
Author: Enrico Talmelli
*/

/*
name: nome dell'operazione
type: somma/sottrazione (+) o prodotto/divisione (*)
input1/input2: i 2 operandi
index1/index2: indice dei 2 operandi (-1 se è una variabile d'ingresso)
*/
public class Operation {
    private String name;
    private char type;
    private String input1;
    private String input2;
    private int index1;
    private int index2;

    public String toString(){
        return (this.name + " := " + this.input1 + " " + this.type + " " + this.input2);
    }

    public void load(String nameOp, String typeOp, String input1Op, String input2Op){ 
        this.name = nameOp;
        // this.type = typeOp;
        this.input1 = input1Op;
        this.input2 = input2Op;


        if(this.input1.length() == 1) {
            this.index1 = -1;
        }
        else {
            this.index1 = Integer.parseInt(this.input1.substring(1));
        }

        if(this.input2.length() == 1) {
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

        // è stato letto un carattere non ammesso
        else {
            System.err.println(typeOp + " is an illegal character");
            System.exit(1);
        }
    }

    public int getNumber(){
        String sub = this.name.substring(1);
        return Integer.parseInt(sub);
    }

    public int getIndex1(){
        return this.index1;
    }

    public int getIndex2() {
        return this.index2;
    }

    public String getType() {
        String s = String.valueOf(this.type);
        return s;
    }
}
