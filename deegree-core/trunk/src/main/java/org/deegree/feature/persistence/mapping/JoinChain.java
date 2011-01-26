//$HeadURL: http://svn.wald.intevation.org/svn/deegree/deegree3/branches/3.0/deegree-core/src/main/java/org/deegree/feature/persistence/mapping/JoinChain.java $
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.feature.persistence.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code></code> class TODO add class documentation here.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 25450 $, $Date: 2010-07-20 06:42:28 -0700 (Tue, 20 Jul 2010) $
 */
public class JoinChain implements MappingExpression {

    private List<DBField> dbFields;

    public JoinChain( DBField dbf1, DBField dbf2 ) {
        dbFields = new ArrayList<DBField>( 2 );
        dbFields.add( dbf1 );
        dbFields.add( dbf2 );
    }

    public JoinChain( DBField dbf, JoinChain jc ) {
        dbFields = new ArrayList<DBField>( jc.dbFields.size() + 1 );
        dbFields.add( dbf );
        dbFields.addAll( jc.dbFields );        
    }

    public List<DBField> getFields() {
        return dbFields;
    }
    
    @Override
    public String toString() {
        String s = dbFields.get( 0 ).toString();
        for ( int i = 1; i < dbFields.size(); i++ ) {
            s += "->";
            s += dbFields.get( i );
        }
        return s;
    }
}
