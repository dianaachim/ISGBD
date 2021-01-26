package Server;

import Domain.*;
import Repository.*;
import com.sun.tools.javac.util.Pair;
import org.bson.Document;
import org.w3c.dom.Attr;

import java.io.UnsupportedEncodingException;
import java.util.*;

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
            case "select":
                return this.checkSelectCommand(cmd[1], cmd[2]);
        }
        return "Wrong command";
    }

    private String checkSelectCommand(String attributeDistinct, String selectBody) throws Exception {
        //for lab 4
        String[] body = selectBody.split("\\.");
        String tableName = "";
        ArrayList<String> whereConditions = new ArrayList<>();
        ArrayList<String> attributeList = new ArrayList<>();

        for (String element: body)
        {
            if (element.split("\\(")[0].equals("from")) {
                tableName = element.split("[()]")[1];
            } else if (element.split("\\(")[0].equals("where")) {
                String[] whereString  = element.split("[()]")[1].split("[AND]");
                for (String whereCondition: whereString) {
                    if (!whereCondition.equals("")) {
                        whereConditions.add(whereCondition.split("\\[")[1].split("\\]")[0]);
                    }
                }
            } else {
                String[] valuesString  = element.split("[()]")[1].split(",");
                for (String attr: valuesString) {
                    if (!attr.equals("")) {
                        attributeList.add(attr);
                    }
                }
            }
        }
        return this.executeSelect(attributeDistinct, tableName, attributeList, whereConditions);
    }

    private String executeSelect(String attributeDistinct, String tableName, ArrayList<String> attributeList, ArrayList<String> whereConditions) throws Exception {
        HashMap<String, List<Pair<String, String>>> conditions = this.whereConditionsToHashMap(whereConditions); //key=coloana, value=lista de pair(operator,valoare)
        String index = this.findIndexName(conditions, tableName);
        List<DTO> dtoList = this.mongoDbConfig.getDto(tableName); //lista cu toate dto-urile din tabela
        List<DTO> dtoListIndex; //lista cu dto-urile din index
        List<String> keys = new ArrayList<>(); //lista cu keys care respecta conditiile din where
        List<String> keyAttributesList = new ArrayList<>(conditions.keySet()); //lista cu atribute din where
        String finalTable;
        String item = "Id";
        String item2= "id";

        if (this.repository.findTable(this.currentDatabase.toString(), tableName)==null) {
            return "Table not found";
        }

        if (!index.equals("")) {
            dtoListIndex = this.mongoDbConfig.getDto(index);
            for (DTO dto: dtoListIndex) {
                String[] keyAttributes = dto.getKey().split("#");
                int i = 0;
                for (String key: keyAttributes) {
                    List<Pair<String, String>> conditionsList = conditions.get(keyAttributesList.get(i)); //lista de conditii pt key
                    for (Pair<String, String> cond: conditionsList) {
                        //verificam fiecare pereche de conditie (pereche = operator + valoare)
                        if (this.checkCondition(key, cond)) {
                            String[] values = this.mongoDbConfig.getValueByKey2(index, key).split("#");
                            keys.addAll(Arrays.asList(values));
                        }
                    }
                    i+=1;
                }
            }
            //finalTable = this.keysToPrettyTable(keys, tableName, attributeDistinct, attributeList);
        } else {
            //daca nu avem index pe atributele din where
            //TODO: IMPLEMENTARE PENTRU CAND NU ESTE INDEX
            //verificam daca conditia e pe id
            if (keyAttributesList.size() == 1 && conditions.get(keyAttributesList.get(0)).size()== 1 && conditions.get(keyAttributesList.get(0)).get(0).fst.equals("=") && keyAttributesList.get(0).contains(item) || keyAttributesList.get(0).contains(item2)){
                String value = this.mongoDbConfig.getValueByKey2(tableName,conditions.get(keyAttributesList.get(0)).get(0).snd);
                if (value.equals("No find")) {
                    return "No element found";
                }
                keys.add(conditions.get(keyAttributesList.get(0)).get(0).snd);
            }
            else {
                //daca nu este nici un index, mergem pe toate elementele din tabelul principal si verificam conditia
                for (DTO dto: dtoList) {
                    //luam fiecare element si facem split pe valoare
                    String[] value = dto.getValue().split("#");
                    String[] allValues = new String[value.length+1];
                    allValues[0] = dto.getKey();
                    System.arraycopy(value, 0, allValues, 1, value.length);
                    //mergem simultan pe lista de valori si pe lista de atribute din tabel
                    boolean checks = true; // presupunem ca toate conditiile sunt indeplinite
                    List<Attribute> tableAttributes = this.repository.findTable(this.currentDatabase.getDatabaseName(), tableName).getAttributeList();
                    for (int i = 0; i < allValues.length; i++) {
                        //verificam daca valoarea de pe pozitia i indeplineste conditiile din conditions pentru elementul cu valoara i+1 din tableAttributes
                        List<Pair<String, String>> elementConditions = conditions.get(tableAttributes.get(i).getName());
                        if (elementConditions != null) {
                            for (Pair<String, String> pair : elementConditions) {
                                if (!this.checkCondition(allValues[i], pair)) {
                                    checks = false;
                                }
                            }
                        }
                    }
                    if (checks) {
                        //inseamna ca toate conditiile au fost indeplinite
                        keys.add(dto.getKey());
                    }
                }

            }
        }

        finalTable = this.keysToPrettyTable(keys, tableName, attributeDistinct, attributeList);

        return finalTable;
    }

    private String keysToPrettyTable(List<String> keys, String tableName, String attributeDistinct, ArrayList<String> attributeList) throws Exception {
        List<String> finalAttributesStrings = new ArrayList<>();
        StringBuilder finalTable = new StringBuilder();
        String tableHead = "|";

        for (String key: keys) {
            //luam toate key-urile care respecta conditia si afisam atributele din select
            String tableField = "|";
            String value = this.mongoDbConfig.getValueByKey2(tableName, key);
            String[] columns = value.split("#");

            if (attributeList.size()==1 && attributeList.get(0).equals("*")) {
                //atunci ne traba toate atributele
                tableField = tableField + key + "|";
                List<Attribute> tableAttributes = this.repository.findTable(this.currentDatabase.getDatabaseName(), tableName).getAttributeList();
                for (Attribute attribute: tableAttributes) {
                    tableHead = tableHead + attribute.getName() + "|";
                }
                for (String col: columns) {
                    tableField = tableField + col + "|";
                }

            } else {
                //atunci cand traba numa o parte din atribute
                int i=0;
                List<Attribute> tableAttributes = this.repository.findTable(this.currentDatabase.getDatabaseName(), tableName).getAttributeList();
                for (Attribute attribute: tableAttributes) {
                    if (attributeList.contains(attribute.getName())) {
                        if (attribute.getName().matches("(.*)id") || attribute.getName().matches("(.*)Id")) {
                            tableField = tableField + key + "|";
                        } else {
                            tableField = tableField + columns[i] + "|";
                        }
                        tableHead = tableHead + attribute.getName() + "|";
                        i+=1;
                    }
                }

            }
            if (attributeDistinct.equals("all")) {
                finalAttributesStrings.add(tableField);
            } else if (attributeDistinct.equals("distinct")) {
                if (!finalAttributesStrings.contains(tableField)) {
                    finalAttributesStrings.add(tableField);
                }
            } else {
                return "Wrong command!";
            }

        }
        finalTable = new StringBuilder(tableHead);

        for (String row: finalAttributesStrings) {
            finalTable.append("\n").append(row);
        }

        return finalTable.toString();
    }

    private List<Pair<String, String>> keyAttributesToList(List<String> keyAttributes) {
        List<Pair<String, String>> attributes = new ArrayList<>();
        for (String attr: keyAttributes) {
            attributes.add(Pair.of(attr,""));
        }
        return attributes;
    }

    private boolean checkCondition(String key, Pair<String, String> cond) {
        switch (cond.fst) {
            case "<": {
                int value = Integer.parseInt(cond.snd);
                int keyInt = Integer.parseInt(key);
                if (keyInt < value) {
                    return true;
                }
                break;
            }
            case ">": {
                int value = Integer.parseInt(cond.snd);
                int keyInt = Integer.parseInt(key);
                if (keyInt > value) {
                    return true;
                }
                break;
            }
            case ">=": {
                int value = Integer.parseInt(cond.snd);
                int keyInt = Integer.parseInt(key);
                if (keyInt >= value) {
                    return true;
                }
                break;
            }
            case "<=": {
                int value = Integer.parseInt(cond.snd);
                int keyInt = Integer.parseInt(key);
                if (keyInt <= value) {
                    return true;
                }
                break;
            }
            case "<>":
                if (!key.equals(cond.snd)) {
                    return true;
                }
                break;
            case "=":
                if (key.equals(cond.snd)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private String findIndexName(HashMap<String, List<Pair<String, String>>> conditions, String tableName) throws Exception {
        String index = "";
        String uniqueIndex = "";
        StringBuilder indexName = new StringBuilder("INDEX_" + tableName);
        StringBuilder indexNameCond = new StringBuilder("INDEX_" + tableName);

        for (String col: conditions.keySet()) {
            if (this.repository.findAttribute(this.currentDatabase.getDatabaseName(), tableName, col).getUk() && !col.matches("(.*)"+"Id") && !col.matches("(.*)"+"id")) {
                uniqueIndex = "UK_" + tableName + "_" + col;
            }
            indexName.append("_").append(col);
        }

        if (!indexName.equals(indexNameCond)) {
            for (Index idx: this.repository.findTable(this.currentDatabase.getDatabaseName(), tableName).getIndexList()) {
                if (idx.getName().matches(indexName + "(.*)")) {
                    index = idx.getName();
                }
            }
        }

        if (!index.equals("")) {
            return index;
        } else if (!uniqueIndex.equals("")) {
            return uniqueIndex;
        } else {
            return "";
        }
    }

    private HashMap<String, List<Pair<String, String>>> whereConditionsToHashMap (ArrayList<String> whereConditions) {
        HashMap<String, List<Pair<String, String>>> conditions = new HashMap<>();
        for (String cond: whereConditions) {
            ArrayList<String> conditionArray = this.splitCondition(cond);
            if (conditions.containsKey(conditionArray.get(0))) {
                conditions.get(conditionArray.get(0)).add(Pair.of(conditionArray.get(1), conditionArray.get(2)));
            } else {
                List<Pair<String, String>> c = new ArrayList<>();
                c.add(Pair.of(conditionArray.get(1), conditionArray.get(2)));
                conditions.put(conditionArray.get(0), c);
            }
        }
        return conditions;
    }

    private ArrayList<String> splitCondition(String cond) {
        //splits a condition like id=3 to a list like [id,=,3]
        ArrayList<String> condition = new ArrayList<>();
        if (cond.matches("(.*)<=(.*)")) {
            String[] splitCondition = cond.split("[<=]");
            condition.add(splitCondition[0]);
            condition.add("<=");
            condition.add(splitCondition[2]);
            return condition;
        } else if (cond.matches("(.*)>=(.*)")) {
            String[] splitCondition = cond.split("[>=]");
            condition.add(splitCondition[0]);
            condition.add(">=");
            condition.add(splitCondition[2]);
            return condition;
        } else if (cond.matches("(.*)=(.*)")) {
            String[] splitCondition = cond.split("=");
            condition.add(splitCondition[0]);
            condition.add("=");
            condition.add(splitCondition[1]);
            return condition;
        } else if (cond.matches("(.*)<>(.*)")) {
            String[] splitCondition = cond.split("[<>]");
            condition.add(splitCondition[0]);
            condition.add("<>");
            condition.add(splitCondition[2]);
            return condition;
        } else if (cond.matches("(.*)<(.*)")) {
            String[] splitCondition = cond.split("<");
            condition.add(splitCondition[0]);
            condition.add("<");
            condition.add(splitCondition[1]);
            return condition;
        } else if (cond.matches("(.*)>(.*)")) {
            String[] splitCondition = cond.split(">");
            condition.add(splitCondition[0]);
            condition.add(">");
            condition.add(splitCondition[1]);
            return condition;
        }

        return null;
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
        //se sterge din fisierele de index
        for (Index idx: table.getIndexList()) {
            String tableName = idx.getName();
            Document document = this.mongoDbConfig.getDocumentByValue(tableName, dto.getKey());
            if (document != null) {
                this.mongoDbConfig.deleteByDocument(tableName, document);
            }
        }
        return true;
    }
}
