package org.incf.atlas.aba.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class AtlasNamespacePrefixMapper extends NamespacePrefixMapper {

	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, 
			boolean requirePrefix) {
		if (namespaceUri.equals("http://www.opengis.net/ows/1.1")) {
			return "ows";
		}
		if (namespaceUri.equals("http://www.opengis.net/wps/1.0.0")) {
			return "wps";
		}
		if (namespaceUri.equals("http://www.w3.org/1999/xlink")) {
			return "xlink";
		}
		return null;
	}

}
