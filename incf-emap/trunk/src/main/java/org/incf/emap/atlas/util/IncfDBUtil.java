/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.incf.emap.atlas.util;

import java.util.*;
import java.sql.*;
import java.io.*;

/**
 *
 * @author amemon
 */

public class IncfDBUtil {
	
    PostgresDBService db = new PostgresDBService();

    private  int nextSequenceValue() throws Exception
    {
        int id = -1;
        String sql = "select nextval('general_sequence')";
        PostgresDBService db = new PostgresDBService();
          Connection c = db.getConnection();
         PreparedStatement ps = c.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();

         if(rs.next())
             id = rs.getInt(1);

         c.close();

         return id;
    }
    private int getResourceID(String resourcePath)throws Exception
    {
        Integer id = null;
        String sql = "select id from annotation_dataset where dataset_name = ?";
        Connection c = db.getConnection();
        PreparedStatement ps  = c.prepareStatement(sql);
        ps.setString(1, resourcePath);
        ResultSet rs = ps.executeQuery();

        if(rs.next())
            id = rs.getInt(1);
        else
        {
            int tid = this.nextSequenceValue();
            sql = "insert into annotation_dataset(id, dataset_name) values(?,?)";
            ps  = c.prepareStatement(sql);
            ps.setInt(1, tid);
            ps.setString(2, resourcePath);
            ps.executeUpdate();
            id = tid;
        }
        c.close();

        return id;
    }


    private void deleteOldPolygons(int id)throws Exception
    {
        String sql = "delete from annotation_sdo where id = ?";
         Connection c = db.getConnection();
        PreparedStatement ps  = c.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        c.close();
        
    }


    public void insertAnnotation(AnnotationModel model)throws Exception
    {
        int id = this.getResourceID(model.getResourcePath());
        this.deleteOldPolygons(id);
        Vector<PolygonModel> vp = model.getVp();
        for(int i=0;i<vp.size();i++)
        {
            int polygonID =this.insertPolygon(vp.get(i), id, model.getResourcePath());

            try
            {
                String prefix= "imageRegistrationID_";
                if(model.getResourcePath().toLowerCase().startsWith(prefix))
                {
                    String regID = model.getResourcePath().substring(prefix.length(),model.getResourcePath().length());
                    String polygonString = this.getPolygonString(vp.get(i));


                    XMLReader transformation = new XMLReader();
                    transformation.updateCoordinates(regID, polygonString, polygonID+"");
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private String polygonToString(PolygonModel m)throws Exception
    {
       Vector<Annot_Point> v = m.getvPoint();
       StringBuffer buff = new StringBuffer();
       for(int i=0;i<  v.size();i++)
       {
           Annot_Point p = v.get(i);
           if(i>0)
               buff.append(", ");
           buff.append(p.getX()+" "+p.getY());

       }
       return buff.toString();
    }



    private boolean isInteger(String d)
    {
        boolean r = true;

        try
        {
            Integer.parseInt(d);
        }
        catch(Exception e)
        {
            r = false;
        }

        return r;
    }



    private int insertPolygon(PolygonModel pm,int id, String resourcePath)throws Exception
    {
        int pid = pm.getPolygonID();
        String sql = "insert into annotation_sdo(polygon_id,id, sdo, depth, coordinates, username,instance_id ,onto_name ,onto_uri, modified_time) values(?,?, "+this.getPolygonString(pm)+ ", ?, ?,?,?,?,?,?)";
        Connection c = db.getConnection();
         PreparedStatement ps  = c.prepareStatement(sql);
        ps.setInt(1, pid);
        ps.setInt(2, id);
        ps.setDouble(3, this.getDepth(pm));

        String coord = pm.toString();
        System.out.println(coord);
        ps.setString(4, coord);
        ps.setString(5, pm.getUserName());
        java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());

        if(pm.getGp() != null)
        {
            HasGeometryProperty gp = pm.getGp();
            ps.setString(6, gp.getInstanceID());
            ps.setString(7, gp.getOntoName());
            ps.setString(8, gp.getOntoUri());
            ps.setTimestamp(9, ts);
        }
        else
        {
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setTimestamp(9, ts);
        }


        ps.executeUpdate();
        c.close();


        if(this.isInteger(resourcePath))
        {
            int rid = Integer.parseInt(resourcePath);
            this.updateRegistrationID(rid, pid);

           XMLReader test = new XMLReader();

           String d2Coord = pm.to2DString();

            test.updateCoordinates(rid+"", d2Coord, pid+"");
        }

        return pid;

    }


    private void updateRegistrationID(int registrationID, int sdo_id) {
    	
        System.err.println("------------------updateRegistrationID: regID:"+registrationID+"----------sdo_id:"+sdo_id);
        String aba = "Mouse_ABAreference_1.0";
        try
        {
            String sql = "update annotation_sdo set srs_name = ?, registration_id=? where polygon_id =?";
            Connection c= db.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, aba);
            ps.setInt(2, registrationID);
            ps.setInt(3, sdo_id);
            ps.executeUpdate();

            c.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }


    private String getUsername(PolygonModel pm)
    {
        return pm.getUserName();
    }

    private double getDepth(PolygonModel pm)
    {
       return  pm.getvPoint().get(0).getZ();
    }

    private String getPolygonString(PolygonModel pm)
    {
        StringBuffer buff = new StringBuffer();
        buff.append("ST_GeomFromText('POLYGON((");
        Vector<Annot_Point> vp = pm.getvPoint();
        for(int i=0;i<vp.size();i++)
        {
           Annot_Point p = vp.get(i);

           if(i > 0)
               buff.append(",");
           buff.append(p.getX()+" "+p.getY());

        }
        buff.append("))')");

        System.err.println(buff.toString());
        return buff.toString();
    }


	public String getXMLString(String xmlPath) { 

		String xmlString = "";

		try {
			//String xmlPath = "C:\\Users\\AsifMemon\\Desktop\\Annotations.xml";
			RandomAccessFile file = new RandomAccessFile( xmlPath, "r");
			int length = (int) file.length();
			byte[] a = new byte[length];
			file.readFully( a, 0, length );
			xmlString = new String( a );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return xmlString;

	}

    public static void main(String[] args)throws Exception {

    	org.incf.emap.atlas.util.PostgresDBService.URL = "jdbc:postgresql://incf-dev-local.crbs.ucsd.edu:5432/atlas-aba-db";
    	org.incf.emap.atlas.util.PostgresDBService.USERNAME = "atlas-aba-db";
    	org.incf.emap.atlas.util.PostgresDBService.PASSWORD = "aba4321";

        XML2AnnotObjects xml2o = new XML2AnnotObjects();
        File f = new File("C:\\Documents and Settings\\Administrator\\Desktop\\Annotation1.xml");

        //String xml = xml2o.fileToXML(f);
        IncfDBUtil util = new IncfDBUtil();
        String xml = util.getXMLString("C:\\Documents and Settings\\Administrator\\Desktop\\Annotation1.xml");
        System.out.println(xml);
        AnnotationModel amodel = xml2o.xml2polygons(xml);
        System.out.println("Resource path:"+amodel.getResourcePath());

        IncfDBUtil iutil = new IncfDBUtil();
        iutil.insertAnnotation(amodel);

       // PostgresDBUtil util = new PostgresDBUtil();
       // int id = util.getResourceID("test2");
       // System.out.println(id);
    }

}
