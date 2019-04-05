package app.Controllers;

import app.MySQL.MySQLHelper;
import app.interfaces.GroupAssociationControllerInterface;
import app.models.Group;
import app.models.Profile;

public class GroupAssociationController implements GroupAssociationControllerInterface {


    /**
    * Removes the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
    *
    * @param profileId The ID of the {@link Profile} to disassociate.
    *
    * @param groupId The ID of the {@link Group} to disassociate.
    */
    @Override
    public void leaveGroup(int profileId, int groupId) {
        MySQLHelper.executeUpdate("DELETE FROM meetup.groupassociation " +
        "where profileid = " + profileId +
        " and groupid = " + gro        );

        System.out.println("Successfully removed GroupAssociation between profile ID: " + profileId + " and group ID: " + groupId + "!");
    }

    /**
    *Joins the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
    *
    * @param profileId
    *     The ID of the {@link Profile} to associate with a Group.
    * @param groupId
    *     The ID of the {@link Group} to associate.
    */
    @Override
    public void joinGroup(int profileId, int groupId) {
        //TODO: Complete
        MySQLHelper.executeUpdate("INSERT INTO meetup.groupassociation " +
        "where profileid = " + profileId +
        " and groupid = " + gro        );

        System.out.println("Successfully added GroupAssociation between profile ID: " + profileId + " and group ID: " + groupId + "!");
    }
}
