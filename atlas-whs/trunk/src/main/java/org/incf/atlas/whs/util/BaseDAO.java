/*
 * @(#)BaseDAO.java, May 01, 2006, 12:44 PM
 *
 */
package org.incf.atlas.whs.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;


/**
 * BaseDAO is an abstract class that provides connection methods
 *
 * @author	Asif Memon
 *
 * @version	4
 * @history	4
 * Date         Who   Description
 * -----------  ----  ---------------------------------------------
 * 23-JUN-2006  AM    Initial Version.
 */
public class BaseDAO {


//--------------------------------Data Members----------------------------------


    DataSource dataSource = null;


//---------------------------Protected Methods----------------------------------


    /** For Datasource(Connection Pooling)
     * Gets the connection to the database.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
/*    protected Connection getConnection( String jndiName )
            throws Exception {

        boolean standAlone = true;

        Connection connection = null;

        BIRNConfigurator props = BIRNConfigurator.INSTANCE;

            //This is done for connection pooling 
            try {
                connection = getDataSource( jndiName ).getConnection();
            } catch ( SQLException sqle ) {
                throw new DAOException( sqle.getMessage() );
            } catch ( Exception e ) {
                throw new DAOException( e.getMessage() );
            }

        return connection;

    }  // end of getConnection method
    
*/


    /**
     * Gets the connection to the database. Does not do connection pooling.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
    public Connection getStandAloneConnection()// please change public to protected
            throws Exception {

    	System.out.println( "Start - getStandAloneConnection()");
        
    	Connection connection = null;

        WHSConfigurator props = WHSConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("server.database.driverClassName"); 
        String dbUrl = props.getValue("server.database.atlasdburl"); 
        String dbUser = props.getValue("server.database.atlasdbuser");
        String dbPassword = props.getValue("server.database.atlasdbpassword");

    	System.out.println( "driverClassName - "+driverClassName);
    	System.out.println( "dbUrl - "+dbUrl);
    	System.out.println( "dbUser - "+dbUser);
    	System.out.println( "dbPassword - "+dbPassword);

        try {
        	Class.forName( driverClassName );
        	System.out.println( "Start - getStandAloneConnection()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            System.out.println( "End - getStandAloneConnection()");
 
        } catch ( Exception e ) {
        	e.printStackTrace();
        	System.out.println( "There is an error" + e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        System.out.println( "End - getStandAloneConnection()");
        return connection;

    }// end of getStandAloneConnection method


    /**
     * Gets the connection to the database. Does not do connection pooling.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
    public Connection getStandAloneConnectionForPostgres()// please change public to protected
            throws Exception {

    	System.out.println( "Start - getStandAloneConnectionForPostgres()");
        
    	Connection connection = null;

        WHSConfigurator props = WHSConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("postgres.server.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.server.database.atlasdburl");
        String dbUser = props.getValue("postgres.server.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.server.database.atlasdbpassword");

        System.out.println( "driverClassName - "+driverClassName);
        System.out.println( "dbUrl - "+dbUrl);
        System.out.println( "dbUser - "+dbUser);
        System.out.println( "dbPassword - "+dbPassword);
 
        try {
        	Class.forName( driverClassName );
        	System.out.println( "Start - getStandAloneConnectionForPostgres()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            System.out.println( "End - getStandAloneConnectionForPostgres()");
 
        } catch ( Exception e ) {
        	System.out.println( "There is an error" + e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        System.out.println( "End - getStandAloneConnectionForPostgres()");
        return connection;

    }// end of getStandAloneConnection method


    /**
     * Closes an open connection.
     *
     * @param connection - A connection object
     */
    protected void closeConnection( Connection connection ) {

        try {
            if ( connection != null || !connection.isClosed() ) {
                connection.close();
            }
        } catch (Exception doNothing) {
        } finally {
            connection = null;
        }
    }


//------------------------------- Private Methods -----------------------------


    /**
     * Returns data source to be used to get the connection object.
     *
     * @param jndiName - The jndi name for the data source
     * @return Returns the DataSource object
     * @throws Exception Ignores the exception and passes it to the caller
     */
    public DataSource getDataSource( String jndiName )
            throws Exception {

        // Throw the exception back to the caller
        if ( dataSource == null ) {
            Context context = new InitialContext();
            dataSource = ( DataSource )context.lookup( jndiName );
        }

        return dataSource;

    } // end of getDataSource

    /**
     * Returns data source to be used to get the connection object.
     *
     * @param jndiName - The jndi name for the data source
     * @return Returns the DataSource object
     * @throws Exception Ignores the exception and passes it to the caller
     */
    public void setDataSource( String jndiName )
            throws Exception {

        // Throw the exception back to the caller
        if ( dataSource == null ) {
            Context context = new InitialContext();
            dataSource = ( DataSource )context.lookup( jndiName );
        }

    } // end of getDataSource


}//end of DataAccessObject.java
