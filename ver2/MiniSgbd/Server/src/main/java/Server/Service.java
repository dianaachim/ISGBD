package Server;

import Domain.*;
import Repository.Repository;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.w3c.dom.Attr;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Service {
    private Database currentDatabase;
    private Repository repository;

    public Service(Repository repository) {
        this.repository = repository;
        this.currentDatabase = this.repository.getLastDatabase();
    }

    public String checkCommand(String command) throws Exception {
        String[] cmd = command.split(" ", 3);
        if (cmd[0].toLowerCase().equals("status")) {
            return this.getCurrentDatabase().toString();
        } else if (cmd[0].toLowerCase().equals("create")) {
            if (cmd[1].toLowerCase().equals("db")) {
                return this.createDB(cmd[2]);
            } else if (cmd[1].toLowerCase().equals("table")) {
                return this.checkCreateTableCommand(cmd[2]);
            } else if (cmd[1].toLowerCase().equals("index")) {
                return this.checkCreateIndexCommand(cmd[2]);
            } else return "Wrong command!";
        } else if (cmd[0].toLowerCase().equals("drop")) {
            if (cmd[1].toLowerCase().equals("db")) {
                return this.dropDB(cmd[2]);
            } else if (cmd[1].toLowerCase().equals("table")) {

                return this.dropTable(cmd[2], currentDatabase.getDatabaseName());
            }
        } else if (cmd[0].toLowerCase().equals("use")) {
            if (cmd[1].toLowerCase().equals("db")) {
                if (this.repository.find(cmd[2])!=null) {
                    this.setCurrentDatabase(cmd[2]);
                    return "Database changed to " + cmd[2];
                } else {
                    return "Database not found!";
                }
            }
        } else if(cmd[0].toLowerCase().equals("test")) {
            return cmd[2];
        } else if(cmd[0].toLowerCase().equals("show")) {
            if (cmd[1].toLowerCase().equals("dbs")) {
                return this.repository.getDbsList().toString();
            } else if (cmd[1].toLowerCase().equals("tables")) {
                return this.repository.getTables(this.currentDatabase.toString()).toString();
            }
        }
        return "Wrong command";
    }

    private String checkCreateIndexCommand(String cmd) throws Exception {
        String[] index = cmd.split("\\.");
        String indexName=index[0];
        String tableName= "";
        String columnName="";
        Boolean unique=false;
        for (String ind : index) {
            if (ind.split("\\(")[0].equals("on")) {
                tableName =  ind.split("[\\()]")[1];
            }
            else if (ind.split("\\(")[0].equals("column")) {
                 columnName =  ind.split("[\\()]")[1];
            }
            else if (ind.split("\\(")[0].equals("unique")) {
                unique = true;
            }
            else
                return "Wrong command";
        }
        return this.createIndex(indexName, tableName, columnName, this.currentDatabase.getDatabaseName(), unique);

    }

    public String checkCreateTableCommand(String cmd) throws Exception {
        String[] table = cmd.split("\\.");
        ArrayList<Attribute> attributeArrayList = new ArrayList<>();
        String tableName = table[0];
        for (String tb : table) {
            if (tb.split("\\(")[0].equals("attrs")) {
                String[] attrs =  tb.split("[\\()]")[1].split(",");
                for (String attribute : attrs) {
                    Attribute a = new Attribute();
                    String[] att = attribute.split(" ");
                    if (att[0].equals("")) {
                        a.setName(att[1]);
                        a.setType(att[2]);
                    } else {
                        a.setName(att[0]);
                        a.setType(att[1]);
                    }
                    attributeArrayList.add(a);
                }
            } else if (tb.split("\\(")[0].equals("uk")) {
                String uk =  tb.split("[\\()]")[1];
                for (Attribute a: attributeArrayList) {
                    if (a.getName().equals(uk)) {
                        a.setUk(true);
                        a.setNotNull(true);
                    }
                }
            } else if (tb.split("\\(")[0].equals("pk")) {
                String pk =  tb.split("[\\()]")[1];
                for (Attribute a: attributeArrayList) {
                    if (a.getName().equals(pk)) {
                        a.setPk(true);
                        a.setNotNull(true);
//                        this.createIndex(a.getName(), tableName, a.getName(), this.currentDatabase.getDatabaseName(), true);
                    }
                }
            } else if (tb.split("\\(")[0].equals("ref")) {
                String[] ref =  tb.split("[\\()]")[1].split(",");
                String tableRef = ref[1].split(" ")[1];
                if (this.repository.findTable(this.currentDatabase.getDatabaseName(), tableRef) == null) {
                    return "Attribute reference not found";
                } else {
                    for (Attribute a: attributeArrayList) {
                        if (a.getName().equals(ref[0])) {
                            a.setFk(true);
                            a.setReference(tableRef);
                        }
                    }
                }
            }
        }
        return this.createTable(tableName, this.currentDatabase.getDatabaseName(), attributeArrayList);

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

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(String dbname) throws Exception {
        this.currentDatabase = this.repository.find(dbname);
    }
}
