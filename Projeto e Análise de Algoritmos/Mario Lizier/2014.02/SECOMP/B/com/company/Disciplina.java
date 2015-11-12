package com.company;

/**
 * Created by Thales on 10/26/2014.
 */
public class Disciplina {

    private String s;
    private int c, f, i;

    public Disciplina(String s, int c, int f, int i){
        this.s = s;
        this.c = c;
        this.f = f;
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
