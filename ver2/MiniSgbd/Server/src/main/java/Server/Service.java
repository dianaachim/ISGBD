package Server;

import Domain.*;
import Repository.*;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private Database currentDatabase;
    private Repository repository;
    private MongoDbConfig mongoDbConfig;

    public Service(Repository repository) {
        this.repository = repository;
        this.currentDatabase = this.repository.getLastDatabase();
        this.mongoDbConfig = new MongoDbConfig(currentDatabase);
    }

    public String checkCommand(String command) throws Exception {
        String[] cmd = command.split(" ", 3);
        switch (cmd[0].toLowerCase()) {
            case "status":
                return this.getCurrentDatabase().toString();
            case "create":
                switch (cmd[1].toLowerCase()) {
                    case "db":
                        return this.createDB(cmd[2]);
                    case "table":
                        return this.checkCreateTableCommand(cmd[2]);
                    case "index":
                        return this.checkCreateIndexCommand(cmd[2]);
                    default:
                        return "Wrong command!";
                }
            case "drop":
                if (cmd[1].toLowerCase().equals("db")) {
                    return this.dropDB(cmd[2]);
                } else if (cmd[1].toLowerCase().equals("table")) {

                    return this.dropTable(cmd[2], currentDatabase.getDatabaseName());
                } else {
                    return "Wrong command";
                }
//                break;
            case "use":
                if (cmd[1].toLowerCase().equals("db")) {
                    if (this.repository.find(cmd[2]) != null) {
                        this.setCurrentDatabase(cmd[2]);
                        return "Database changed to " + cmd[2];
                    } else {
                        return "Database not found!";
                    }
                } else {
                    return "Wrong command!";
                }
//                break;
            case "test":
                return cmd[2];
            case "show":
                if (cmd[1].toLowerCase().equals("dbs")) {
                    return this.repository.getDbsList().toString();
                } else if (cmd[1].toLowerCase().equals("tables")) {
                    return  this.showTables();
//                    return this.repository.getTables(this.currentDatabase.toString()).toString();
                }
//                } else {
//                    return "Wrong command!";
//                }
                    break;
            case "insert":
                if (cmd[1].toLowerCase().equals("table")) {
                    return this.checkInsertCommand(cmd[2]);
                }
                break;
            case "delete":
                if (cmd[1].toLowerCase().equals("table")) {
                    return this.checkDeleteCommand(cmd[2]);
                }
                break;
        }
        return "Wrong command";
    }

    private String checkDeleteCommand(String cmd) throws  Exception{
        String[] table = cmd.split("\\.");
        String tableName = table[0];
        String key = "";
        String value = "";
        for (String tb: table) {
            if (tb.split("\\(")[0].equals("value")) {
                key = tb.split("[()]")[1];
            }
        }
        DTO dto=new DTO(key,value);
        return this.delete(tableName, dto);
    }

    private String checkInsertCommand(String cmd) throws Exception {
        String[] table = cmd.split("\\.");
        String tableName = table[0];
        //the table in which we insert the values
        Table tbl = this.repository.findTable(this.currentDatabase.getDatabaseName(), tableName);
        ArrayList<Attribute> attrList = (ArrayList<Attribute>) tbl.getAttributeList();
        String key = "";
        String value = "";
        for (String tb: table) {
            if (tb.split("\\(")[0].equals("value")) {
                String[] attrs = tb.split("[()]")[1].split(",");
                int i = 0;
                while (i < tbl.getAttributeList().size()) {
                    if (attrList.get(i).getPk()==true) {
                        if (key.equals("")) {
                            key = attrs[i];
                        } else {
                            key += "#" + attrs[i];
                        }
                    } else {
                        if ( value.equals("")) {
                            value = attrs[i];
                        } else {
                            value = "#" + attrs[i];
                        }
                    }
                    i+=1;
                }

            }
        }
        DTO dto = new DTO(key, value);
        return this.insert(tableName, dto);
    }

    private String checkCreateIndexCommand(String cmd) throws Exception {
        String[] index = cmd.split("\\.");
        String indexName=index[0];

        String tableName= "";
        String columnName="";
        boolean unique=false;
        ArrayList<String> indexColumns = new ArrayList<>();
        for (String ind : index) {
            switch (ind.split("\\(")[0]) {
                case "on":
                    tableName = ind.split("[()]")[1];
                    break;
                case "column":
                    String[] columns = ind.split("[()]")[1].split("[ ,]");
                    for (String column : columns) {
                        if (!column.equals("")) {
                            indexColumns.add(column);
                        }
                    }
//                    columnName = ind.split("[()]")[1];
                    break;
                case "unique":
                    unique = true;
                    break;
            }

        }
        return this.createIndex(indexName, tableName, indexColumns, this.currentDatabase.getDatabaseName(), unique);
    }

    public String checkCreateTableCommand(String cmd) throws Exception {
        List<String> primaryKeys = new ArrayList<>();
        List<String> uniqueKeys = new ArrayList<>();
        String[] table = cmd.split("\\.");
        ArrayList<Attribute> attributeArrayList = new ArrayList<>();
        String tableName = table[0];
        for (String tb : table) {
            switch (tb.split("\\(")[0]) {
                case "attrs":
                    String[] attrs = tb.split("[()]")[1].split(",");
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
                    break;
                case "uk":
                    String[] uks = tb.split("[()]")[1].split("[ ,]");
                    for (String uk: uks) {
                        for (Attribute a : attributeArrayList) {
                            if (a.getName().equals(uk)) {
                                a.setUk(true);
                                a.setNotNull(true);
                                uniqueKeys.add(uk);
                            }
                        }
                    }
                    break;
                case "pk":
                    String[] pks = tb.split("[()]")[1].split("[ ,]");
//                    String pk = tb.split("[()]")[1];
                    for (String pk: pks) {
                        for (Attribute a : attributeArrayList) {
                            if (a.getName().equals(pk)) {
                                a.setPk(true);
                                a.setNotNull(true);
                                primaryKeys.add(pk);
//                        this.createIndex(a.getName(), tableName, a.getName(), this.currentDatabase.getDatabaseName(), true);
                            }
                        }
                    }
                    break;
                case "ref":
                    String[] ref = tb.split("[()]")[1].split(",");
                    String tableRef = ref[1].split(" ")[1];
                    if (this.repository.findTable(this.currentDatabase.getDatabaseName(), tableRef) == null) {
                        return "Attribute reference not found";
                    } else {
                        for (Attribute a : attributeArrayList) {
                            if (a.getName().equals(ref[0])) {
                                a.setFk(true);
                                a.setReference(tableRef);
                            }
                        }
                    }
                    break;
            }
        }
        PrimaryKeys pks = new PrimaryKeys();
        pks.setPrimaryKeys(primaryKeys);
        UniqueKeys uks = new UniqueKeys();
        uks.setUniqueKeys(uniqueKeys);
        return this.createTable(tableName, this.currentDatabase.getDatabaseName(), attributeArrayList, pks, uks);

    }

    public String createDB(String name) throws Exception {
        Database db = new Database();
        db.setDatabaseName(name);
        return this.repository.save(db);


    }

    public String dropDB(String name) throws Exception {

        return this.repository.delete(name);

    }

    public String showTables() throws Exception {
        List<Table> tableList = this.repository.getTables(this.currentDatabase.getDatabaseName());
        StringBuilder tableString = new StringBuilder();
        for (Table table: tableList) {
            tableString.append(table.toString());
        }
        return tableString.toString();
    }

    public String createTable(String tableName, String databaseName, List<Attribute> attributes, PrimaryKeys pks, UniqueKeys uks) throws Exception {
        Table table = new Table();
        List<Index> idxList = new ArrayList<>();
        table.setTableName(tableName);
        table.setAttributeList(attributes);
        table.setPks(pks);
        table.setUks(uks);
        Index idx = new Index(table.getTableName() + ".ind", tableName, pks.getPrimaryKeys(), databaseName, true);

        idxList.add(idx);
        table.setIndexList(idxList);
        return this.repository.saveTable(table, databaseName);
    }

    public String dropTable(String tableName, String databaseName) throws Exception {
        return this.repository.deleteTable(databaseName, tableName);
    }

    public String createIndex(String name, String tableName, List<String> columnName, String dbName, Boolean unique) throws Exception {
        Index index = new Index(name, tableName, columnName, dbName, unique);

        return this.repository.saveIndex(dbName, tableName, index);
    }

    public Database getCurrentDatabase() {
        return currentDatabase;
    }

    public void setCurrentDatabase(String dbname) throws Exception {
        this.currentDatabase = this.repository.find(dbname);
        this.mongoDbConfig.setDatabase(this.currentDatabase);
    }

    public String insert(String tbName,DTO dto) {

        this.mongoDbConfig.insert(tbName, dto);
        return "Value inserted";
    }

    public String delete(String tbName, DTO dto){
        this.mongoDbConfig.delete(tbName,dto);
        return "Value deleted";
    }
}
