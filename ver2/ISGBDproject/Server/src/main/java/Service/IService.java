package Service;

import Domain.Databases;
import Domain.DatabasesNames;
import Utils.Observable;

import java.util.List;

public interface IService extends Observable {
    void addDatabaseNames(DatabasesNames database) throws Exception;
    List<DatabasesNames> getNames() throws Exception;
    void removeDatabasesNames(String databasesNames) throws Exception;

    Databases read();
    void write(Databases db);
}
