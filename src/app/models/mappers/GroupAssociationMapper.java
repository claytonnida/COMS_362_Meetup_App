package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.GroupAssociation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupAssociationMapper implements ResultMapper<GroupAssociation> {

	@Override
	public GroupAssociation createObject(ResultSet rs) throws SQLException {
		ReflectMapper<GroupAssociation> pmapper = new ReflectMapper<>(GroupAssociation.class);
		return pmapper.toObject(rs);
	}

	@Override
	public List<GroupAssociation> createObjectList(ResultSet rs) throws SQLException {
		ReflectMapper<GroupAssociation> pmapper = new ReflectMapper<>(GroupAssociation.class);
		return pmapper.toObjectList(rs);
	}

	@Override
	public List<GroupAssociation> createObjectList(String query) throws SQLException {
		ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
		return createObjectList(rs);
	}

	@Override
	/**
	 * Creates an Update query from the provided object
	 *
	 */
	public String toUpdateQueryQuery(GroupAssociation object) {
		ReflectMapper<GroupAssociation> pmapper = new ReflectMapper<>(GroupAssociation.class);
		return pmapper.toUpdateStatement(object);
	}

	@Override
	/**
	 * Creates an Insert query from the provided object
	 *
	 */
	public String toInsertQueryQuery(GroupAssociation object) {
		//INSERT INTO meetup.%s (%s) VALUES (%s)
		return String.format("Insert into meetup.groupAssociation (profileid, groupid) values (%d,%d)",
				object.getProfileid(),object.getGroupid());
	}
}
