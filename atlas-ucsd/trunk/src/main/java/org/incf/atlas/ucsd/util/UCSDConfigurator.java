/*
 * @(#)INCFConfigurator.java, 
 *
 * Company/Project - UCSD/BIRN
 * 9500 Gilman Drive, Bldg - Holly, San Diego, CA 92093 U.S.A.
 * All rights reserved.
 *
 */
package org.incf.atlas.ucsd.util;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * It reads the xml file containing generic name/value pairs and mantains these
 * properties in a hashtable for easy lookup in the application
 * 
 * This class follows the Singelton patterm to insure that only one instance of
 * this class is used by an applicaiton
 * 
 * This class also supports automatic refreshing of properties from the file -
 * if the input xml file has been modified
 * 
 * @version 2
 * 
 * Date Who Description 26-JAN-2009 amemon Initial Version
 * 
 */
public class UCSDConfigurator {

	/** Only public access to a single instance of this class */
	public static final UCSDConfigurator INSTANCE = new UCSDConfigurator();

	/**
	 * holds the name/value propertg pairs
	 * 
	 */
	private Hashtable propsH;

	/** Force all users to use the static reference */
	private UCSDConfigurator() {
	}
	

	private InputStream getInputFileURL() {

		InputStream path = null;

		path = getClass().getResourceAsStream( 
		"/ucsd-config-properties-development.xml");

		System.out.println("Config Path is - " + path);
		return path;

	}

	/**
	 * Main getter for properties. Returns null id not found of if the name/key
	 * is null
	 * 
	 */
	public synchronized String getValue(String name) { 
		// check for first time in or a refresh conditions
		if (propsH == null) {
			load();
		}

		String value = null;
		if (name != null) {
			try {
				value = (String) propsH.get(name);
			} catch (NullPointerException npE) {
			}
		}
		return value;
	}

	/**
	 * Loads the name/value pairs from the xml file
	 * 
	 */
	private void load() {
		propsH = new Hashtable();

		try {

			InputStream configPropertiesFileURL = getInputFileURL();

            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(configPropertiesFileURL);
            
            Element root = doc.getRootElement();
            List properties = root.getChildren();
            Iterator iterator = properties.iterator();

            while (iterator.hasNext())  {
                // work with each property node to retrieve the
                // name and value

            	Element property = (Element) iterator.next();
                String name  = property.getChild("name").getTextTrim();
                String value = property.getChild("value").getTextTrim();

                // check that the name does not already exist in the
                // hash table. If it does throw error and quit.
                if (propsH.containsKey(name))
                {
                    propsH = null;
                    throw new Exception ("Duplicate property name passed in via xml file: " + name);
                }
                // add the property
                propsH.put(name,value);
            }		
			System.out.println("7");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}