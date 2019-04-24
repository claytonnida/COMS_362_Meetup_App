package app.Controllers;

import app.App;
import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.GroupAssociationControllerInterface;
import app.models.Group;
import app.models.GroupAssociation;
import app.models.Profile;
import app.models.mappers.GroupAssociationMapper;
import app.models.mappers.ProfileMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupAssociationController implements GroupAssociationControllerInterface {

    //TODO: Javadoc
    public void inviteToGroup(int groupId) {
        ProfileController profileController = new ProfileController();

        ProfileMapper profileMapper = new ProfileMapper();

        Profile chosenProfile;
        try {
            List<Profile> profileList = profileMapper.createObjectList("SELECT * FROM meetup.profile");

            chosenProfile = (Profile) InputReader.readFromOptions("Please select a profile ID from the list.", new ArrayList<>(profileList));

        }
        catch(SQLException e) {
            System.out.println("ERROR! Unable to create invite to group.");

            if(App.DEV_MODE) {
                e.printStackTrace();
            }
            return;
        }

        if(InputReader.inputYesNo("Send group invite to " + chosenProfile.getName() + "?")) {
            try {
                if(doesInviteExist(chosenProfile.getId(), groupId)) {
                    System.out.println("That invite has already been sent!");
                } else {
                    createInvite(chosenProfile.getId(), groupId);
                }
            } catch (SQLException e) {
                if(App.DEV_MODE) e.printStackTrace();
            }
        }
    }

    //TODO: Javadoc
    private boolean doesInviteExist(int profileId, int groupId) throws SQLException {
        //TODO: Implement
        // If it doesn't work, check if the table still exists. Don't comment it out and push to master.
        ResultSet resultSet = MySQLHelper.executeQuery(
                String.format("SELECT * " +
                        "FROM meetup.groupInvitation " +
                        "WHERE groupid = %s " +
                        "AND profileid = %s", groupId, profileId)
        );

        return resultSet.first();
    }

    // TODO: Javadoc
    private void createInvite(int profileId, int groupId) {
        MySQLHelper.executeUpdate(String.format("INSERT INTO meetup.groupAssociation (profileid, groupid) VALUES (%d, %d)",
                profileId, groupId));
        System.out.println("Invite was successfully sent!");
    }

    // TODO: Javadoc
    public void removeInvite(int profileId, int groupId) {
        MySQLHelper.executeUpdate(
                String.format("DELETE FROM meetup.groupAssociation " +
                        "WHERE profileid = %s " +
                                "AND groupid = %s", profileId, groupId)
        );

        System.out.println("Successfully removed invitation for profile ID: " + profileId + " to group ID: " + groupId + "!");
    }

    // For debugging purposes
    public static void main(String[] args) {

    }

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
        MySQLHelper.executeUpdate(gam.toInsertQueryQuery(ga));

        System.out.println("Successfully created GroupAssociation between profile ID: " + profileId + " and group ID: " + groupId + "!");
    }
}
