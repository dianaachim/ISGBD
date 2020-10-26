package Server;

import Domain.*;
import Repository.Repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Service {
//    private ArrayList<Database> dbsList;
//    private Databases dbs;
    private Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
//        this.dbsList = new ArrayList<>();
//        this.dbs = dbs;
    }

    public String createDB(String name) throws Exception {
        Database db = new Database();
        db.setDatabaseName(name);
        return this.repository.save(db);

//        Database db = new Database();
//        db.setDatabaseName(name);
//        db.setTablesList(new ArrayList<>());
//        dbsList.add(db);
//
//        dbs.setDbList(dbsList);
//
//        JAXBContext context = JAXBContext.newInstance(Databases.class);
//        Marshaller mar= context.createMarshaller();
//        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        mar.marshal(dbs, new File("./catalog.xml"));
//        return "Database successfully created!";
    }

    public String dropDB(String name) throws Exception {

        return this.repository.delete(name);

//        for (Database db: dbsList) {
//            if (db.getDatabaseName().equals(name)) {
//                dbsList.remove(db);
//                dbs.setDbList(dbsList);
//                JAXBContext context = JAXBContext.newInstance(Databases.class);
//                Marshaller mar= context.createMarshaller();
//                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//                mar.marshal(dbs, new File("./catalog.xml"));
//                return "Database successfully removed!";
//            }
//        }
//        return "Error dropping database!";
    }

    public String createTable(String tableName, String databaseName, List<Attribute> attributes) throws Exception {
        Table table = new Table();
        table.setTableName(tableName);
        table.setAttributeList(attributes);
        return this.repository.saveTable(table, databaseName);
//        List<Table> tableList;
//        for (Database db: dbsList) {
//            if (db.getDatabaseName().equals(databaseName)) {
//                tableList = db.getTablesList();
//                Table table = new Table(tableName, databaseName);
//                table.setAttributeList(attributes);
//                tableList.add(table);
//                db.setTablesList(tableList);
//                JAXBContext context = JAXBContext.newInstance(Databases.class);
//                Marshaller mar= context.createMarshaller();
//                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//                mar.marshal(dbs, new File("./catalog.xml"));
//                return "Table successfully created!";
//            }
//        }
//        return "Error creating table!";
    }

    public String dropTable(String tableName, String databaseName) throws Exception {
        return this.repository.deleteTable(databaseName, tableName);
//        List<Table> tableList;
//        for (Database db: dbsList) {
//            if (db.getDatabaseName().equals(databaseName)) {
//                tableList = db.getTablesList();
//                tableList.removeIf(table -> table.getTableName().equals(tableName));
//                db.setTablesList(tableList);
//                JAXBContext context = JAXBContext.newInstance(Databases.class);
//                Marshaller mar= context.createMarshaller();
//                mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//                mar.marshal(dbs, new File("./catalog.xml"));
//                return "Table successfully removed!";
//            }
//        }
//        return "Error dropping table!";
    }

    public String createIndex(String name, String tableName, String columnName, String dbName, Boolean unique) throws Exception {
        Index index = new Index(name, tableName, columnName, dbName, unique);

        return this.repository.saveIndex(dbName, tableName, index);
//        String message = "Error creating Index!";
//
//        List<Index> indexList;
//
//        for (Database db : dbsList) {
//            if (db.getDatabaseName().equals(dbName)) {
//                for (Table table : db.getTablesList()) {
//                    if (table.getTableName().equals(tableName)) {
//                        Index index = new Index(name, tableName, columnName, dbName, unique);
//                        indexList = table.getIndexList();
//                        indexList.add(index);
//                        table.setIndexList(indexList);
//
//                        JAXBContext context = JAXBContext.newInstance(Databases.class);
//                        Marshaller mar= context.createMarshaller();
//                        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//                        mar.marshal(dbs, new File("./catalog.xml"));
//
//                        message = "Index successfully created!";
//                        return message;
//                    }
//                }
//            }
//        }
//
//        return message;
    }
}
