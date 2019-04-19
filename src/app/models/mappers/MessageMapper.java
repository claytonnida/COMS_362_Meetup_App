package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.Group;
import app.models.Message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageMapper implements ResultMapper<Message> {
    @Override
    public Message createObject(ResultSet rs) throws SQLException {
        ReflectMapper<Message> pmapper = new ReflectMapper<>(Message.class);
        return pmapper.toObject(rs);
    }

    @Override
    public List<Message> createObjectList(ResultSet rs) throws SQLException {
        ReflectMapper<Message> pmapper = new ReflectMapper<>(Message.class);
        return pmapper.toObjectList(rs);
    }

    @Override
    public List<Message> createObjectList(String query) throws SQLException {
        ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
        return createObjectList(rs);
    }

    @Override
    public String toUpdateQueryQuery(Message object) {
        ReflectMapper<Message> pmapper = new ReflectMapper<>(Message.class);
        return pmapper.toUpdateStatement(object);
    }

    @Override
    public String toInsertQueryQuery(Message object) {
        ReflectMapper<Message> pmapper = new ReflectMapper<>(Message.class);
        return pmapper.toInsertStatement(object);
    }
}
