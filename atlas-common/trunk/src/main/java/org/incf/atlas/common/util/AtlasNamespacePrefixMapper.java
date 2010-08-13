package org.incf.atlas.common.util;

import java.util.HashMap;
import java.util.Map;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class AtlasNamespacePrefixMapper extends NamespacePrefixMapper {
	
	private static final String[][] MAP = {
		{ "http://www.opengis.net/ows/1.1", "ows" },
		{ "http://www.opengis.net/wps/1.0.0", "wps"},
		{ "http://www.w3.org/1999/xlink", "xlink" },
		{ "http://www.opengis.net/gml/3.2", "gml" },
//		{ "http://www.incf.org/waxML", "wax" },
		{ "http://www.incf.org/WaxML/", "wax" },
	};
	
	private Map<String, String> prefixMap;
	
	public AtlasNamespacePrefixMapper() {
		prefixMap = new HashMap<String, String>();
		for (int i = 0; i < MAP.length; i++) {
			prefixMap.put(MAP[i][0], MAP[i][1]);
		}
	}

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, 
			boolean requirePrefix) {
		return prefixMap.get(namespaceUri);
	}

}
