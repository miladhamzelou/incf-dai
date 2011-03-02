/*

 * @(#)INCFConfigurator.java, 
 *
 * Company/Project - UCSD/BIRN
 * 9500 Gilman Drive, Bldg - Holly, San Diego, CA 92093 U.S.A.
 * All rights reserved.
 *
 */
package org.incf.whs.atlas.util;

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
public class WHSConfigurator {

	/** Only public access to a single instance of this class */
	public static final WHSConfigurator INSTANCE = new WHSConfigurator();

	/**
	 * holds the name/value propertg pairs
	 * 
	 */
	private Hashtable propsH;

	/** Force all users to use the static reference */
	private WHSConfigurator() {
	}
	

	private InputStream getInputFileURL() {

		System.out.println("1");
		InputStream path = null;
		System.out.println("2");

		path = getClass().getResourceAsStream(
		"/stage-whs-config-properties.xml");
		System.out.println("3");

		System.out.println("WHS Config Path is - " + path);
		System.out.println("4");
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
			System.out.println("1.1");

            SAXBuilder parser = new SAXBuilder();
			System.out.println("1.2");
            Document doc = parser.build(configPropertiesFileURL);
			System.out.println("1.3");
            
            Element root = doc.getRootElement();
            List properties = root.getChildren();
            Iterator iterator = properties.iterator();
			System.out.println("1.4");

            while (iterator.hasNext())  {
                // work with each property node to retrieve the
                // name and value

            	Element property = (Element) iterator.next();
                String name  = property.getChild("name").getTextTrim();
                String value = property.getChild("value").getTextTrim();
    			System.out.println("Name: " + name);
    			System.out.println("Value: " + value);

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