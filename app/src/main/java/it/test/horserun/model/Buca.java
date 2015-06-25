package it.test.horserun.model;

/**
 * Created by Savarese on 23/06/15.
 */
public class Buca {

    public Buca(char t, int v){
        type=t;
        value=v;
    }

    public Buca (char t){
        type=t;
        value=-1;
    }

    private char type;
    private int value;

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
