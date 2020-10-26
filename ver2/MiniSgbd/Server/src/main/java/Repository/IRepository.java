package Repository;

public interface IRepository<ID, T> {
    String save(T entity) throws Exception;
    String delete(ID id) throws Exception;
    T find (String entity) throws Exception;
}
