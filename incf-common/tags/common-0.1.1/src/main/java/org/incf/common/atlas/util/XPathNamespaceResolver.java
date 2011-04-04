package org.incf.common.atlas.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathNamespaceResolver implements NamespaceContext {
	
    private static final Logger LOG = LoggerFactory.getLogger(
    		XPathNamespaceResolver.class);

	// prefix assigned to default namespace for xpath use
	/* XPath must use the same namespaces as the document and the document's
	 * default namespace must be mapped to a prefix that is used in the xpath
	 * expression! See
	 * http://www.ibm.com/developerworks/library/x-javaxpathapi.html#N1022D
	 */
	// xpath must use the same namespaces as the document and the document's
	// default namespace must be mapped to a prefix that is used in the xpath
	// expression
    public static final String DEFAULT_PREFIX = "dflt";
    
    private Map<String, String> prefix2Uri = new HashMap<String, String>();
    private Map<String, String> uri2Prefix = new HashMap<String, String>();

    /**
     * This constructor parses the document and stores all namespaces it can
     * find. If toplevelOnly is true, only namespaces in the root are used.
     * 
     * @param document source document
     * @param toplevelOnly restriction of the search to enhance performance
     */
    public XPathNamespaceResolver(Document document, boolean toplevelOnly) {
        examineNode(document.getFirstChild(), toplevelOnly);
        
        if (LOG.isDebugEnabled()) {
        	StringBuilder buf = new StringBuilder();
        	buf.append("The list of the cached namespaces:");
            for (String key : prefix2Uri.keySet()) {
                buf.append("\nprefix " + key + ": uri " + prefix2Uri.get(key));
            }
            LOG.debug(buf.toString());
        }
    }

    /**
     * A single node is read, the namespace attributes are extracted and stored.
     * 
     * @param node to examine
     * @param attributesOnly, if true no recursion happens
     */
    private void examineNode(Node node, boolean attributesOnly) {
        NamedNodeMap attributes = node.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            storeAttribute((Attr) attribute);
        }

        if (!attributesOnly) {
            NodeList chields = node.getChildNodes();
            for (int i = 0; i < chields.getLength(); i++) {
                Node chield = chields.item(i);
                if (chield.getNodeType() == Node.ELEMENT_NODE)
                    examineNode(chield, false);
            }
        }
    }

    /**
     * This method looks at an attribute and stores it, if it is a namespace
     * attribute.
     * 
     * @param attribute to examine
     */
    private void storeAttribute(Attr attribute) {
    	
        // examine attributes in namespace xmlns
        if (attribute.getNamespaceURI() != null
                && attribute.getNamespaceURI().equals(
                        XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            if (attribute.getNodeName().equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            	
            	// default namespace uri
                putInCache(DEFAULT_PREFIX, attribute.getNodeValue());
                
            } else {
            	
                // defined prefixes and uri's
                putInCache(attribute.getLocalName(), attribute.getNodeValue());
            }
        }
    }

    private void putInCache(String prefix, String uri) {
        prefix2Uri.put(prefix, uri);
        uri2Prefix.put(uri, prefix);
    }

    /**
     * This method is called by XPath. It returns the default namespace, if the
     * prefix is null or "".
     * 
     * @param prefix to search for
     * @return uri
     */
    public String getNamespaceURI(String prefix) {
        if (prefix == null || prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            return prefix2Uri.get(DEFAULT_PREFIX);
        } else {
            return prefix2Uri.get(prefix);
        }
    }

    /**
     * This method is not needed in this context, but can be implemented in a
     * similar way.
     */
    public String getPrefix(String namespaceURI) {
        return uri2Prefix.get(namespaceURI);
    }

    public Iterator getPrefixes(String namespaceURI) {
    	throw new UnsupportedOperationException();
    }
    
}
