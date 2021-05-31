package Database;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseActions<T> {
    void insert(T data) throws SQLException;
    void update(T data) throws SQLException;
    List<T> getList() throws SQLException;
    T getById(int id) throws SQLException;
    void delete(int id) throws SQLException;

}
