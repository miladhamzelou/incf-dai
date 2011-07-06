/*
 * @(#)BaseDAO.java, May 01, 2006, 12:44 PM
 *
 */
package org.incf.emap.atlas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

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

        EMAPConfigurator props = EMAPConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("server.database.driverClassName"); 
        String dbUrl = props.getValue("server.database.atlasdburl"); 
        String dbUser = props.getValue("server.database.atlasdbuser");
        String dbPassword = props.getValue("server.database.atlasdbpassword");

    	LOG.debug( "driverClassName - {}",driverClassName);
    	LOG.debug( "dbUrl - {}",dbUrl);
    	LOG.debug( "dbUser - {}",dbUser);
    	LOG.debug( "dbPassword - {}",dbPassword);

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

        EMAPConfigurator props = EMAPConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("postgres.server.database.driverClassName"); 
        String dbUrl = props.getValue("postgres.server.database.atlasdburl");
        String dbUser = props.getValue("postgres.server.database.atlasdbuser");
        String dbPassword = props.getValue("postgres.server.database.atlasdbpassword");

        LOG.debug( "driverClassName - {}",driverClassName);
        LOG.debug( "dbUrl - {}",dbUrl);
        LOG.debug( "dbUser - {}",dbUser);
        LOG.debug( "dbPassword - {}",dbPassword);
 
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

        EMAPConfigurator props = EMAPConfigurator.INSTANCE;
        
        String driverClassName = props.getValue("ccdbpostgres.server.database.driverClassName"); 
        String dbUrl = props.getValue("ccdbpostgres.server.database.atlasdburl");
        String dbUser = props.getValue("ccdbpostgres.server.database.atlasdbuser");
        String dbPassword = props.getValue("ccdbpostgres.server.database.atlasdbpassword");

        LOG.debug( "driverClassName - {}",driverClassName);
        LOG.debug( "dbUrl - {}",dbUrl);
        LOG.debug( "dbUser - {}",dbUser);
        LOG.debug( "dbPassword - {}",dbPassword);
 
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


	/**
	 * This method is used to create a new record in the
	 * mouseSpatialRegistryTable table
	 * 
	 * @param Value
	 *            object with the data
	 * 
	 * @return void
	 */
	public void addAnnotation(ArrayList list, String filePath)
			throws SQLException, Exception {

		// Used for Oracle connection
		Connection connection = getStandAloneConnectionForPostgres();
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		Statement stmt3 = null;
		PreparedStatement stmt4 = null;
		PreparedStatement stmt5 = null;

		// Delete from annotation_sdo
		StringBuffer deleteQuery1 = new StringBuffer();
		deleteQuery1
				.append("delete from annotation_sdo where id in (select id from annotation_dataset where dataset_name = '"
						+ filePath + "')");

		StringBuffer deleteQuery2 = new StringBuffer();
		deleteQuery2
				.append("delete from annotation_dataset where dataset_name = '"
						+ filePath + "'");

		StringBuffer annotationIDQuery = new StringBuffer();
		annotationIDQuery
				.append("select nextval('annotation_id_seq') as annotation_id");

		String errorMessage = "";
		String uniqueAnnotationID = "";

		try {

			// Delete a record from 1st table
			System.out.println("BaseDAO - Delete Query1 is - "
					+ deleteQuery1.toString());
			stmt1 = connection.prepareStatement(deleteQuery1.toString());
			// stmt1.executeUpdate();

			if (stmt1.executeUpdate() != 1) {
				errorMessage = "Unable to delete a record, please contact the administrator to resolve this issue";
			}
			System.out.println("Query 1 Done");

			// Delete a record from 2nd table
			System.out.println("BaseDAO - Delete Query2 is - "
					+ deleteQuery2.toString());
			stmt2 = connection.prepareStatement(deleteQuery2.toString());
			if (stmt2.executeUpdate() != 1) {
				errorMessage = "Unable to delete a record, please contact the administrator to resolve this issue";
			}
			System.out.println("Query 2 Done");

			System.out.println("BaseDAO - AnnotationIDQuery Query3 is - "
					+ annotationIDQuery.toString());
			stmt3 = connection.createStatement();
			ResultSet rs = stmt3.executeQuery(annotationIDQuery.toString());

			while (rs.next()) {
				uniqueAnnotationID = rs.getString("annotation_id");
			}
			System.out.println("Query 3 Done");

			StringBuffer insertQuery4 = new StringBuffer();
			insertQuery4.append(
					" INSERT INTO annotation_dataset (id, dataset_name)")
					.append(
							" VALUES (" + uniqueAnnotationID + ", '" + filePath
									+ "');");

			System.out.println("BaseDAO - Insert Query4 is - "
					+ insertQuery4.toString());
			stmt4 = connection.prepareStatement(insertQuery4.toString());
			if (stmt4.executeUpdate() != 1) {
				errorMessage = "Unable to insert a record, please contact the administrator to resolve this issue";
			}
			System.out.println("Query 4 Done");

			// Start - Loop
			Iterator iterator = list.iterator();
			AnnotationVO vo = null;

			while (iterator.hasNext()) {
				vo = (AnnotationVO) iterator.next();
				// Insert a record into 2nd table
				StringBuffer insertQuery5 = new StringBuffer();
				insertQuery5.append(
						" INSERT INTO annotation_sdo (id, coordinates, sdo) ")
						.append(
								" VALUES (" + uniqueAnnotationID + ",'"
										+ vo.getPolygonString()
										+ "', ST_GeomFromText('POLYGON(("
										+ vo.getPolygonString() + "))')); ");

				System.out.println("BaseDAO - Insert Query5 is - "
						+ insertQuery5.toString());
				stmt5 = connection.prepareStatement(insertQuery5.toString());
				if (stmt5.executeUpdate() != 1) {
					errorMessage = "Unable to insert a record, please contact the administrator to resolve this issue";
				}
				System.out.println("Query 5 Done");
			}
			// End - Loop

			// connection.commit();

		} catch (SQLException sqle) {

			throw new SQLException("SQLException thrown " + "From BaseDAO(): "
					+ sqle.getMessage());

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			stmt1.close();
			stmt2.close();
			stmt3.close();
			stmt4.close();
			stmt5.close();
			connection.close();

		}

	}

	public void updateAnnotation(String polygonID,
			String transformedPolygonString,
			String transformedCoordinateStringWithZ) throws SQLException,
			Exception {

		// Used for Oracle connection
		Connection connection = getStandAloneConnectionForPostgres();
		PreparedStatement stmt1 = null;

		// Delete from annotation_sdo
		StringBuffer updateQuery = new StringBuffer();
		updateQuery.append(" update annotation_sdo ").append(
				" set transformed_sdo = ST_GeomFromText('POLYGON(("
						+ transformedPolygonString + "))'), ").append(
				" transformed_coordinate = '"
						+ transformedCoordinateStringWithZ + "' ").append(
				" where polygon_id = " + polygonID);

		String errorMessage = "";

		try {

			// Delete a record from 1st table
			System.out.println("BaseDAO - Update Query1 is - "
					+ updateQuery.toString());
			stmt1 = connection.prepareStatement(updateQuery.toString());

			if (stmt1.executeUpdate() != 1) {
				errorMessage = "Unable to update a record, please contact the administrator to resolve this issue";
			}

		} catch (SQLException sqle) {

			throw new SQLException("SQLException thrown " + "From BaseDAO(): "
					+ sqle.getMessage());

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			stmt1.close();
			connection.close();

		}

	}

	public AnnotationVO getTFWStringFromDB(String registrationID) {

		BaseDAO baseDAO = new BaseDAO();
		StringBuffer query = new StringBuffer();
		ArrayList list = new ArrayList();
		Connection conn = null;
		Statement stmt = null;
		AnnotationVO vo = null;

		try {

			conn = baseDAO.getStandAloneConnectionForOracle();
			stmt = conn.createStatement();

			query.append(" select CX, RX, RY, CY, IX, IY ").append(
					"from MOUSE_SPATIAL_REGISTRY ").append(
					"where REGISTRATION_ID = '" + registrationID + "' ");

			System.out
					.println("getImageRegistrationDataForSliceID - Query is - "
							+ query.toString());

			ResultSet rs = stmt.executeQuery(query.toString());

			while (rs.next()) {

				vo = new AnnotationVO();
				vo.setTfwLine1(rs.getString("CX"));
				vo.setTfwLine2(rs.getString("RX"));
				vo.setTfwLine3(rs.getString("RY"));
				vo.setTfwLine4(rs.getString("CY"));
				vo.setTfwLine5(rs.getString("IX"));
				vo.setTfwLine6(rs.getString("IY"));

			}

		} catch (SQLException sqle) {

			sqle.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return vo;

	}

	
	/**
	 * Gets the connection to the database. Does not do connection pooling.
	 * 
	 * @param jndiName
	 *            - The jndi name of the datasource
	 * @return A Connection object
	 * @throws DataAccessObjectException
	 *             - If there is an exception establishing the connection.
	 * 
	 */
	public Connection getStandAloneConnectionForOracle()// please change
			// public to
			// protected
			throws Exception {

		Connection connection = null;

		String driverClassName = "oracle.jdbc.driver.OracleDriver";
		String dbUrl = "jdbc:oracle:thin:@//oracle-rac1.crbs.ucsd.edu:1523/ccdbpri1";
		String dbUser = "atlas";
		String dbPassword = "beer";

		try {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		return connection;

	}// end of getStandAloneConnection method
    
    
}//end of DataAccessObject.java
