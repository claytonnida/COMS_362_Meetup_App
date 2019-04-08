package app.Controllers;

import app.MySQL.MySQLHelper;
import app.interfaces.GroupAssociationControllerInterface;
import app.models.Group;
import app.models.GroupAssociation;
import app.models.Profile;
import app.models.mappers.GroupAssociationMapper;

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
        MySQLHelper.executeUpdate("DELETE FROM meetup.groupAssociation " +
                "where profileid = " + profileId +
                " and groupid = " + groupId
        );

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
        GroupAssociationMapper gam = new GroupAssociationMapper();
        GroupAssociation ga = new GroupAssociation();
        ga.setGroupid(groupId);
        ga.setProfileid(profileId);
        MySQLHelper.executeUpdate(gam.toInsertQueryQuery(ga)
        );

        System.out.println("Successfully removed GroupAssociation between profile ID: " + profileId + " and group ID: " + groupId + "!");
    }
}
