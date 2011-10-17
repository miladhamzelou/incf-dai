/**
 * This class is used to deal with the database like create connection, query, etc.
 */
package org.incf.ucsd.atlas.util; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.incf.ucsd.atlas.util.BaseDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Asif Memon
 *
 */
public class UCSDServiceDAOImpl {

	private UCSDConfigurator configurator = UCSDConfigurator.INSTANCE;

	private static final Logger LOG = LoggerFactory
	.getLogger(UCSDServiceDAOImpl.class);

	public ArrayList getSRSsData() {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";
		String srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"','Mouse_Yuko_1.0'";
		
		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForUCSDHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from srs where srs_name in (" + srsName + ") and STATUS = 'ACTIVE'" );

		LOG.debug("getSRSData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		UCSDServiceVO vo = null;

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setSrsCode(rs.getString("SRS_CODE"));
			vo.setSrsName(rs.getString("SRS_Name"));
			vo.setSrsDescription(rs.getString("SRS_DESCRIPTION"));
			vo.setSrsAuthorCode(rs.getString("SRS_AUTHOR_CODE"));
			vo.setSrsDateSubmitted(rs.getString("SRS_DATE_SUBMITTED"));
			vo.setOrigin(rs.getString("ORIGIN"));
			vo.setUnitsAbbreviation(rs.getString("UNITS_ABBREVIATION"));
			vo.setUnitsName(rs.getString("UNITS_NAME"));
			vo.setNeuroPlusXCode(rs.getString("NEURO_PLUS_X_CODE"));
			vo.setNeuroMinusXCode(rs.getString("NEURO_MINUS_X_CODE"));
			vo.setNeuroPlusYCode(rs.getString("NEURO_PLUS_Y_CODE"));
			vo.setNeuroMinusYCode(rs.getString("NEURO_MINUS_Y_CODE"));
			vo.setNeuroPlusZCode(rs.getString("NEURO_PLUS_Z_CODE"));
			vo.setNeuroMinusZCode(rs.getString("NEURO_MINUS_Z_CODE"));
			vo.setSourceURI(rs.getString("SOURCE_URI"));
			vo.setSourceFileFormat(rs.getString("SOURCE_FILE_FORMAT"));
			vo.setSrsAbstract(rs.getString("ABSTRACT"));
			vo.setDerivedFromSRSCode(rs.getString("DERIVED_FROM_SRS_CODE"));
			vo.setDerivedMethod(rs.getString("DERIVED_METHOD"));
			vo.setSpecies(rs.getString("SPECIES"));
			vo.setSrsBase(rs.getString("SRS_BASE"));
			vo.setRegionOfValidity(rs.getString("REGION_OF_VALIDITY"));
			vo.setRegionURI(rs.getString("REGION_URI"));
			vo.setSrsVersion(rs.getString("SRS_VERSION"));
			vo.setDimensionMinX(rs.getString("DIMENSION_MIN_X"));
			vo.setDimensionMaxX(rs.getString("DIMENSION_MAX_X"));
			vo.setDimensionMinY(rs.getString("DIMENSION_MIN_Y"));
			vo.setDimensionMaxY(rs.getString("DIMENSION_MAX_Y"));
			vo.setDimensionMinZ(rs.getString("DIMENSION_MIN_Z"));
			vo.setDimensionMaxZ(rs.getString("DIMENSION_MAX_Z"));

			list.add(vo);
			
		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList getDescribeSRSData(String srsName) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";
		//srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"'";
		//srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"','Mouse_Yuko_1.0'";

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForUCSDHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from srs where srs_name in ('" + srsName + "') and STATUS = 'ACTIVE'" ); 

		LOG.debug("getSRSData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		UCSDServiceVO vo = null;

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setSrsCode(rs.getString("SRS_CODE"));
			vo.setSrsName(rs.getString("SRS_Name"));
			vo.setSrsDescription(rs.getString("SRS_DESCRIPTION"));
			vo.setSrsAuthorCode(rs.getString("SRS_AUTHOR_CODE"));
			vo.setSrsDateSubmitted(rs.getString("SRS_DATE_SUBMITTED"));
			vo.setOrigin(rs.getString("ORIGIN"));
			vo.setUnitsAbbreviation(rs.getString("UNITS_ABBREVIATION"));
			vo.setUnitsName(rs.getString("UNITS_NAME"));
			vo.setNeuroPlusXCode(rs.getString("NEURO_PLUS_X_CODE"));
			vo.setNeuroMinusXCode(rs.getString("NEURO_MINUS_X_CODE"));
			vo.setNeuroPlusYCode(rs.getString("NEURO_PLUS_Y_CODE"));
			vo.setNeuroMinusYCode(rs.getString("NEURO_MINUS_Y_CODE"));
			vo.setNeuroPlusZCode(rs.getString("NEURO_PLUS_Z_CODE"));
			vo.setNeuroMinusZCode(rs.getString("NEURO_MINUS_Z_CODE"));
			vo.setSourceURI(rs.getString("SOURCE_URI"));
			vo.setSourceFileFormat(rs.getString("SOURCE_FILE_FORMAT"));
			vo.setSrsAbstract(rs.getString("ABSTRACT"));
			vo.setDerivedFromSRSCode(rs.getString("DERIVED_FROM_SRS_CODE"));
			vo.setDerivedMethod(rs.getString("DERIVED_METHOD"));
			vo.setSpecies(rs.getString("SPECIES"));
			vo.setSrsBase(rs.getString("SRS_BASE"));
			vo.setRegionOfValidity(rs.getString("REGION_OF_VALIDITY"));
			vo.setRegionURI(rs.getString("REGION_URI"));
			vo.setSrsVersion(rs.getString("SRS_VERSION"));
			vo.setDimensionMinX(rs.getString("DIMENSION_MIN_X"));
			vo.setDimensionMaxX(rs.getString("DIMENSION_MAX_X"));
			vo.setDimensionMinY(rs.getString("DIMENSION_MIN_Y"));
			vo.setDimensionMaxY(rs.getString("DIMENSION_MAX_Y"));
			vo.setDimensionMinZ(rs.getString("DIMENSION_MIN_Z"));
			vo.setDimensionMaxZ(rs.getString("DIMENSION_MAX_Z"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public ArrayList getOrientationData() {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from orientation " );

		LOG.debug("getOrientation - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		UCSDServiceVO vo = null;

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setOrientationCode(rs.getString("ORIENTATION_CODE"));
			vo.setOrientationName(rs.getString("ORIENTATION_NAME"));
			vo.setOrientationDescription(rs.getString("ORIENTATION_DESCRIPTION"));
			vo.setOrientationAuthor(rs.getString("AUTHOR"));
			vo.setOrientationDateSubmitted(rs.getString("DATE_SUBMITTED"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public ArrayList getSliceData( UCSDServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from slice where space_code = '"+vo.getSpaceCode()+"' " );

		LOG.debug("getSliceData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		//ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setSpaceCode(rs.getString("space_code"));
			vo.setSlideValueOrigin(rs.getString("slide_value_origin"));
			vo.setValueDirection(rs.getString("value_direction"));
			vo.setRightDirection(rs.getString("right_direction"));
			vo.setUpDirection(rs.getString("up_direction"));
			vo.setPlusX(rs.getString("plus_x"));
			vo.setPlusY(rs.getString("plus_y"));
			vo.setPlusZ(rs.getString("plus_z"));
			vo.setSliceID(rs.getString("slice_id"));
			vo.setSlideValue(rs.getString("slide_value"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public ArrayList getFiducialsData( UCSDServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from fiducial where srs_name = '"+vo.getSpaceCode()+"' and STATUS = 'ACTIVE'" );

		LOG.debug("getSliceData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		//ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setFiducialCode(rs.getString("fiducial_code"));
			vo.setFiducialName(rs.getString("fiducial_name"));
			vo.setFiducialType(rs.getString("fiducial_type"));
			vo.setDerivedFrom(rs.getString("derived_from"));
			vo.setAuthorCode(rs.getString("author_code"));
			vo.setCertaintyLevel(rs.getString("certainty_level"));
			vo.setSrsName(rs.getString("srs_name"));
			vo.setDescription(rs.getString("description"));
			vo.setDateSubmitted(rs.getString("date_submitted"));
			vo.setDateUpdated(rs.getString("date_updated"));
			vo.setPos(rs.getString("pos"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public String[] getCoordinateRangeForSRS( String srsName ) { 

		BaseDAO dao = new BaseDAO();
		String[] coordinatesRange = new String[6];
		srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"'";

		LOG.debug("SrsName: {}" , srsName);

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select dimension_min_x, dimension_max_x, dimension_min_y, dimension_max_y, dimension_min_z, dimension_max_z " )
		.append(" from srs ")
		.append(" where srs_name = '"+srsName+"' ");

		LOG.debug("Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString());

		while ( rs.next() ) {

			coordinatesRange[0] = rs.getString("dimension_min_x");
			coordinatesRange[1] = rs.getString("dimension_max_x");
			coordinatesRange[2] = rs.getString("dimension_min_y");
			coordinatesRange[3] = rs.getString("dimension_max_y");
			coordinatesRange[4] = rs.getString("dimension_min_z");
			coordinatesRange[5] = rs.getString("dimension_max_z");

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return coordinatesRange;
	}

	
	public ArrayList getAnnotationData(UCSDServiceVO vo, String polygonString) { 

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection from ccdb
		Connection conn = dao.getStandAloneConnectionForUCSDHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		//query.append( " select srs_name, dataset_name, coordinates, polygon_id, depth, username, instance_id, onto_name, onto_uri, modified_time, transformed_sdo, transformed_coordinate from annotation_sdo s, annotation_dataset m " )
		     //.append( " where ST_INTERSECTS(sdo, ST_GEOMFROMTEXT('POLYGON((6333 6163, 6163 6163, 6163 6333, 6333 6333, 6333 6163))')) " );
			 //.append( " and s.id = m.id " );

		query.append( " select m.id, srs_name, dataset_name, coordinates, polygon_id, depth, username, instance_id, onto_name, onto_uri, modified_time, transformed_sdo, transformed_coordinate from annotation_sdo s, annotation_dataset m " )
	         .append( " where ST_INTERSECTS(transformed_sdo, ST_GEOMFROMTEXT('POLYGON(("+polygonString+"))')) " )
		     .append( " and s.id = m.id " );

		LOG.debug("getAnnotationData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setUniqueID(rs.getString("id"));
			vo.setOntoFilePath(rs.getString("dataset_name"));
			vo.setSrsName(rs.getString("srs_name"));
			vo.setCoordinates(rs.getString("coordinates"));
			vo.setPolygonID(rs.getString("polygon_id"));
			vo.setDepth(rs.getString("depth"));
			vo.setUserName(rs.getString("username"));
			vo.setInstanceID(rs.getString("instance_id"));
			vo.setOntoName(rs.getString("onto_name"));
			vo.setOntoURI(rs.getString("onto_uri"));
			vo.setUpdatedTime(rs.getDate("modified_time"));
			vo.setTransformedCoordinates(rs.getString("transformed_coordinate"));
			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//Method override
	public ArrayList getAnnotationData(UCSDServiceVO vo) { 

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		LOG.debug("Filter in database side: " + vo.getFilter());

		try {

		//Used for postgres connection from ccdb
		Connection conn = dao.getStandAloneConnectionForUCSDHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();

		query.append( " select m.id, srs_name, dataset_name, coordinates, polygon_id, depth, username, instance_id, onto_name, onto_uri, modified_time, transformed_sdo, transformed_coordinate from annotation_sdo s, annotation_dataset m " )
	         .append( " where m.dataset_name = '"+vo.getFilter()+"' " )
		     .append( " and s.id = m.id " );

		LOG.debug("getAnnotationData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setUniqueID(rs.getString("id"));
			vo.setOntoFilePath(rs.getString("dataset_name"));
			vo.setSrsName(rs.getString("srs_name"));
			vo.setCoordinates(rs.getString("coordinates"));
			vo.setPolygonID(rs.getString("polygon_id"));
			vo.setDepth(rs.getString("depth"));
			vo.setUserName(rs.getString("username"));
			vo.setInstanceID(rs.getString("instance_id"));
			vo.setOntoName(rs.getString("onto_name"));
			vo.setOntoURI(rs.getString("onto_uri"));
			vo.setUpdatedTime(rs.getDate("modified_time"));
			vo.setTransformedCoordinates(rs.getString("coordinates"));
			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public String updateSRSs(UCSDServiceVO vo) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";
		String srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"'";

		String statusMessage = "";

		try {

			Connection connection = dao.getStandAloneConnectionForUCSDHub();
		    PreparedStatement stmt1 = null;
			StringBuffer updateQuery = new StringBuffer();
			updateQuery.append("update srs set status = 'INACTIVE' where srs_name = '"
							+ vo.getSrsName() + "'");
		
		    PreparedStatement stmt2 = null;
			StringBuffer insertQuery = new StringBuffer();
			insertQuery.append(
					" INSERT INTO srs( " + 
					"       srs_code, srs_name, srs_description, srs_author_code, srs_date_submitted,  " + 
					"       origin, units_abbreviation, units_name, neuro_plus_x_code, neuro_minus_x_code, " + 
					"       neuro_plus_y_code, neuro_minus_y_code, neuro_plus_z_code, neuro_minus_z_code, " +
					"       source_uri, source_description_uri, source_file_format, abstract,  " +
					"       derived_from_srs_code, derived_method, species, srs_base, region_of_validity, " + 
					"       region_uri, srs_version, dimension_min_x, dimension_max_x, dimension_min_y,  " +
					"       dimension_max_y, dimension_min_z, dimension_max_z, status) " +
				    " VALUES ('"+vo.getSrcSRSCode()+"', '"+vo.getSrsName()+"', '"+vo.getSrsDescription()+"', '"+vo.getAuthorCode()+"', '"+vo.getDateSubmitted()+"',  '" +
				    vo.getOrigin()+"', '"+vo.getUnitsAbbreviation()+"', '"+vo.getUnitsName()+"', '"+vo.getNeuroPlusXCode()+"', '"+vo.getNeuroMinusXCode()+"', '" +
				    vo.getNeuroPlusYCode()+"', '"+vo.getNeuroMinusYCode()+"', '"+vo.getNeuroPlusZCode()+"', '"+vo.getNeuroMinusZCode()+"', '" +
				    vo.getSourceURI()+"', '"+vo.getSourceDescriptionURI()+"', '"+vo.getSourceFileFormat()+"', '"+vo.getSrsAbstract()+"', '" +
				    vo.getDerivedFromSRSCode()+"', '"+vo.getDerivedMethod()+"', '"+vo.getSpecies()+"', '"+vo.getSrsBase()+"', '"+vo.getRegionOfValidity()+"', '"  +
				    vo.getRegionURI()+"', "+vo.getSrsVersion()+", "+vo.getDimensionMinX()+", "+vo.getDimensionMaxX()+", "+vo.getDimensionMinY()+", " +
				    vo.getDimensionMaxY()+", "+vo.getDimensionMinZ()+", "+vo.getDimensionMaxZ()+", '"+vo.getStatus() + "' )"); 

				//Delete a record from 2nd table
				System.out.println("Update SRS - Query is - " + updateQuery.toString() );

				stmt1 = connection.prepareStatement(updateQuery.toString());

				stmt1.executeUpdate();
				
/*				if (stmt1.executeUpdate() != 1) {
					statusMessage = "Error: Unable to delete a record, please contact the administrator to resolve this issue";
				}
*/				System.out.println("Query 1 Done");

				System.out.println("BaseDAO - InsertQuery is - "
						+ insertQuery.toString());
				stmt2 = connection.prepareStatement(insertQuery.toString());
				if (stmt2.executeUpdate() != 1) {
					statusMessage = "Error: Unable to insert a record, please contact the administrator to resolve this issue";
				} else {
					statusMessage = "Successfully registered the data to the hub";
				}

				System.out.println("Query 2 Done");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusMessage;
	}


	public String updateFiducials(UCSDServiceVO vo) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";
		String srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"'";

		String statusMessage = "";

		try {

			Connection connection = dao.getStandAloneConnectionForUCSDHub();
		    PreparedStatement stmt1 = null;
			StringBuffer updateQuery = new StringBuffer();
			updateQuery.append("update fiducial set status = 'INACTIVE' where fiducial_name = '"+ vo.getFiducialName() + "'");

		    PreparedStatement stmt2 = null;
			StringBuffer insertQuery = new StringBuffer();

			insertQuery.append(
					" INSERT INTO fiducial( " + 
					"       fiducial_code, fiducial_name, fiducial_type, derived_from, author_code,  " + 
					"       certainty_level, srs_name, description, date_submitted, " + 
					"       date_updated, pos, status) " + 
				    " VALUES ('"+vo.getFiducialCode()+"', '"+vo.getFiducialName()+"', '"+vo.getFiducialType()+"', '"+vo.getDerivedFrom()+"', '"+vo.getAuthorCode()+"', '" +
				    vo.getCertaintyLevel()+"', '"+vo.getSrsName()+"', '"+vo.getDescription()+"', '"+vo.getDateSubmitted()+"', '" +
				    vo.getDateUpdated()+"', '"+vo.getPos()+"', '"+vo.getStatus()+ "' )");

				//Delete a record from 2nd table
 				System.out.println("Update Fiducial - Query is - " + updateQuery.toString() );

				stmt1 = connection.prepareStatement(updateQuery.toString());

				stmt1.executeUpdate();

/*				if (stmt1.executeUpdate() != 1) {
					statusMessage = "Error: Unable to delete a record, please contact the administrator to resolve this issue";
				}
*/				System.out.println("Query 1 Done");

				System.out.println("BaseDAO - InsertQuery is - "
						+ insertQuery.toString());
				stmt2 = connection.prepareStatement(insertQuery.toString());
				if (stmt2.executeUpdate() != 1) {
					statusMessage = "Error: Unable to insert a record, please contact the administrator to resolve this issue";
				} else {
					statusMessage = "Successfully registered the data to the hub";
				}

				System.out.println("Query 2 Done");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusMessage;
	}


	public String updateSpaceTransformation(UCSDServiceVO vo) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";
		String srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"'";

		String statusMessage = "";

		try {

			Connection connection = dao.getStandAloneConnectionForUCSDHub();
		    PreparedStatement stmt1 = null;
			StringBuffer updateQuery = new StringBuffer();
			updateQuery.append("update space_transformations set status = 'INACTIVE' where source = '"+ vo.getTransformationSource() + "' and destination = '"+ vo.getTransformationDestination()+"'");

		    PreparedStatement stmt2 = null;
			StringBuffer insertQuery = new StringBuffer();

			insertQuery.append(
					" INSERT INTO space_transformations( " + 
					"       source, destination, hub, status, transformation_url)  " + 
				    " VALUES ('"+vo.getTransformationSource()+"', '"+vo.getTransformationDestination()+"', '"+vo.getTransformationHub()+"', '"+vo.getStatus()+"', '"+vo.getTransformationURL()+"')" );

				//Delete a record from 2nd table
 				System.out.println("Update Space Trasnformation - Query is - " + updateQuery.toString() );

				stmt1 = connection.prepareStatement(updateQuery.toString());

				stmt1.executeUpdate();

/*				if (stmt1.executeUpdate() != 1) {
					statusMessage = "Error: Unable to delete a record, please contact the administrator to resolve this issue";
				}
*/				System.out.println("Query 1 Done");

				System.out.println("BaseDAO - InsertQuery is - "
						+ insertQuery.toString());
				stmt2 = connection.prepareStatement(insertQuery.toString());
				if (stmt2.executeUpdate() != 1) {
					statusMessage = "Error: Unable to insert a record, please contact the administrator to resolve this issue";
				} else {
					statusMessage = "Successfully registered the data to the hub";
				}

				System.out.println("Query 2 Done");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusMessage;
	}

	
	public String updateSlices(UCSDServiceVO vo) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";
		String srsName = "'"+configurator.getValue("srsname.paxinos.10")+"','"+configurator.getValue("srsname.ucsdnewsrs.10")+"'";

		String statusMessage = "";

		try {

			Connection connection = dao.getStandAloneConnectionForUCSDHub();
		    PreparedStatement stmt1 = null;
			StringBuffer updateQuery = new StringBuffer();
			updateQuery.append("update slice set status = 'INACTIVE' where slice_id = '"+ vo.getSliceID() + "' and space_code = '" +vo.getSpaceCode()+ "'"); 

		    PreparedStatement stmt2 = null;
			StringBuffer insertQuery = new StringBuffer();

			insertQuery.append(
					" INSERT INTO slice( " + 
					"       space_code, slide_value_origin, value_direction, right_direction, " + 
					"       up_direction, plus_x, plus_y, plus_z, slice_id, slide_value, " + 
					"       status) " + 
				    " VALUES ('"+vo.getSpaceCode()+"', '"+vo.getSlideValueOrigin()+"', '"+vo.getValueDirection()+"', '"+vo.getRightDirection()+"', '" +
				    vo.getUpDirection()+"', '"+vo.getPlusX()+"', '"+vo.getPlusY()+"', '"+vo.getPlusZ()+"', '" +
				    vo.getSliceID()+"', '"+vo.getSlideValue()+"', '"+vo.getStatus()+ "' )");

				//Delete a record from 2nd table
 				System.out.println("Update Space Code - Query is - " + updateQuery.toString() );

				stmt1 = connection.prepareStatement(updateQuery.toString());

				stmt1.executeUpdate();

/*				if (stmt1.executeUpdate() != 1) {
					statusMessage = "Error: Unable to delete a record, please contact the administrator to resolve this issue";
				}
*/				System.out.println("Query 1 Done");

				System.out.println("BaseDAO - InsertQuery is - "
						+ insertQuery.toString());
				stmt2 = connection.prepareStatement(insertQuery.toString());
				if (stmt2.executeUpdate() != 1) {
					statusMessage = "Error: Unable to insert a record, please contact the administrator to resolve this issue";
				} else {
					statusMessage = "Successfully registered the data to the hub";
				}

				System.out.println("Query 2 Done");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusMessage;
	}


	public ArrayList getAllSpaceTransformationData( UCSDServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForUCSDHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from space_transformations where status = 'ACTIVE' " );

		LOG.debug("getSpaceTransformationData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 

		while ( rs.next() ) {

			vo = new UCSDServiceVO();

			vo.setTransformationSource(rs.getString("source"));
			vo.setTransformationDestination(rs.getString("destination"));
			vo.setTransformationHub(rs.getString("hub"));
			vo.setTransformationURL(rs.getString("transformation_url"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public ArrayList getUCSDSpaceTransformationData( UCSDServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForUCSDHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from space_transformations where status = 'ACTIVE' and hub = 'UCSD'" ); 

		LOG.debug("getSpaceTransformationData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 

		while ( rs.next() ) {

			vo = new UCSDServiceVO();
			
			vo.setTransformationSource(rs.getString("source"));
			vo.setTransformationDestination(rs.getString("destination"));
			vo.setTransformationHub(rs.getString("hub"));
			vo.setTransformationURL(rs.getString("transformation_url"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
