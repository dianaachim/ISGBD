package Service;

import Domain.*;
import Utils.Observable;
import Utils.Observer;

import java.util.List;
import java.util.Map;

public interface IService extends Observable
{

    void addDatabaseNames(DatabasesNames database) throws Exception;
     List<DatabasesNames> getNames() throws Exception;
     void removeDatabasesNames(String databasesNames) throws Exception;
    void addTableNames(TablesNames table, String dbName) throws Exception;
    void addAttribute(Attribute attribute, String dbName, String tableName) throws Exception;
    List<Attribute> getAttributes(String dbName, String tableName) throws Exception;
    List<TablesNames> getTablesN(String dbName) throws Exception;
    void removeTablesNames(String dbNames, String tablesNames) throws Exception;
    void addIndex(String dbName, String tbName, Index index) throws Exception;
     void write(Databases db);
     Databases read();
     void insert(String dbNames, String tablesNames, DTO dto);
     Integer getSize(String dbNames, String tablesNames) throws Exception;
     List<DTO> getDate(String dbNames, String tablesNames);
     void createCollection(String dbName, String tbName);
     void delete(String dbName, String tbName, String key);
     void update(String dbName, String tbName, DTO dto);
     Attribute findAttribute(String dbName, String tbName, String key)throws Exception;
     boolean findPk(String dbName, String tbName, String name);
     Index findIndex(String dbName, String tbName, String name) throws Exception;
     boolean findUkI(String dbName, String tbName, String index, String value);
     void addI(String dbName, String tbName, String index, DTO dto);
    void deleteI(String dbName, String tbName, String index, String value);
    List<String> getUk(String dbName, String tbName, int i);
    int findNr(String dbName, String tbName, String attr) throws Exception;
    List<String> getValueByKey(String dbName, String tbName, String k);
    TablesNames findTable(String entity, String tableName) throws Exception;
    String getPk(String dbName, String tbName);
    String getValueByKeyI(String dbName, String tbName, String index, String k);
    void updateI(String dbName, String tbName, String index, DTO dto);
    String getKeyByValueI(String dbName, String tbName, String index, String v);
    String findAttributeRefI(String entity, String tableName) throws Exception;
    TablesNames findAttributeRef(String entity, String tableName) throws Exception;
    List<String> getKeys(String db, String tb);
    List<DTO> getDtoIndex(String dbName, String tbName, String index);
    String getValueByKey1(String dbName, String tbName, String k);
    @Override
    void NotifyObservers();

    @Override
    void AddObserver(Observer observer);

    @Override
    void RemoveObserver(Observer observer);
}
