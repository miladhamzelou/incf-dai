package org.incf.atlas.central.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class XmlNameSpaceContext implements NamespaceContext {

	// prefix --> uri (1:1)
	private Map<String, String> prefixUriMap = new HashMap<String, String>();

	// uri --prefix(es) (1:many)
	private HashMap<String, Set<String>> uriPrefixesMap = 
			new HashMap<String, Set<String>>();
	
	public XmlNameSpaceContext() {
		
		// prefix is "xml"
		addNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
		
		// prefix is "xmlns"
		addNamespace(XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
		
		// this is default, override with setDefaultNamespaceURI()
		addNamespace(XMLConstants.DEFAULT_NS_PREFIX, XMLConstants.NULL_NS_URI);
	}
	
	public void setDefaultNamespaceURI(String defaultNamespaceURI) {
		addNamespace(XMLConstants.DEFAULT_NS_PREFIX, defaultNamespaceURI);
	}

	public void addNamespace(String prefix, String uri) {
		
		// prefix --> uri
		prefixUriMap.put(prefix, uri);
		
		// uri --> prefix(es)
		if (uriPrefixesMap.containsKey(uri)) {
			uriPrefixesMap.get(uri).add(prefix);
		} else {
			Set<String> prefixes = new HashSet<String>();
			prefixes.add(prefix);
			uriPrefixesMap.put(uri, prefixes);
		}
	}

	public String getNamespaceURI(String prefix) {
		
		// argument is null
		if (prefix == null) {
			throw new IllegalArgumentException();
		}
		
		// prefix is bound
		String namespaceUri = prefixUriMap.get(prefix);
		if (namespaceUri != null) {
			return namespaceUri;
		}
		
		// prefix is unbound
		return XMLConstants.NULL_NS_URI;
	}

	public String getPrefix(String namespaceUri) {
		
		// argument is null
		if (namespaceUri == null) {
			throw new IllegalArgumentException();
		}
		
		// uri is bound
		Set<String> prefixes = uriPrefixesMap.get(namespaceUri);
		if (prefixes != null) {
			return prefixes.iterator().next();		// single or arbitrary
		}
	
		// uri is unbound
		return null;
	}

	public Iterator<String> getPrefixes(String namespaceURI) {
		Set<String> prefixes = uriPrefixesMap.get(namespaceURI);
		
		// TODO this iterator should not support remove()
		return prefixes.iterator();
	}
	
	// not sure how to provide an iterator with disabled remove()
//	public class namespacePrefixIterator<String> implements Iterator<String> {
//		public void remove() {
//			throw new UnsupportedOperationException();
//		}
//	}

	/**
	 * Removes the prefix and the namespace URI. If multiple prefixes map to
	 * a single URI, remove only this prefix and not the URI.
	 * 
	 * @param prefix namespace prefix to be removed
	 */
	public void removePrefix(String prefix) {
		if (prefix == null) {
			return;
		}
		
		// get uri for prefix, exit if none
		String namespaceUri = prefixUriMap.get(prefix);
		if (namespaceUri == null) {
			return;
		}
		
		// get all prefixes for uri
		Set<String> prefixes = uriPrefixesMap.get(namespaceUri);
		
		// if not more than 1, remove from both maps
		if (prefixes.size() <= 1) {
			uriPrefixesMap.remove(namespaceUri);
			prefixUriMap.remove(prefix);
			return;
		}
		
		// if other prefixes for uri, remove only this one
		Iterator<String> it = prefixes.iterator();
		while (it.hasNext()) {
			String thisPrefix = it.next();
			if (thisPrefix.equals(prefix)) {
				it.remove();
			}
		}
		prefixUriMap.remove(prefix);
	}

	/**
	 * Finds all prefixes for given URI, removes every prefix, then finally
	 * removes the URI.
	 * 
	 * @param namespaceUri URI to be removed
	 */
	public void removeUri(String namespaceUri) {
		if (namespaceUri == null) {
			return;
		}
		
		// get prefixes for uri, exit if none
		Set<String> prefixes = uriPrefixesMap.get(namespaceUri);
		if (prefixes == null) {
			return;
		}
		
		// for each prefix, remove prefix
		Iterator<String> it = prefixes.iterator();
		while (it.hasNext()) {
			prefixUriMap.remove(it.next());
		}
		
		// finally remover uri
		uriPrefixesMap.remove(namespaceUri);
	}
	
}
