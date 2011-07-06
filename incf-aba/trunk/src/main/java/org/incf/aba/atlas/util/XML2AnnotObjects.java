/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.incf.aba.atlas.util;

import java.util.*;
import java.io.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 *
 * @author amemon
 */
public class XML2AnnotObjects
{
    public static final String ENCODING = "UTF-8";

    public InputStream fromString(String str) throws Exception
    {
        byte[] bytes = str.getBytes(ENCODING);
        return new ByteArrayInputStream(bytes);
    }


    public AnnotationModel xml2polygons(String xml)throws Exception
    {
        AnnotationModel amodel = new AnnotationModel();
        Vector<PolygonModel> vm = new Vector<PolygonModel>();
        InputStream st = this.fromString(xml);
        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(new ByteArrayInputStream(xml.getBytes()));
        Element root = doc.getRootElement();

        Element resource =root.getChild("RESOURCE");
        String filepath = resource.getAttributeValue("filepath");
        amodel.setResourcePath(filepath);

        HashMap<String,HasGeometryProperty> omap = new HashMap<String,HasGeometryProperty>();
        Element ontoModel = root.getChild("GENERAL_ONTO_MODEL");
        Element relation = ontoModel.getChild("RELATIONS");
        List<Element> propList = relation.getChildren("ONTO_PROPERTY");

        for(int i=0;i<propList.size();i++)
        {
            Element ep = propList.get(i);
            HasGeometryProperty  gp = this.getGeometryProperty(ep);
            if(gp != null)
                omap.put(gp.getPolygonID()+"", gp);
        }

        Element geometries = root.getChild("GEOMETRIES");
        List<Element> glist = geometries.getChildren();
        
        for(int i=0;i<glist.size();i++)
        {
            Element ge = glist.get(i);
            PolygonModel model = this.getPolygonModel(ge);
            int pid = model.getPolygonID();
            if(omap.containsKey(pid+""))
            {
                model.setGp(omap.get(pid+""));
            }
            
            vm.addElement(model);
        }
        amodel.setVp(vm);

//        System.out.println(root);

        return amodel;
    }
    
    
    private PolygonModel getPolygonModel(Element ge)throws Exception
    {
        PolygonModel model = new PolygonModel();
        System.out.println(ge);

        String userName =  ge.getAttributeValue("user_name");
        
        Element pge = ge.getChild("POLYGON");

        String ID = pge.getAttributeValue("ID");

        model.setUserName(userName);

        Integer id = Integer.parseInt(ID);
        model.setPolygonID(id);
        System.err.println("PolygonID:"+model.getPolygonID());

        List<Element> plist = pge.getChildren("POINT");
        
        
        
        for(int i=0;i<plist.size();i++)
        {
            Element p = plist.get(i);
            String loc = p.getText();
            StringTokenizer st = new StringTokenizer(loc,",");
            String sx = st.nextToken();
            String sy = st.nextToken();
            String sz = st.nextToken();
            
            double x = Double.parseDouble(sx);
            double y = Double.parseDouble(sy);
            double z = Double.parseDouble(sz);
            
            Annot_Point point = new Annot_Point();
            point.setX(x);
            point.setY(y);
            point.setZ(z);

            model.getvPoint().addElement(point);
        }
        return model;
        
    }
    
    
    
    public String fileToXML(File f)throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(f));
        StringBuffer buff = new StringBuffer();
        String line = br.readLine();
        while(line != null)
        {
            System.out.println(buff.append(line+"\n"));
            line = br.readLine();
        }
        br.close();
        
        return buff.toString();
        
    }




    private HasGeometryProperty getGeometryProperty(Element oproperty)
    {
        
       String ontoName = oproperty.getAttributeValue("onto_name");
       if(!ontoName.equals("has_geometry"))
           return null;
       
       HasGeometryProperty prop = new HasGeometryProperty();
       
       Element subject = oproperty.getChild("SUBJECT");
       Element ontoTerm = subject.getChild("ONTO_TERM");
       String instID = ontoTerm.getAttributeValue("instance_id");
       String oName = ontoTerm.getAttributeValue("onto_name");
       String ouri = ontoTerm.getAttributeValue("onto_uri");
       
       prop.setInstanceID(instID);
       prop.setOntoName(oName);
       prop.setOntoUri(ouri);
       
       Element object = oproperty.getChild("OBJECT");
       Element geometry = object.getChild("GEOMETRY");
       if(geometry == null)
           return null;
       Element polygon = geometry.getChild("POLYGON");
       String spid = polygon.getAttributeValue("ID");
       int pid = Integer.parseInt(spid);
       prop.setPolygonID(pid);
       
       return prop;
    }


    public static void main(String[] args)throws Exception
    {

    	org.incf.aba.atlas.util.PostgresDBService.URL = "jdbc:postgresql://ceecee.ucsd.edu:5432/ccdbv2_2";
    	org.incf.aba.atlas.util.PostgresDBService.USERNAME = "ccdbd_dev";
    	org.incf.aba.atlas.util.PostgresDBService.PASSWORD = "sand|ego";

        XML2AnnotObjects xml2o = new XML2AnnotObjects();
        File f = new File("C:\\Users\\wawong\\Desktop\\wibdb.pl.xml");
        String xml = xml2o.fileToXML(f);
        System.out.println(xml);
        AnnotationModel amodel = xml2o.xml2polygons(xml);
        IncfDBUtil util = new IncfDBUtil();
        util.insertAnnotation(amodel);


       // System.out.println("AnnotationModel:"+amodel);
    }

}
