package app.models.mappers;

import app.MySQL.MySQLHelper;
import app.models.Interests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InterestsMapper implements ResultMapper<Interests> {

	@Override
	public Interests createObject(ResultSet rs) throws SQLException {
		ReflectMapper<Interests> pmapper = new ReflectMapper<>(Interests.class);
		return pmapper.toObject(rs);
	}

	@Override
	public List<Interests> createObjectList(ResultSet rs) throws SQLException {
		ReflectMapper<Interests> pmapper = new ReflectMapper<>(Interests.class);
		return pmapper.toObjectList(rs);
	}

	@Override
	public List<Interests> createObjectList(String query) throws SQLException {
		ResultSet rs = MySQLHelper.createStatement().executeQuery(query);
		return createObjectList(rs);
	}

	@Override
	/**
	 * Creates an Update query from the provided object
	 *
	 */
	public String toUpdateQueryQuery(Interests object) {
		ReflectMapper<Interests> pmapper = new ReflectMapper<>(Interests.class);
		return pmapper.toUpdateStatement(object);
	}

	@Override
	/**
	 * Creates an Insert query from the provided object
	 *
	 */
	public String toInsertQueryQuery(Interests object) {
		//INSERT INTO meetup.%s (%s) VALUES (%s)
		return String.format("Insert into meetup.interests (interestName, interestOf) values (%d,%d)",
				object.getInterestName(),object.getInterestOf());
	}
}
