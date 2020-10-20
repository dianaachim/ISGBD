package Repository;

import Domain.Databases;
import Domain.DatabasesNames;

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
        if (this.find(entity.getDatabaseName())==null) {
            this.dbs.getDb().add(entity);
            this.repository.jaxbObjectToXML(this.dbs);
        }
    }

    @Override
    public void delete(String s) throws Exception {
        DatabasesNames db = this.find(s);
        this.dbs.getDb().remove(db);
        this.repository.jaxbObjectToXML(this.dbs);
    }

    @Override
    public DatabasesNames find(String entity) throws Exception {
        for (DatabasesNames db: this.dbs.getDb()) {
            if (entity.equals(db.getDatabaseName()))
                return db;
        }

        return null;
    }

    public List<DatabasesNames> getDBNames()
    {
        return this.dbs.getDb();
    }
}
