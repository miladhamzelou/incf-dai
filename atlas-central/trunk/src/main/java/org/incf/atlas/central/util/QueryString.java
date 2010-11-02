package org.incf.atlas.central.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryString {
	
/*
service=WPS&version=1.0.0&request=Execute&Identifier=GetXxxByPOI&ResponseForm=text/xml 	
service=WPS&version=1.0.0&request=Execute&Identifier=GetXxxByPOI&DataInputs=srsName=xxx;x=263.7;y=159.4;z=227.8&ResponseForm=text/xml 	
 */

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<String, String> queryKVPairs;
	private DataInputs dataInputs;
	
	public QueryString(String queryString) {
		
		logger.debug("QueryString: {}", queryString);
		
		queryKVPairs = new HashMap<String, String>();
		parse(queryString);
	}
	
	public Set<String> getKeys() {
		return queryKVPairs.keySet();
	}
	
	public String getValue(String inputKey) {
		return queryKVPairs.get(inputKey);
	}
	
	public DataInputs getDataInputs() {
		return dataInputs;
	}
	
	private void parse(String queryString) {
		
		// service=WPS&version=1.0.0&request=Execute&Identifier=GetXxxByPOI
		// &DataInputs=srsName=xxx;x=263.7;y=159.4;z=227.8&ResponseForm=text/xml
		String[] pairs = queryString.split("&");
		for (String kv : pairs) {
			int idx = kv.indexOf('=');
			String key = kv.substring(0, idx);
			if (key.equalsIgnoreCase("DataInputs")) {
				dataInputs = new DataInputs(kv.substring(idx + 1));
			} else {
				queryKVPairs.put(key, kv.substring(idx + 1));
			}
		}
	}

}
