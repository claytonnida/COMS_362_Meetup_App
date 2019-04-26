package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.GroupInvitation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupInvitationMapper implements ResultMapper<GroupInvitation> {

	@Override
	public GroupInvitation createObject(ResultSet rs) throws SQLException {
		ReflectMapper<GroupInvitation> pmapper = new ReflectMapper<>(GroupInvitation.class);
		return pmapper.toObject(rs);
	}

	@Override
	public List<GroupInvitation> createObjectList(ResultSet rs) throws SQLException {
		ReflectMapper<GroupInvitation> pmapper = new ReflectMapper<>(GroupInvitation.class);
		return pmapper.toObjectList(rs);
	}

	@Override
	public List<GroupInvitation> createObjectList(String query) throws SQLException {
		ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
		return createObjectList(rs);
	}

	@Override
	public String toUpdateQueryQuery(GroupInvitation object) {
		ReflectMapper<GroupInvitation> pmapper = new ReflectMapper<>(GroupInvitation.class);
		return pmapper.toUpdateStatement(object);
	}

	@Override
	public String toInsertQueryQuery(GroupInvitation object) {
		return String.format("Insert into meetup.groupInvitation (profileid, groupid) values (%d,%d)",
				object.getProfileid(),object.getGroupid());
	}
}
