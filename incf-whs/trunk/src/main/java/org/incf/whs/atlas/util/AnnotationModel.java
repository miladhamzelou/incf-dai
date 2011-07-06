/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.incf.whs.atlas.util;

import java.util.*;
/**
 *
 * @author amemon
 */
public class AnnotationModel
{
    private String resourcePath = null;
    private Vector<PolygonModel> vp = new Vector<PolygonModel>();

    /**
     * @return the resourcePath
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @param resourcePath the resourcePath to set
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * @return the vp
     */
    public Vector<PolygonModel> getVp() {
        return vp;
    }

    /**
     * @param vp the vp to set
     */
    public void setVp(Vector<PolygonModel> vp) {
        this.vp = vp;
    }

    public String toString()
    {
        return this.resourcePath+"---"+this.vp.toString();
    }
}
