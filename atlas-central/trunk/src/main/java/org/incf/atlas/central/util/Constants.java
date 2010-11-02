package org.incf.atlas.central.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

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
	
	private PropertiesConfiguration config;

	// singleton pattern
	private Constants() {
		
		config = new PropertiesConfiguration();
		try {
			config.load(Constants.class.getResourceAsStream(PROPS));
		} catch (ConfigurationException e) {
			throw new IllegalStateException("Unable to load resources '" 
					+ PROPS + "'.");
		}

//		Properties props = new Properties();
//		try {
//			props.load(Constants.class.getResourceAsStream(PROPS));
//		} catch (IOException e) {
//			throw new IllegalStateException("Unable to load resource '" 
//					+ PROPS + "'.", e);
//		}
		
        defaultLanguage = config.getString("defaultLanguage");
        defaultResponseForm = config.getString("defaultResponseForm");
        defaultService = config.getString("defaultService");
        defaultVersion = config.getString("defaultVersion");
        
//        String[] sNames = config.getString("srsNames").split(",");
        String[] sNames = config.getStringArray("srsNames");
        srsNames = new HashSet<String>();
        for (int i = 0; i < sNames.length; i++) {
            String srsName = sNames[i].trim().toLowerCase();
            if (srsName != null && srsName.length() != 0) {
                srsNames.add(srsName);
            }
        }
        
//        validateXml = config.getBoolean("validateXml");
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
	
	public Configuration getConfiguration() {
		return config;
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
