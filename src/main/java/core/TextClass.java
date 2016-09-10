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
public class TextClass {
    private String text;
    private int classindex;

    public TextClass(String text, int classindex) {
        this.text = text;
        this.classindex = classindex;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the classindex
     */
    public int getClassindex() {
        return classindex;
    }

    /**
     * @param classindex the classindex to set
     */
    public void setClassindex(int classindex) {
        this.classindex = classindex;
    }
    
    
    
    
}
