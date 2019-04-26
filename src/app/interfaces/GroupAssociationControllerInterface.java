package app.interfaces;

import app.models.Group;
import app.models.GroupAssociation;
import app.models.Profile;

import java.sql.SQLException;

public interface GroupAssociationControllerInterface {

	/**
	 * Checks the database to see if a {@link GroupAssociation} with the given IDs exists.
	 *
	 * @param profileId
	 * 		The ID of the {@link Profile} in the {@link GroupAssociation}.
	 * @param groupId
	 * 		The ID of the {@link Group} in the {@link GroupAssociation}.
	 *
	 * @return {@code true}, if the {@link GroupAssociation} does exist on the database. {@code false} otherwise.
	 *
	 * @throws SQLException
	 * 		Occurs if there is an error when querying the database.
	 */
	boolean doesGroupAssociationExist(int profileId, int groupId) throws SQLException;

    /**
    * Removes the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
    *
    * @param profileId The ID of the {@link Profile} to disassociate.
    *
    * @param groupId The ID of the {@link Group} to disassociate.
    */
    void leaveGroup(int profileId, int groupId);

    /**
    *Joins the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
    *
    * @param profileId
    *     The ID of the {@link Profile} to associate with a Group.
    * @param groupId
    *     The ID of the {@link Group} to associate.
    */
    void joinGroup(int profileId, int groupId);
}
