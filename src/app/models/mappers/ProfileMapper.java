package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.Group;
import app.models.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfileMapper implements ResultMapper<Profile> {


    @Override
    public Profile createObject(ResultSet rs) throws SQLException {
        ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
        return pmapper.toObject(rs);
    }

    @Override
    public List<Profile> createObjectList(ResultSet rs) throws SQLException {
        ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
        return pmapper.toObjectList(rs);
    }

    @Override
    /**
     * Creates an Update query from the provided object
     *
     */
    public String toUpdateQueryQuery(Profile object) {
        ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
        return pmapper.toUpdateStatement(object);
    }

    @Override
    /**
     * Creates an Insert query from the provided object
     *
     */
    public String toInsertQueryQuery(Profile object) {
        ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
        return pmapper.toInsertStatement(object);
    }

    @Override
    public List<Profile> createObjectList(String query) throws SQLException {
        ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
        return createObjectList(rs);
    }
}