package Server;

import Domain.Attribute;
import Domain.Database;
import Domain.Databases;
import Domain.Table;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private ArrayList<Database> dbsList;
    private Databases dbs;

    public Service(Databases dbs) {
        this.dbsList = new ArrayList<>();
        this.dbs = dbs;
    }

    public void createDB(String name) throws JAXBException {

        Database db = new Database();
        db.setDatabaseName(name);
        db.setTablesList(new ArrayList<>());
        dbsList.add(db);

        dbs.setDbList(dbsList);

        JAXBContext context = JAXBContext.newInstance(Databases.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(dbs, new File("./data.xml"));
    }

    public void dropDB(String name) throws JAXBException {

        for (Database db: dbsList) {
            if (db.getDatabaseName().equals(name)) {
                dbsList.remove(db);
                dbs.setDbList(dbsList);
                JAXBContext context = JAXBContext.newInstance(Databases.class);
                Marshaller mar= context.createMarshaller();
                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                mar.marshal(dbs, new File("./data.xml"));
            }
        }
    }

    public void createTable(String tableName, String databaseName, List<Attribute> attributes) throws JAXBException {
        List<Table> tableList;
        for (Database db: dbsList) {
            if (db.getDatabaseName().equals(databaseName)) {
                tableList = db.getTablesList();
                Table table = new Table(tableName, databaseName);
                table.setAttributeList(attributes);
                tableList.add(table);
                db.setTablesList(tableList);
                JAXBContext context = JAXBContext.newInstance(Databases.class);
                Marshaller mar= context.createMarshaller();
                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                mar.marshal(dbs, new File("./data.xml"));
            }
        }
    }

    public void dropTable(String tableName, String databaseName) throws JAXBException {
        List<Table> tableList;
        for (Database db: dbsList) {
            if (db.getDatabaseName().equals(databaseName)) {
                tableList = db.getTablesList();
                for (Table table: tableList) {
                    if (table.getTableName().equals(tableName)){
                        tableList.remove(table);
                    }
                }
                db.setTablesList(tableList);
                JAXBContext context = JAXBContext.newInstance(Databases.class);
                Marshaller mar= context.createMarshaller();
                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                mar.marshal(dbs, new File("./data.xml"));
            }
        }
    }
}
