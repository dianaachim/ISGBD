package Server;

import Domain.*;
import Repository.*;
import org.bson.Document;
import org.w3c.dom.Attr;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String[] attrs = new String[100];
        //the table in which we insert the values
        Table tbl = this.repository.findTable(this.currentDatabase.getDatabaseName(), tableName);
        ArrayList<Attribute> attrList = (ArrayList<Attribute>) tbl.getAttributeList();
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for (String tb: table) {
            if (tb.split("\\(")[0].equals("value")) {
                attrs = tb.split("[()]")[1].split(",");
                int i = 0;
                while (i < tbl.getAttributeList().size()) {
                    if (attrList.get(i).getPk()) {
                        if (key.toString().equals("")) {
                            key = new StringBuilder(attrs[i]);
                        } else {
                            key.append("#").append(attrs[i]);
                        }
                    } else {
                        if ( value.toString().equals("")) {
                            value = new StringBuilder(attrs[i]);
                        } else {
                            value.append("#").append(attrs[i]);
                        }
                    }
                    i+=1;
                }

            }
        }
        DTO dto = new DTO(key.toString(), value.toString());
        if (this.mongoDbConfig.getValueByKey(tableName, key.toString())==null) {
            String message  = this.insertUniqueIndexes(attrList, attrs, key.toString(), tableName);
            if (message.equals("Duplicate key!")) {
                return "Duplicate unique key";
            }
            message = this.insertForeignKeyIndexes(attrList, attrs, key.toString(), tableName);
            if (message.equals("Fk ref not found!")) {
                return "Fk not found in reference table!";
            }
            message = this.insertUserIndexes(tbl, attrs, key.toString());
            if (message.equals("Duplicate key!")) {
                return "Duplicate unique key";
            }
        }
//        String message = this.mongoDbConfig.insertIndex("UK_" + tableName, dtoUK);

        return this.insert(tableName, dto);
    }

    private String insertUniqueIndexes(ArrayList<Attribute> attrList, String[] attrs, String pk, String tableName) throws UnsupportedEncodingException {
        int i = 0;
        boolean ok = true;
        Map<String, DTO> dtoMap = new HashMap<>();
        while (i < attrList.size()) {
            if (attrList.get(i).getUk() && !attrList.get(i).getPk()) {
                String uk = attrs[i];
                DTO dto = new DTO(uk, pk);
                dtoMap.put("UK_"+tableName+"_"+attrList.get(i).getName(), dto);
                if (this.mongoDbConfig.getValueByKey("UK_"+tableName+"_"+attrList.get(i).getName(), uk)!=null) {
                    ok = false;
                    return "Duplicate key!";
                }
            }
            i+=1;
        }
        if (ok) {
            for (String file : dtoMap.keySet()) {
                this.mongoDbConfig.insertIndex(file, dtoMap.get(file));
            }
        }
        return "Index files added!";
    }

    private String insertForeignKeyIndexes(ArrayList<Attribute> attrList, String[] attrs, String pk, String tableName) throws UnsupportedEncodingException {
        int i = 0;
        Map<String, DTO> dtoMap = new HashMap<>();
        while (i < attrList.size()) {
            if (attrList.get(i).getFk()) {
                String fk = attrs[i];
                String collectionName = "FK_" + tableName + "_" + attrList.get(i).getReference();
                //check if the foreign key exists in the ref table
                if (this.mongoDbConfig.getValueByKey(attrList.get(i).getReference(), fk)==null) {
                    return "Fk ref not found!";
                }
                if (this.mongoDbConfig.getValueByKey(collectionName, fk)!=null) {
                    Document doc = this.mongoDbConfig.getValueByKey(collectionName, fk);
                    //updatam documentul
                    String value = (String) doc.get("value");
                    value += "#" + pk;
                    DTO dto = new DTO(fk, value);
                    this.mongoDbConfig.update(collectionName, dto);
                } else {
                    DTO dto = new DTO(fk, pk);
                    dtoMap.put(collectionName, dto);
                }
            }
            i+=1;
        }
        for (String file : dtoMap.keySet()) {
            this.mongoDbConfig.insertIndex(file, dtoMap.get(file));
        }
        return "Index files added!";
    }

    private String insertUserIndexes(Table tbl, String[] attrs, String pk) throws UnsupportedEncodingException {
        ArrayList<Attribute> attrList = (ArrayList<Attribute>) tbl.getAttributeList();
        Map<String, DTO> dtoMap = new HashMap<>();
        for (Index index : tbl.getIndexList()) {
            //luam fiecare index in parte si verificam ce coloane contine
            int i = 0;
            String key = "";
            String valueIndex = "";
            while (i<attrList.size()) {
                if (index.getColumns().contains(attrList.get(i).getName())) {
                    if (key.equals("")) {
                        key+=attrs[i];
                    } else {
                        key += "#" + attrs[i];
                    }
                }
                i+=1;
            }
            if (index.getUnique() && this.mongoDbConfig.getValueByKey(index.getName(), key)!=null) {
                return "Duplicate key!";
            } else if (!index.getUnique() && this.mongoDbConfig.getValueByKey(index.getName(), key)!=null) {
                Document doc = this.mongoDbConfig.getValueByKey(index.getName(), key);
                //updatam documentul
                String value = (String) doc.get("value");
                value += "#" + pk;
                DTO dto = new DTO(key, value);
                this.mongoDbConfig.update(index.getName(), dto);
            } else {
                DTO dto = new DTO(key, pk);
                dtoMap.put(index.getName(), dto);
            }

        }
        for (String file : dtoMap.keySet()) {
            this.mongoDbConfig.insertIndex(file, dtoMap.get(file));
        }
        return "Index files added!";
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

    public boolean validateAttributeType(String type) {
        List<String> types = new ArrayList<>();
        types.add("int");
        types.add("varchar");
        types.add("datetime");
        types.add("float");
        return types.contains(type);
    }

    public String checkCreateTableCommand(String cmd) throws Exception {

        List<String> primaryKeys = new ArrayList<>();
        List<String> uniqueKeys = new ArrayList<>();
        List<ForeignKey> foreignKeys = new ArrayList<>();
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
                            String[] type = att[2].split("\\[");
                            if (this.validateAttributeType(type[0])) {
                                a.setType(type[0]);
                                if (type.length > 1) {
                                    a.setLength(Integer.parseInt(type[1].split("\\]")[0]));
                                }
                            } else {
                                return "Attribute types can only be varchar, int, float, datetime";
                            }
//                            a.setType(att[2]);
                        } else {
                            a.setName(att[0]);
                            String[] type = att[1].split("\\[");
                            if (this.validateAttributeType(type[0].split("\\]")[0])) {
                                a.setType(type[0]);
                                if (type.length > 1) {
                                    a.setLength(Integer.parseInt(type[1]));
                                }
                            } else {
                                return "Attribute types can only be varchar, int, float, datetime";
                            }
//                            a.setType(att[1]);
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
                                ForeignKey fk = new ForeignKey(a.getName(), ref[0], tableRef, "FK_" + tableName + "_" + tableRef);
                                foreignKeys.add(fk);
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
        ForeignKeys fks = new ForeignKeys();
        fks.setForeignKeyList(foreignKeys);
        return this.createTable(tableName, this.currentDatabase.getDatabaseName(), attributeArrayList, pks, uks, fks);

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

    public String createTable(String tableName, String databaseName, List<Attribute> attributes, PrimaryKeys pks, UniqueKeys uks, ForeignKeys fks) throws Exception {
        Table table = new Table();
        List<Index> idxList = new ArrayList<>();
        table.setTableName(tableName);
        table.setAttributeList(attributes);
        table.setPks(pks);
        table.setUks(uks);
        table.setFks(fks);
//        Index idx = new Index(table.getTableName() + ".ind", tableName, pks.getPrimaryKeys(), databaseName, true);

//        idxList.add(idx);
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

        return this.mongoDbConfig.insert(tbName, dto);
//        return "Value inserted";
    }

    public String delete(String tbName, DTO dto) throws Exception {
        if (this.safeDelete(this.repository.findTable(this.currentDatabase.getDatabaseName(), tbName), dto)) {
            this.mongoDbConfig.delete(tbName,dto);
            return "Value deleted";
        } else {
            return "Attribute referenced in another table";
        }

    }

    public boolean safeDelete(Table table, DTO dto) throws Exception {
        //se verifica referintele
        ArrayList<Table> tables = (ArrayList<Table>) this.repository.getTables(this.currentDatabase.getDatabaseName());
        for (Table dbTable: tables) {
            for (ForeignKey fk: dbTable.getFks().getForeignKeyList()) {
                if (fk.getRefTable().equals(table.getTableName())) {
                    if (this.mongoDbConfig.getValueByKey(fk.getFkfile(), dto.getKey())!= null) {
                        //daca exista referinta la dto in alt tabel
                        return false;
                    }
                }
            }
        }
        //se sterg fisierele de uk
        for (String uk: table.getUks().getUniqueKeys()) {
            String tableName = "UK_" + table.getTableName() + "_" + uk;
            Document document = this.mongoDbConfig.getDocumentByValue(tableName, dto.getKey());
            if (document != null) {
                this.mongoDbConfig.deleteByDocument(tableName, document);
            }
        }
        return true;
    }
}
