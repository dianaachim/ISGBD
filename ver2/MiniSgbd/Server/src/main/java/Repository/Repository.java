package Repository;

import Domain.*;

import javax.xml.bind.JAXBException;
import java.util.List;

public class Repository implements IRepository<String, Database> {
    private Databases dbs;
    private ConvertXML converter;

    public Repository(ConvertXML converter) throws JAXBException {
        this.converter = converter;
        this.dbs = this.converter.jaxbXMLToObject();
    }

    @Override
    public String save(Database entity) throws Exception {
        if (this.find(entity.getDatabaseName())==null) {
            //daca baza de date nu exista deja
            this.dbs.getDbList().add(entity);
            this.converter.jaxbObjectToXML(this.dbs);
            return "Database successfully added!";
        } else {
            return "Database already exists!";
        }
    }

    @Override
    public String delete(String s) throws Exception {
        if (this.find(s)!=null) {
            Database db = this.find(s);
            this.dbs.getDbList().remove(db);
            this.converter.jaxbObjectToXML(this.dbs);
            return "Database successfully removed";
        } else {
            return "Database not found!";
        }
    }

    @Override
    public Database find(String entity) throws Exception {
        for ( Database db: this.dbs.getDbList())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                return db;
            }
        }
        return null;
    }

    public List<Database> getDbsList() {
        return this.dbs.getDbList();
    }

    public List<Table> getTables(String dbName) throws Exception {
        Database db=this.find(dbName);
        return db.getTablesList();
    }

    public Table findTable(String entity,String tableName) throws Exception {


        for ( Database db: this.dbs.getDbList())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                if(!db.getTablesList().isEmpty())
                    for(Table tb: db.getTablesList())
                    {
                        if(tableName.equals(tb.getTableName()))
                            return tb;
                    }
            }
        }
        return null;
    }

    public Table findAttributeRef(String entity,String tableName) throws Exception {
        //verifica daca exista referinte la tabelul tableName in alte tabele

        for ( Database db: this.dbs.getDbList())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(Table tb: db.getTablesList())
                {
                    for(Attribute a:tb.getAttributeList())
                        if(a.getReference().equals(tableName))
                            return tb;
                }
            }
        }
        return null;
    }

    public Index findIndex(String entity,String tableName,String index) throws Exception {


        for ( Database db: this.dbs.getDbList())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(Table tb: db.getTablesList())
                {
                    if(tb.getTableName().equals(tableName))
                        for(Index a:tb.getIndexList())
                            if(a.getName().equals(index))
                                return a;
                }
            }
        }
        return null;
    }

    public String saveTable(Table table,String dbName) throws Exception {
        if (this.find(dbName)!=null) {
            Database db = this.find(dbName);
            List<Table> tableList = db.getTablesList();
            if (this.findTable(dbName, table.getTableName())==null) {
                tableList.add(table);
                this.find(dbName).setTablesList(tableList);
                this.converter.jaxbObjectToXML(this.dbs);
                return "Table successfully added!";
            } else {
                return "Table already exists!";
            }
        } else {
            return "Database not found!";
        }
    }

    public String deleteTable(String dbName, String tbName) throws Exception
    {
        if (this.find(dbName)!= null) {
            Database db=this.find(dbName);
            if (this.findTable(dbName, tbName)!=null) {
                Table table = this.findTable(dbName, tbName);

                //stergem tabelele cu referinta la tabelul tbName
                while(this.findAttributeRef(dbName,tbName)!=null) {
//                    db.getTablesList().remove(this.findAttributeRef(dbName, tbName));
                    this.deleteTable(dbName, this.findAttributeRef(dbName, tbName).getTableName());
                }
                //stergem tabelul
                db.getTablesList().remove(table);
                this.converter.jaxbObjectToXML(this.dbs);
                return "Table successfully removed!";
            } else {
                return "Table not found!";
            }
        } else {
            return "Database not found!";
        }
    }

    public String saveIndex(String dbName, String tbName, Index index) throws Exception {
        if (this.find(dbName) != null) {
            if (this.findTable(dbName, tbName)!=null) {
                Table table = this.findTable(dbName, tbName);
                if(findIndex(dbName,tbName,index.getName())==null) {
                    table.getIndexList().add(index);
                    this.converter.jaxbObjectToXML(this.dbs);
                    return "Index successfully added!";
                } else {
                    return "Index already exists!";
                }
            }else {
                return "Table not found!";
            }
        } else {
            return "Database not found!";
        }
    }

}
