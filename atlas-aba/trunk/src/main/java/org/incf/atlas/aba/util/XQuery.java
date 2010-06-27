package org.incf.atlas.aba.util;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import net.sf.saxon.dom.TextOverNodeInfo;
import net.sf.saxon.xqj.SaxonXQDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XQuery {

	private static final Logger logger = LoggerFactory.getLogger(XQuery.class);
	
	public String execute(String xQuery) throws XQException {
		// run query
		XQDataSource ds = new SaxonXQDataSource();
		XQConnection conn = ds.getConnection();
		XQPreparedExpression exp = conn.prepareExpression(xQuery);
		XQResultSequence result = exp.executeQuery();
		if (result.next()) {
//			Object o = result.getObject();
//			
//			logger.debug("type: {}", o.getClass().getName());
			
			return ((TextOverNodeInfo) result.getObject()).getData();
		}
		return null;
	}
	
}
