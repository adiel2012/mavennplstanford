/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author LABORATORIO 5
 */
public class Pair implements Comparable<Pair>{
    private String string;
    private int value;

    public Pair(String string, int value) {
        this.string = string;
        this.value = value;
    }

    /**
     * @return the string
     */
    public String getString() {
        return string;
    }

    /**
     * @param string the string to set
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Pair o) {
      return value - o.value;    
    }
    
    
}
