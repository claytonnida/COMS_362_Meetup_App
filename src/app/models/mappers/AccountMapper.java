package app.models.mappers;

import app.models.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountMapper implements ResultMapper<Account> {
    @Override
    public Account createObject(ResultSet rs) throws SQLException {
        ReflectMapper<Account> pmapper = new ReflectMapper<>(Account.class);
        return pmapper.toObject(rs);
    }

    @Override
    public List<Account> createObjectList(ResultSet rs) throws SQLException {
        ReflectMapper<Account> pmapper = new ReflectMapper<>(Account.class);
        return pmapper.toObjectList(rs);
    }

    @Override
    /**
     * Creates an Update query from the provided object
     *
     */
    public String toUpdateQueryQuery(Account object) {
        ReflectMapper<Account> pmapper = new ReflectMapper<>(Account.class);
        return null;//TODO
    }

    @Override
    /**
     * Creates an Insert query from the provided object
     *
     */
    public String toInsertQueryQuery(Account object) {
        ReflectMapper<Account> pmapper = new ReflectMapper<>(Account.class);
        return pmapper.toInsertStatement(object);
    }
}
