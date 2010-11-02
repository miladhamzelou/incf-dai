package org.incf.atlas.central.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

public class XmlNameSpaceContext implements NamespaceContext {

	private HashMap<String, String> prefixUriMap = new HashMap<String, String>();
	private HashMap<String, HashSet<String>> uriPrefixMap = new HashMap<String, HashSet<String>>();

	public void addNamespace(String prefix, String uri) {
		this.prefixUriMap.put(prefix, uri);
		if (this.uriPrefixMap.containsKey(uri)) {
			this.uriPrefixMap.get(uri).add(prefix);
		} else {
			HashSet<String> set = new HashSet<String>();
			set.add(prefix);
			this.uriPrefixMap.put(uri, set);
		}
	}

	public String getNamespaceURI(String prefix) {
		return this.prefixUriMap.get(prefix);
	}

	public String getPrefix(String namespaceURI) {
		HashSet<String> set = this.uriPrefixMap.get(namespaceURI);
		Iterator<String> i = set.iterator();
		String result = null;
		if (i.hasNext()) {
			result = i.next();
		}
		return result;
	}

	public Iterator<String> getPrefixes(String namespaceURI) {
		HashSet<String> set = this.uriPrefixMap.get(namespaceURI);
		return set.iterator();
	}

	public void removePrefix(String prefix) {
		this.prefixUriMap.remove(prefix);
	}

	public void removeUri(String uri) {
		if (this.uriPrefixMap.containsKey(uri)) {
			HashSet<String> set = this.uriPrefixMap.get(uri);
			Iterator<String> i = set.iterator();
			while (i.hasNext()) {
				this.prefixUriMap.remove(i.next());
			}
			this.uriPrefixMap.remove(uri);
		}
	}
	
}
