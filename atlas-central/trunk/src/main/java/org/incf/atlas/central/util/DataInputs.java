package org.incf.atlas.central.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataInputs {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, String> dataInputs;
	
	public DataInputs(String dataInputsString) {
		
		logger.debug("DataInputs: {}", dataInputsString);
		
		dataInputs = new HashMap<String, String>();
		parse(dataInputsString);
	}
	
	public Set<String> getKeys() {
		return dataInputs.keySet();
	}
	
	public String getValue(String inputKey) {
		return dataInputs.get(inputKey);
	}
	
	private void parse(String dataInputsString) {
		
		// e.g. srsName=xxx;x=263.7;y=159.4;z=227.8
		String[] inputs = dataInputsString.split(";");
		for (String input : inputs) {
			String[] inputKeyValue = input.split("=");
			dataInputs.put(inputKeyValue[0], inputKeyValue[1]);
		}
	}

}
