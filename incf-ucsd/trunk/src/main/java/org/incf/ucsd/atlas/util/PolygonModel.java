/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.incf.ucsd.atlas.util;

import java.util.*;
/**
 *
 * @author amemon
 */
public class PolygonModel
{
    private int polygonID = -1;
    private Vector<Annot_Point> vPoint = new Vector<Annot_Point>();
    private String userName = null;

    private HasGeometryProperty gp = null;

    /**
     * @return the polygonID
     */
    public int getPolygonID() {
        return polygonID;
    }

    /**
     * @param polygonID the polygonID to set
     */
    public void setPolygonID(int polygonID) {
        this.polygonID = polygonID;
    }

    /**
     * @return the vPoint
     */
    public Vector<Annot_Point> getvPoint() {
        return vPoint;
    }

    /**
     * @param vPoint the vPoint to set
     */
    public void setvPoint(Vector<Annot_Point> vPoint) {
        this.vPoint = vPoint;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        for(int i=0;i<this.vPoint.size();i++)
        {
            Annot_Point p = this.vPoint.get(i);
            if(i>0)
                buff.append(",");
            buff.append(p.toString());
        }

       return buff.toString();
       // return this.userName+"----"+this.polygonID+" "+buff.toString();
    }

    /**
     * @return the gp
     */
    public HasGeometryProperty getGp() {
        return gp;
    }

    /**
     * @param gp the gp to set
     */
    public void setGp(HasGeometryProperty gp) {
        this.gp = gp;
    }


    public String to2DString()
    {
        StringBuffer buff = new StringBuffer();
        for(int i=0;i<this.vPoint.size();i++)
        {
            Annot_Point p = this.vPoint.get(i);
            double x = p.getX();
            double y = p.getY();

            if(i > 0)
                buff.append(", ");
            buff.append(x+" "+y);
        }
        return buff.toString();
    }
}
