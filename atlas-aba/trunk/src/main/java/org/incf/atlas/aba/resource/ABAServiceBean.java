/**
 * This is a service bean to write the business logic
 */
package org.incf.atlas.aba.resource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.incf.atlas.aba.util.ABAConfigurator;

/**
 * @author Asif Memon
 *
 */
public class ABAServiceBean {


	private ABAConfigurator configurator = ABAConfigurator.INSTANCE;

	public List getSRSListData() {
		
		List list = new ArrayList();
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
