/*
 * @(#)BaseDAO.java, May 01, 2006, 12:44 PM
 *
 */
package org.incf.central.atlas.util;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
	private static final Logger LOG = LoggerFactory
	.getLogger(BaseDAO.class);


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

    	LOG.debug( "Start - getStandAloneConnection()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("server.database.driverClassName"); 
        String dbUrl = props.getValue("server.database.atlasdburl"); 
        String dbUser = props.getValue("server.database.atlasdbuser");
        String dbPassword = props.getValue("server.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone - {}",driverClassName);
    	LOG.debug( "dbUrl in central standalone - {}",dbUrl);
    	LOG.debug( "dbUser in central standalone - {}",dbUser);
    	LOG.debug( "dbPassword in central standalone - {}",dbPassword);

        try {

        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnection()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnection()");

        } catch ( Exception e ) {
        	e.printStackTrace();
        	LOG.debug( "There is an error: {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnection()");
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

    	LOG.debug( "Start - getStandAloneConnectionForPostgres()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("postgres.server.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.server.database.atlasdburl");
        String dbUser = props.getValue("postgres.server.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.server.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone postgres - {}",driverClassName);
        LOG.debug( "dbUrl in central standalone postgres - {}",dbUrl);
        LOG.debug( "dbUser in central standalone postgres - {}",dbUser);
        LOG.debug( "dbPassword in central standalone postgres - {}",dbPassword);
 
        try {
        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnectionForPostgres()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnectionForPostgres()");
 
        } catch ( Exception e ) {
        	LOG.debug( "There is an error {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnectionForPostgres()");
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
    public Connection getStandAloneConnectionForPostgresFromCCDB()// please change public to protected
            throws Exception {

    	LOG.debug( "Start - getStandAloneConnectionForPostgresFromCCDB()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;

        String driverClassName = props.getValue("ccdbpostgres.server.database.driverClassName"); 
        String dbUrl = props.getValue("ccdbpostgres.server.database.atlasdburl");
        String dbUser = props.getValue("ccdbpostgres.server.database.atlasdbuser");
        String dbPassword = props.getValue("ccdbpostgres.server.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone oracle - {}",driverClassName);
        LOG.debug( "dbUrl in central standalone oracle - {}",dbUrl);
        LOG.debug( "dbUser in central standalone oracle - {}",dbUser);
        LOG.debug( "dbPassword in central standalone oracle - {}",dbPassword);

        try {
        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnectionForPostgresFromCCDB()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnectionForPostgresFromCCDB()");
        } catch ( Exception e ) {
        	LOG.debug( "There is an error {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnectionForPostgresFromCCDB()");
        return connection;

    }// end of getStandAloneConnectionForPostgresFromCCDB method


    /**
     * Gets the connection to the database. Does not do connection pooling.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
    public Connection getStandAloneConnectionForPostgresFromABA()// please change public to protected
            throws Exception {

    	LOG.debug( "Start - getStandAloneConnectionForPostgresFromABA()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;

        String driverClassName = props.getValue("postgres.aba.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.aba.database.atlasdburl");
        String dbUser = props.getValue("postgres.aba.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.aba.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone oracle - {}",driverClassName);
        LOG.debug( "dbUrl in central standalone oracle - {}",dbUrl);
        LOG.debug( "dbUser in central standalone oracle - {}",dbUser);
        LOG.debug( "dbPassword in central standalone oracle - {}",dbPassword);

        try {
        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnectionForPostgresFromaBA()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnectionForPostgresFromABA()");
        } catch ( Exception e ) {
        	LOG.debug( "There is an error {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnectionForPostgresFromABA()");
        return connection;

    }// end of getStandAloneConnectionForPostgresFromCCDB method

    /**
     * Gets the connection to the database. Does not do connection pooling.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
    public Connection getStandAloneConnectionForPostgresFromUCSD()// please change public to protected
            throws Exception {

    	LOG.debug( "Start - getStandAloneConnectionForPostgresFromUCSD()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;

        String driverClassName = props.getValue("postgres.ucsd.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.ucsd.database.atlasdburl");
        String dbUser = props.getValue("postgres.ucsd.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.ucsd.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone oracle - {}",driverClassName);
        LOG.debug( "dbUrl in central standalone oracle - {}",dbUrl);
        LOG.debug( "dbUser in central standalone oracle - {}",dbUser);
        LOG.debug( "dbPassword in central standalone oracle - {}",dbPassword);

        try {
        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnectionForPostgresFromUCSD()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnectionForPostgresFromUCSD()");
        } catch ( Exception e ) {
        	LOG.debug( "There is an error {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnectionForPostgresFromUCSD()");
        return connection;

    }// end of getStandAloneConnectionForPostgresFromUCSD method

    /**
     * Gets the connection to the database. Does not do connection pooling.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
    public Connection getStandAloneConnectionForPostgresFromWHS()// please change public to protected
            throws Exception {

    	LOG.debug( "Start - getStandAloneConnectionForPostgresFromWHS()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;

        String driverClassName = props.getValue("postgres.whs.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.whs.database.atlasdburl");
        String dbUser = props.getValue("postgres.whs.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.whs.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone oracle - {}",driverClassName);
        LOG.debug( "dbUrl in central standalone oracle - {}",dbUrl);
        LOG.debug( "dbUser in central standalone oracle - {}",dbUser);
        LOG.debug( "dbPassword in central standalone oracle - {}",dbPassword);

        try {
        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnectionForPostgresFromWHS()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnectionForPostgresFromWHS()");
        } catch ( Exception e ) {
        	LOG.debug( "There is an error {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnectionForPostgresFromWHS()");
        return connection;

    }// end of getStandAloneConnectionForPostgresFromCCDB method

    /**
     * Gets the connection to the database. Does not do connection pooling.
     *
     * @param jndiName - The jndi name of the datasource
     * @return A Connection object
     * @throws DataAccessObjectException - If there is an exception establishing the connection.
     *
     */
    public Connection getStandAloneConnectionForPostgresFromEMAP()// please change public to protected
            throws Exception {

    	LOG.debug( "Start - getStandAloneConnectionForPostgresFromEMAP()");
        
    	Connection connection = null;

        CentralConfigurator props = CentralConfigurator.INSTANCE;

        String driverClassName = props.getValue("postgres.emap.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.emap.database.atlasdburl");
        String dbUser = props.getValue("postgres.emap.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.emap.database.atlasdbpassword");

        LOG.debug( "driverClassName in central standalone oracle - {}",driverClassName);
        LOG.debug( "dbUrl in central standalone oracle - {}",dbUrl);
        LOG.debug( "dbUser in central standalone oracle - {}",dbUser);
        LOG.debug( "dbPassword in central standalone oracle - {}",dbPassword);

        try {
        	Class.forName( driverClassName );
        	LOG.debug( "Start - getStandAloneConnectionForPostgresFromEMAP()");
            connection = 
            	DriverManager.getConnection( dbUrl, dbUser, dbPassword );
            LOG.debug( "End - getStandAloneConnectionForPostgresFromEMAP()");
        } catch ( Exception e ) {
        	LOG.debug( "There is an error {}" , e.getClass() + e.getMessage());
            throw new Exception( e.getMessage() );
        }

        LOG.debug( "End - getStandAloneConnectionForPostgresFromEMAP()");
        return connection;

    }// end of getStandAloneConnectionForPostgresFromEMAP method

    
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
