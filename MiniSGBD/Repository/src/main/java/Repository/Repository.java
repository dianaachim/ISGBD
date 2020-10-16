package Repository;

import Domain.Attribute;
import Domain.Databases;
import Domain.DatabasesNames;
import Domain.TablesNames;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository {


    public  Databases jaxbXMLToObject()
    {
        try
        {
            Databases db;
            File file = new File("date.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Databases.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
             db = (Databases) unmarshaller.unmarshal(file);
           // System.out.println(  unmarshaller.unmarshal(file));
           // System.out.println(db);
            if(db.getDb()==null)
                db.setDb(new ArrayList<>());
            else
                for ( DatabasesNames d: db.getDb())
                    if(d.getTablesNamesList()==null)
                        d.setTablesNamesList(new ArrayList<>());
                    else
                        for(TablesNames t: d.getTablesNamesList()) {
                            if (t.getAttributeList() == null)
                                t.setAttributeList(new ArrayList<>());
                            if(t.getIndexList()==null)
                                t.setIndexList(new ArrayList<>());
                        }

            return db;


        } catch (JAXBException e) {
            e.printStackTrace();
        }
    return null;
    }
    public  void jaxbObjectToXML(Databases dbs)
    {

        try
        {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Databases.class);
            System.out.println(dbs);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();

            File file = new File( "date.xml" );


            //Write XML to StringWriter
            jaxbMarshaller.marshal(dbs, file);

            //Verify XML Content
            String xmlContent = sw.toString();
            System.out.println( xmlContent );

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
