package app.models.mappers;

import app.models.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper extends ResultMapper<Account>{
    @Override
    public Account createObject(ResultSet rs) throws SQLException {
        Account acc = new Account(rs.getString("username"),rs.getString("password"));
        acc.setId(rs.getInt("id"));
        acc.setProfileid(rs.getInt("profile"));
        return acc;
    }

    @Override
    public String toUpdateQueryQuery(Account object) {
        return null;
    }

    @Override
    public String toInsertQueryQuery(Account object) {
        return null;
    }
}
