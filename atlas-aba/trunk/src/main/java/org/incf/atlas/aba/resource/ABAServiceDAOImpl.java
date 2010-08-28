/**
 * This class is used to deal with the database like create connection, query, etc.
 */
package org.incf.atlas.aba.resource; 

import java.util.ArrayList;
import java.util.List;

import org.incf.atlas.aba.util.ABAConfigurator;
import org.incf.atlas.common.util.BaseDAO;

/**
 * @author Asif Memon
 *
 */
public class ABAServiceDAOImpl {
	
	private ABAConfigurator configurator = ABAConfigurator.INSTANCE;

	public List getSRSListData() {
		
		List list = new ArrayList();
		//BaseDAO dao = new BaseDAO();
		//dao.get
		
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
