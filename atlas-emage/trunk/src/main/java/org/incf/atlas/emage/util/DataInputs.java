package org.incf.atlas.emage.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataInputs {

	private final Logger logger = LoggerFactory.getLogger(DataInputs.class);
	
	private Map<String, String> dataInputs;
	
	public DataInputs(String dataInputString) {
		dataInputs = new HashMap<String, String>();
		parse(dataInputString);
	}
	
	public Set<String> getKeys() {
		return dataInputs.keySet();
	}
	
	public String getValue(String inputKey) {
		return dataInputs.get(inputKey);
	}
	
	private void parse(String dataInputString) {
		
		// srsCode=WHS;x=263.7;y=159.4;z=227.8
		String[] inputs = dataInputString.split(";");
		for (String input : inputs) {
			String[] inputKeyValue = input.split("=");
			dataInputs.put(inputKeyValue[0], inputKeyValue[1]);
		}
	}

}
