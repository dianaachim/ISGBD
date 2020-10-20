package Repository;

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

public class Repository {

    public Databases jaxbXMLToObject() {
        try {
            Databases db = null;
            File file = new File("date.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Databases.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            if (db.getDb()==null) {
                db.setDb(new ArrayList<>());
            } else {
                for (DatabasesNames d : db.getDb()){
                    if (d.getTablesNamesList()==null)
                        d.setTablesNamesList(new ArrayList<>());
                    else {
                        for (TablesNames t: d.getTablesNamesList()) {
                            if (t.getAttributeList()==null)
                                t.setAttributeList(new ArrayList<>());
                            if (t.getIndexList()==null)
                                t.setIndexList(new ArrayList<>());
                        }
                    }
                }
            }
            return db;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void jaxbObjectToXML(Databases dbs) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Databases.class);
            System.out.println(dbs);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //print xml string to console
            StringWriter sw = new StringWriter();

            File file = new File("date.xml");

            //write xml to string writer
            jaxbMarshaller.marshal(dbs, file);

            //verify xml content
            String xmlContent = sw.toString();
            System.out.println(xmlContent);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
