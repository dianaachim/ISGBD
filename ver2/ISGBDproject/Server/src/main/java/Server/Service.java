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

    public Service(RepositoryDBNames repositoryDBNames,Repository repository) {
        this.notifyingMap = new ConcurrentHashMap<>();
        this.observers =new ArrayList<>();

        this.repositoryDBNames=repositoryDBNames;
        this.repository=repository;

    }

    @Override
    public void addDatabaseNames(DatabasesNames database) throws Exception {
        this.repositoryDBNames.save(database);
    }

    @Override
    public List<DatabasesNames> getNames() throws Exception {
        return this.repositoryDBNames.getDBNames();
    }

    @Override
    public void removeDatabasesNames(String databasesNames) throws Exception {
        this.repositoryDBNames.delete(databasesNames);
    }

    @Override
    public void NotifyObservers() {
        try{
            Databases d = new Databases();
            d.setDb(this.repositoryDBNames.getDBNames());
            for (Observer o: observers)
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
}
