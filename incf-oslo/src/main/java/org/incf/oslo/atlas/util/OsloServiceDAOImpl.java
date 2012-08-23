/**
 * This class is used to deal with the database like create connection, query, etc.
 */
package org.incf.oslo.atlas.util; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Asif Memon
 *
 */
public class OsloServiceDAOImpl {

	private OsloConfigurator configurator = OsloConfigurator.INSTANCE;
	private static final Logger LOG = LoggerFactory
	.getLogger(OsloServiceDAOImpl.class);

	public ArrayList getSRSsData() {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.whs.09")+"','"+configurator.getValue("srsname.whs.10")+"'"; 
		
		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgresFromOslo();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		//query.append( " select * from srs where srs_name in (" + srsName + ") " );

		query.append( " select * from srs where status = 'ACTIVE' and srs_name in ('Rat_Paxinos_1.0', 'Rat_WHS_0.9', 'Rat_WHS_1.0')" );
		System.out.println("getSRSData - Query is - {}" + query.toString() );
		LOG.debug("getSRSData - Query is - {}", query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		OsloServiceVO vo = null;

		while ( rs.next() ) {

			vo = new OsloServiceVO();

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
		Connection conn = dao.getStandAloneConnectionForPostgresFromOslo();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from orientation " );

		LOG.debug("getOrientation - Query is - {}",query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		OsloServiceVO vo = null;

		while ( rs.next() ) {

			vo = new OsloServiceVO();

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


	public ArrayList getDescribeSRSData(String srsName) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();
		//String srsName = "'"+configurator.getValue("srsname.abareference.10")+"','"+configurator.getValue("srsname.abavoxel.10")+"','"+configurator.getValue("srsname.agea.10")+"'";

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgresFromOslo();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from srs where srs_name in ('" + srsName + "') " );

		System.out.println("Describe SRS - Query is - {}" + query.toString() );

		LOG.debug("getSRSData - Query is - {}", query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		OsloServiceVO vo = null;

		while ( rs.next() ) {

			vo = new OsloServiceVO();

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

	
	public ArrayList getSliceData( OsloServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgresFromOslo();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from slice where space_code = '"+vo.getSpaceCode()+"' " );

		LOG.debug("getSliceData - Query is - {}", query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		//ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new OsloServiceVO();

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


	public ArrayList getFiducialsData( OsloServiceVO vo ) {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgresFromOslo();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from fiducial where srs_name = '"+vo.getSpaceCode()+"' " );

		LOG.debug("getSliceData - Query is - {}", query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		//ABAServiceVO vo = null;

		while ( rs.next() ) {

			vo = new OsloServiceVO();

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


	public ArrayList getStructureData() {

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		//Used for postgres connection
		Connection conn = dao.getStandAloneConnectionForPostgres();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();
		query.append( " select * from incf_whs_structure " );

		LOG.debug("getStructureData - Query is - {}", query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString()); 
		OsloServiceVO vo = null;

		while ( rs.next() ) {

			vo = new OsloServiceVO();

			vo.setStructureID(rs.getString("structure_id"));
			vo.setStructureName(rs.getString("structure_name"));
			vo.setStructureDescription(rs.getString("description"));

			list.add(vo);

		}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public ArrayList getAnnotationData(OsloServiceVO vo, String polygonString) { 

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		try {

		Connection conn = dao.getStandAloneConnectionForWHSHub();
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

			vo = new OsloServiceVO();

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
	public ArrayList getAnnotationData(OsloServiceVO vo) { 

		ArrayList list = new ArrayList();
		BaseDAO dao = new BaseDAO();

		LOG.debug("Filter in database side: " + vo.getFilter());

		try {

		Connection conn = dao.getStandAloneConnectionForWHSHub();
		Statement stmt = conn.createStatement();
		StringBuffer query = new StringBuffer();

		query.append( " select m.id, srs_name, dataset_name, coordinates, polygon_id, depth, username, instance_id, onto_name, onto_uri, modified_time, transformed_sdo, transformed_coordinate from annotation_sdo s, annotation_dataset m " )
	         .append( " where m.dataset_name = '"+vo.getFilter()+"' " )
		     .append( " and s.id = m.id " );

		LOG.debug("getAnnotationData - Query is - {}" , query.toString() );

		ResultSet rs = stmt.executeQuery(query.toString());

		while ( rs.next() ) {

			vo = new OsloServiceVO();

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

}
