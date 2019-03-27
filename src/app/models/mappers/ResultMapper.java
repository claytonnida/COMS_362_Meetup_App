package app.models.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ResultMapper<T> {


    //Should take the result row and return a populated object
    public abstract T createObject(ResultSet rs) throws SQLException;

    //Creates a list of the desired object
    public List<T> createObjectList(ResultSet rs) throws SQLException;

    //Should create an UPDATE statement that allows a WHERE clause to be appended
    public abstract String toUpdateQueryQuery(T object);

    //Should create an INSERT statement that allows a WHERE clause to be appended
    public abstract String toInsertQueryQuery(T object);

}
