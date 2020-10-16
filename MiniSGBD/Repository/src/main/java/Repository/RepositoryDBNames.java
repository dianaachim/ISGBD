package Repository;

import Domain.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class RepositoryDBNames implements IRepositoryDBNames {
    Databases dbs;
    Repository repository;
    public RepositoryDBNames(Repository repository){

        this.repository=repository;
        this.dbs=repository.jaxbXMLToObject();

    }

    @Override
    public void save(DatabasesNames entity) throws Exception {
        if(this.find(entity.getDatabaseName())==null) {
            this.dbs.getDb().add(entity);
            this.repository.jaxbObjectToXML(this.dbs);

        }
    }

    @Override
    public void delete(String s) throws Exception {
        DatabasesNames db=this.find(s);
         this.dbs.getDb().remove(db);
        this.repository.jaxbObjectToXML(this.dbs);
    }

    public String getPk(String dbName, String tbName)
    {
        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (dbName.equals(db.getDatabaseName()))
            {
                for(TablesNames tb: db.getTablesNamesList())
                {
                    if(tb.getTableName().equals(tbName))
                        for(Attribute a:tb.getAttributeList())
                            if(a.getPk())
                                return a.getName();
                }
            }
        }
        return null;
    }
    public void deleteTable(String dbName, String tbName) throws Exception
    {

        DatabasesNames db=this.find(dbName);
        TablesNames tb=this.findTable(dbName,tbName);
        while(this.findAttributeRef(dbName,tbName)!=null) {
            db.getTablesNamesList().remove(this.findAttributeRef(dbName, tbName));
        }
          db.getTablesNamesList().remove(tb);
        this.repository.jaxbObjectToXML(this.dbs);
    }
    public List<TablesNames> getTables(String dbName) throws Exception {
        DatabasesNames db=this.find(dbName);
        return db.getTablesNamesList();
    }

    @Override
    public DatabasesNames find(String entity) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                return db;
            }
        }
        return null;
    }
    public TablesNames findTable(String entity,String tableName) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                if(!db.getTablesNamesList().isEmpty())
                for(TablesNames tb: db.getTablesNamesList())
                {
                    if(tableName.equals(tb.getTableName()))
                        return tb;
                }
            }
        }
        return null;
    }
    public Attribute findAttribute(String entity,String tableName,String attribute) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(TablesNames tb: db.getTablesNamesList())
                {
                    if(tb.getTableName().equals(tableName))
                   for(Attribute a:tb.getAttributeList())
                       if(a.getName().equals(attribute))
                           return a;
                }
            }
        }
        return null;
    }
    public int findAttributeI(String entity,String tableName,String attribute) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(TablesNames tb: db.getTablesNamesList())
                { int p=0;
                    if(tb.getTableName().equals(tableName))
                        for(int i=0;i<tb.getAttributeList().size();i++)
                        { if(tb.getAttributeList().get(i).getPk())p++;
                            if(tb.getAttributeList().get(i).getName().equals(attribute))
                                return i-p;}
                }
            }
        }
        return -1;
    }
    public Index findIndex(String entity,String tableName,String index) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(TablesNames tb: db.getTablesNamesList())
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
    public TablesNames findAttributeRef(String entity,String tableName) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(TablesNames tb: db.getTablesNamesList())
                {
                    for(Attribute a:tb.getAttributeList())
                        if(a.getReference().equals(tableName))
                            return tb;
                }
            }
        }
        return null;
    }
    public String findAttributeRefI(String entity,String tableName) throws Exception {


        for ( DatabasesNames db: this.dbs.getDb())
        {
            if (entity.equals(db.getDatabaseName()))
            {
                for(TablesNames tb: db.getTablesNamesList())
                {
                    for(Attribute a:tb.getAttributeList())
                        if(a.getReference().equals(tableName))
                            return a.getName();
                }
            }
        }
        return null;
    }
public List<DatabasesNames> getDBNAmes()
{
    return this.dbs.getDb();
}

public void addTables(TablesNames tbn,String dbName) throws Exception {
    DatabasesNames dbF=this.find(dbName);

    List<TablesNames> tbL=dbF.getTablesNamesList();

    System.out.println(dbF.getTablesNamesList().isEmpty());
    if(this.findTable(dbName,tbn.getTableName())==null)
        tbL.add(tbn);
   // if(this.findTable(dbName,tbn.getTableName())==null)



    this.repository.jaxbObjectToXML(this.dbs);
}
public void addIndex(String dbName, String tbn, Index index) throws Exception
{

    TablesNames tbL=this.findTable(dbName,tbn);
    if(findIndex(dbName,tbn,index.getName())==null){

   tbL.getIndexList().add(index);
    this.repository.jaxbObjectToXML(this.dbs);}


}
public void addAttribute(String dbName, String tableName, Attribute attribute) throws Exception {
        TablesNames tf=this.findTable(dbName,tableName);
        List<Attribute> aL=tf.getAttributeList();
        if(this.findAttribute(dbName,tableName,attribute.getName())==null)
            if((this.findTable(dbName,attribute.getReference())!=null && !attribute.getReference().equals("") && attribute.getFk()) || (attribute.getReference().equals("") && !attribute.getFk()))
            aL.add(attribute);
    this.repository.jaxbObjectToXML(this.dbs);

}
public List<Attribute> getAttribute(String entity,String tableName) throws Exception
{

    for ( DatabasesNames db: this.dbs.getDb())
    {
        if (entity.equals(db.getDatabaseName()))
        {
            for(TablesNames tb: db.getTablesNamesList())
            {
                if(tableName.equals(tb.getTableName()))
                    return tb.getAttributeList();
            }
        }
    }
    return null;
}
    public Integer getAttrSize(String dbName, String tblName) throws Exception
    {
        List<Attribute> attrs = this.getAttribute(dbName, tblName);
        return attrs.size();
    }
}
