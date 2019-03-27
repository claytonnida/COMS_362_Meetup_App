package app.models.mappers;

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
	/**
	 * Creates an Update query from the provided object
	 *
	 */
	public String toUpdateQueryQuery(GroupAssociation object) {
		ReflectMapper<GroupAssociation> pmapper = new ReflectMapper<>(GroupAssociation.class);
		return null;//TODO
	}

	@Override
	/**
	 * Creates an Insert query from the provided object
	 *
	 */
	public String toInsertQueryQuery(GroupAssociation object) {
		ReflectMapper<GroupAssociation> pmapper = new ReflectMapper<>(GroupAssociation.class);
		return pmapper.toInsertStatement(object);
	}
}
