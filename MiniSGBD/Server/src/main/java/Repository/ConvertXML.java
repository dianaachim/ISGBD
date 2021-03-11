package Repository;

import Domain.Database;
import Domain.Databases;
import Domain.Table;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

public class ConvertXML {


    public  void jaxbObjectToXML(Databases dbs) throws JAXBException {


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

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        //Write XML to StringWriter
            jaxbMarshaller.marshal(dbs, file);

            //Verify XML Content
            String xmlContent = sw.toString();
            System.out.println( xmlContent );

    }

    public  Databases jaxbXMLToObject() throws JAXBException {
            Databases db;
            File file = new File("date.xml");
//            try {
//                if (!file.exists()) {
//                    file.createNewFile();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            JAXBContext jaxbContext = JAXBContext.newInstance(Databases.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            db = (Databases) unmarshaller.unmarshal(file);
            // System.out.println(  unmarshaller.unmarshal(file));
            // System.out.println(db);
            if(db.getDbList()==null)
                db.setDbList(new ArrayList<>());
            else
                for ( Database d: db.getDbList())
                    if(d.getTablesList()==null)
                        d.setTablesList(new ArrayList<>());
                    else
                        for(Table t: d.getTablesList()) {
                            if (t.getAttributeList() == null)
                                t.setAttributeList(new ArrayList<>());
                            if(t.getIndexList()==null)
                                t.setIndexList(new ArrayList<>());
                        }

            return db;
    }
}
