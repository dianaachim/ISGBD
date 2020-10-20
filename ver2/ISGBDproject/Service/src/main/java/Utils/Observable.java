package Utils;

public interface Observable {
    void NotifyObservers();
    void AddObserver(Observer observer);
    void RemoveObserver(Observer observer);
}
