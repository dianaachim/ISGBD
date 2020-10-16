package Server;

import Domain.*;
import Repository.*;
import Service.IService;
import Utils.Observer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private Map<String,Observer> notifyingMap;
    private List<Observer> observers;

    private RepositoryDBNames repositoryDBNames;
    private Repository repository;
    private MongoDBConfig mongoDBConfig;


    public Service(RepositoryDBNames repositoryDBNames,Repository repository,MongoDBConfig mongoDBConfig) {
        this.notifyingMap = new ConcurrentHashMap<>();
        this.observers =new ArrayList<>();

        this.repositoryDBNames=repositoryDBNames;
        this.repository=repository;
        this.mongoDBConfig=mongoDBConfig;

    }


    @Override
    public List<DatabasesNames> getNames() throws Exception
    {
        return this.repositoryDBNames.getDBNAmes();
    }

    @Override
    public void removeDatabasesNames(String databasesNames) throws Exception {
         this.repositoryDBNames.delete(databasesNames);
    }

    @Override
    public void addTableNames(TablesNames table,String dbName) throws Exception {
        this.repositoryDBNames.addTables(table,dbName);

    }

    @Override
    public void addAttribute(Attribute attribute, String dbName, String tableName) throws Exception {
        if(attribute.getPk())this.repositoryDBNames.addIndex(dbName,tableName,new Index(attribute.getName(),"clustered",dbName+"_"+tableName));
        if(attribute.getFk())this.repositoryDBNames.addIndex(dbName,tableName,new Index(attribute.getName(),"unclustered",dbName+"_"+tableName));
        if(attribute.getUk())this.repositoryDBNames.addIndex(dbName,tableName,new Index(attribute.getName(),"unclustered",dbName+"_"+tableName));
        this.repositoryDBNames.addAttribute(dbName,tableName,attribute);
    }

    @Override
    public List<Attribute> getAttributes(String dbName, String tableName) throws Exception {
        return this.repositoryDBNames.getAttribute(dbName,tableName);
    }

    @Override
    public List<TablesNames> getTablesN(String dbName) throws Exception {
       return this.repositoryDBNames.getTables(dbName);
    }

    @Override
    public void removeTablesNames(String dbName,String tablesNames) throws Exception {
        this.repositoryDBNames.deleteTable(dbName,tablesNames);
    }

    @Override
    public void addIndex(String dbName, String tbName, Index index) throws Exception {
        this.repositoryDBNames.addIndex(tbName,dbName,index);
        if(this.findAttribute(dbName,tbName,index.getName())!=null)
            if(!this.findAttribute(dbName,tbName,index.getName()).getPk() )
            this.mongoDBConfig.createIndexT(dbName,tbName,index.getName());
    }


    @Override
    public void addDatabaseNames(DatabasesNames database) throws Exception {
        this.repositoryDBNames.save(database);
    }

    @Override
    public void NotifyObservers() {
        try {
            Databases d = new Databases();
            d.setDb(this.repositoryDBNames.getDBNAmes());
            for (Observer o : observers)
                o.Notify(d);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void AddObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void RemoveObserver(Observer observer) {
        observers.remove(observer);
    }

    public void write(Databases db)
    {
        this.repository.jaxbObjectToXML(db);
    }

    @Override
    public Databases read() {
        return this.repository.jaxbXMLToObject();
    }

    @Override
    public void insert(String dbName,String tbName,DTO dto) {

        this.mongoDBConfig.insert(dbName,tbName,dto);

    }

    @Override
    public Integer getSize(String dbNames, String tablesNames) throws Exception {
        return this.repositoryDBNames.getAttrSize(dbNames,tablesNames);
    }

    @Override
    public List<DTO> getDate(String dbNames, String tablesNames) {
     return mongoDBConfig.getDto(dbNames,tablesNames);
    }

    @Override
    public void createCollection(String dbName, String tbName) {

        this.mongoDBConfig.createTableMDB(dbName,tbName);
    }

    @Override
    public void delete(String dbName, String tbName, String key) {
        this.mongoDBConfig.delete(dbName,tbName,key);
    }

    @Override
    public void update(String dbName, String tbName, DTO dto) {
        this.mongoDBConfig.update(dbName,tbName,dto);
    }

    @Override
    public Attribute findAttribute(String dbName, String tbName, String key) throws Exception {
        return this.repositoryDBNames.findAttribute(dbName,tbName,key);
    }

    @Override
    public boolean findPk(String dbName, String tbName, String name) {
        return this.mongoDBConfig.findPk(dbName,tbName,name);
    }

    @Override
    public Index findIndex(String dbName, String tbName, String name) throws Exception {
        return this.repositoryDBNames.findIndex(dbName,tbName,name);
    }

    @Override
    public boolean findUkI(String dbName, String tbName, String index, String value) {
        return this.mongoDBConfig.findUkI(dbName,tbName,index,value);
    }

    @Override
    public void addI(String dbName, String tbName, String index, DTO dto) {
        this.mongoDBConfig.inserI(dbName,tbName,index,dto);
    }

    @Override
    public void deleteI(String dbName, String tbName, String index, String key) {
        this.mongoDBConfig.deleteI(dbName,tbName,index,key);
    }

    @Override
    public List<String> getUk(String dbName, String tbName,int i) {
        List<String> values = mongoDBConfig.getValue(dbName, tbName);
        List<String> split_values= new ArrayList<String>();
        int count=0;
        for (String value : values)
        {
            System.out.println("value "+value);
            count=0;
            String[] arrOfStr = value.split("#");
            for (String s : arrOfStr) {
                if (count == i)
                {
                    split_values.add(s);
                    System.out.println("din get uk    "+s);
            }
            count++;
            }

        }
        return split_values;

    }


    @Override
    public int findNr(String dbName, String tbName, String attr) throws Exception {
        return this.repositoryDBNames.findAttributeI(dbName,tbName,attr);
    }

    @Override
    public List<String> getValueByKey(String dbName, String tbName,  String k) {
        String value = mongoDBConfig.getValueByKey(dbName,tbName,k);
        List<String> split_values= new ArrayList<String>();


            String[] arrOfStr = value.split("#");
            for (String s : arrOfStr) {

                    split_values.add(s);

            }


        return split_values;

    }

    @Override
    public TablesNames findTable(String entity, String tableName) throws Exception {
        return repositoryDBNames.findTable(entity,tableName);
    }

    @Override
    public String getPk(String dbName, String tbName) {
        return repositoryDBNames.getPk(dbName,tbName);
    }

    @Override
    public String getValueByKeyI(String dbName, String tbName, String index, String k) {
        return mongoDBConfig.getValueByKeyI(dbName,tbName,index,k);
    }

    @Override
    public void updateI(String dbName, String tbName, String index, DTO dto) {
        this.mongoDBConfig.updateI(dbName,tbName,index,dto);
    }

    @Override
    public String getKeyByValueI(String dbName, String tbName, String index, String v) {
        return mongoDBConfig.getKeyByValueI(dbName,tbName,index,v);
    }

    @Override
    public String findAttributeRefI(String entity, String tableName) throws Exception {
        return this.repositoryDBNames.findAttributeRefI(entity,tableName);
    }

    @Override
    public TablesNames findAttributeRef(String entity, String tableName) throws Exception {
        return this.repositoryDBNames.findAttributeRef(entity,tableName);
    }

    @Override
    public List<String> getKeys(String db, String tb) {
        return mongoDBConfig.getKeys(db,tb);
    }

    @Override
    public List<DTO> getDtoIndex(String dbName, String tbName, String index) {
        return mongoDBConfig.getDtoIndex(dbName,tbName,index);
    }

    @Override
    public String getValueByKey1(String dbName, String tbName, String k) {
        return mongoDBConfig.getValueByKey(dbName,tbName,k);
    }
}
