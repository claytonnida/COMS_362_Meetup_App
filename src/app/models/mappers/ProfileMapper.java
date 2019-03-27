package app.models.mappers;

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
        return null;//TODO
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
}