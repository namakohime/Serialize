/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serialixe;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Yamamoto
 */
public class Color implements Serializable {

    private static final long serialVersionUID = 1525287529109561926L;

    //private String color;

    private transient int width;
    private transient int width1;
    private transient int width2;
    private transient int width3;
    private transient int width4;
    private transient int width5;

    Color() {
    }

    Color(int width,int width1,int width2,int width3,int width4,int width5) {
        //this.color = color;
        this.width = width;
        this.width1 = width;
        this.width2 = width;
        this.width3 = width;
        this.width4 = width;
        this.width5 = width;
    }
    
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        width = stream.readInt();
        width1 = stream.readInt();
        width2 = stream.readInt();
        width3 = stream.readInt();
        width4 = stream.readInt();
        width5 = stream.readInt();
    }
    //public String getColor() {
    //    return color;
    //}
    public int getWidth() {
        return width;
    }
    public int getWidth1() {
        return width;
    }
    public int getWidth2() {
        return width;
    }
    public int getWidth3() {
        return width;
    }
    public int getWidth4() {
        return width;
    }
    public int getWidth5() {
        return width;
    }
}
