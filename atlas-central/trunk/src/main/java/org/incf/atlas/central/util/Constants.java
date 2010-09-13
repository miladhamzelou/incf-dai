package org.incf.atlas.central.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Constants {

	private static final String PROPS = "/atlas-central.properties";
	
	// singleton pattern
	private static Constants constants;
	
    private String defaultLanguage;
    private String defaultResponseForm;
    private String defaultService;
    private String defaultVersion;
    private Set<String> srsNames;
    private boolean validateXml;
	
	// singleton pattern
	private Constants() {
		
		Properties props = new Properties();
		try {
			props.load(Constants.class.getResourceAsStream(PROPS));
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load resource '" 
					+ PROPS + "'.", e);
		}
		
        defaultLanguage = props.getProperty("defaultLanguage");
        defaultResponseForm = props.getProperty("defaultResponseForm");
        defaultService = props.getProperty("defaultService");
        defaultVersion = props.getProperty("defaultVersion");
        
        String[] sNames = props.getProperty("srsNames").split(",");
        srsNames = new HashSet<String>();
        for (int i = 0; i < sNames.length; i++) {
            String srsName = sNames[i].trim().toLowerCase();
            if (srsName != null && srsName.length() != 0) {
                srsNames.add(srsName);
            }
        }
        
        validateXml = Boolean.parseBoolean(props.getProperty("validateXml"));
	}
	
	// singleton pattern
	public Constants clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static Constants getInstance() {
		if (constants == null) {
			constants = new Constants();
		}
		return constants;
	}
	
    public String getDefaultLanguage() {
        return defaultLanguage;
    }
    
    public String getDefaultResponseForm() {
        return defaultResponseForm;
    }
    
    public String getDefaultService() {
        return defaultService;
    }
    
    public String getDefaultVersion() {
        return defaultVersion;
    }
    
    public Set<String> getSrsNames() {
        return srsNames;
    }
    
    public boolean validateXml() {
    	return validateXml;
    }
    
}
