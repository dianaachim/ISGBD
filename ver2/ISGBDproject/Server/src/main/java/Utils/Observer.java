package Utils;

import Domain.Databases;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Observer extends Remote {
    void Notify(Databases databases) throws RemoteException;
}
