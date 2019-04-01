package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupMapper implements ResultMapper<Group> {

	@Override
	public Group createObject(ResultSet rs) throws SQLException {
		ReflectMapper<Group> pmapper = new ReflectMapper<>(Group.class);
		return pmapper.toObject(rs);
	}

	@Override
	public List<Group> createObjectList(ResultSet rs) throws SQLException {
		ReflectMapper<Group> pmapper = new ReflectMapper<>(Group.class);
		return pmapper.toObjectList(rs);
	}

	@Override
	public List<Group> createObjectList(String query) throws SQLException {
		ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
		return createObjectList(rs);
	}

	@Override
	/**
	 * Creates an Update query from the provided object
	 *
	 */
	public String toUpdateQueryQuery(Group object) {
		ReflectMapper<Group> pmapper = new ReflectMapper<>(Group.class);
		return pmapper.toUpdateStatement(object);
	}

	@Override
	/**
	 * Creates an Insert query from the provided object
	 *
	 */
	public String toInsertQueryQuery(Group object) {
		ReflectMapper<Group> pmapper = new ReflectMapper<>(Group.class);
		return pmapper.toInsertStatement(object);
	}
}
