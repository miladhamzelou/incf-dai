package org.incf.atlas.aba.util;

import java.io.IOException;
import java.util.Properties;

public class Constants {

	private static final String PROPS = "/atlas-aba.properties";
	
	// singleton pattern
	private static Constants constants;
	
    private String defaultLanguage;
    private String defaultResponseForm;
    private String defaultService;
    private String defaultVersion;
	
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
    
}
