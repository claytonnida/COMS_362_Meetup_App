package app.models.mappers;

import app.models.GroupChat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupChatMapper implements ResultMapper<GroupChat> {

	@Override
	public GroupChat createObject(ResultSet rs) throws SQLException {
		ReflectMapper<GroupChat> pmapper = new ReflectMapper<>(GroupChat.class);
		return pmapper.toObject(rs);
	}

	@Override
	public List<GroupChat> createObjectList(ResultSet rs) throws SQLException {
		ReflectMapper<GroupChat> pmapper = new ReflectMapper<>(GroupChat.class);
		return pmapper.toObjectList(rs);
	}

	@Override
	/**
	 * Creates an Update query from the provided object
	 *
	 */
	public String toUpdateQueryQuery(GroupChat object) {
		ReflectMapper<GroupChat> pmapper = new ReflectMapper<>(GroupChat.class);
		return null;//TODO
	}

	@Override
	/**
	 * Creates an Insert query from the provided object
	 *
	 */
	public String toInsertQueryQuery(GroupChat object) {
		ReflectMapper<GroupChat> pmapper = new ReflectMapper<>(GroupChat.class);
		return pmapper.toInsertStatement(object);
	}
}
