package org.incf.atlas.xmlbeans.examples;

import java.util.HashMap;

public class Utilities {
	public static HashMap SuggestedNamespaces() {
		HashMap suggestedPrefixes = new HashMap();
		
		suggestedPrefixes.put("http://www.opengis.net/gml/3.2", "gml");
		// uncomment if ou want all the elements prefixed with wax:
	//	suggestedPrefixes.put("http://www.incf.org/WaxML/", "wax");
		suggestedPrefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");

		
		return suggestedPrefixes;
	}
	
	
}
