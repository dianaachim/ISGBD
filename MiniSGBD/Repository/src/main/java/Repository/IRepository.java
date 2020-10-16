package Repository;


public interface IRepository<ID, T> {
    void save(T entity) throws Exception;
    void delete(ID id) throws Exception;
    T find(String entity) throws Exception;
}