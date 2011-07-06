/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.incf.whs.atlas.util;

/**
 *
 * @author amemon
 */
public class HasGeometryProperty
{
    private String instanceID = null;
    private String ontoName = null;
    private String ontoUri= null;
    private int polygonID = -1;

    /**
     * @return the instanceID
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * @param instanceID the instanceID to set
     */
    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    /**
     * @return the ontoName
     */
    public String getOntoName() {
        return ontoName;
    }

    /**
     * @param ontoName the ontoName to set
     */
    public void setOntoName(String ontoName) {
        this.ontoName = ontoName;
    }

    /**
     * @return the ontoUri
     */
    public String getOntoUri() {
        return ontoUri;
    }

    /**
     * @param ontoUri the ontoUri to set
     */
    public void setOntoUri(String ontoUri) {
        this.ontoUri = ontoUri;
    }

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

    
}
