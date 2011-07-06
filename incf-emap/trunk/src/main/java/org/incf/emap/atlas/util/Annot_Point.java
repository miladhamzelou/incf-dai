/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.incf.emap.atlas.util;

/**
 *
 * @author amemon
 */
public class Annot_Point
{
    private double x = 0.0;
    private double y = 0.0;
    private double z = 0.0;

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }

    public String toString()
    {
        return this.x+" "+this.y+" "+this.z;
    }

}
