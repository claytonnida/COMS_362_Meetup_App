package app.Controllers;

import app.App;
import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.GroupInvitationControllerInterface;
import app.models.Group;
import app.models.GroupInvitation;
import app.models.Profile;
import app.models.mappers.GroupInvitationMapper;
import app.models.mappers.ProfileMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupInvitationController implements GroupInvitationControllerInterface {

    /**
     * @see GroupInvitationControllerInterface#inviteToGroup(int)
     */
    @Override
    public void inviteToGroup(int groupId) {

        try {
            List<Profile> profileList = new ProfileMapper().createObjectList("SELECT * FROM meetup.profile");

            Profile chosenProfile = (Profile) InputReader.readFromOptions("Please select a profile from the list.", new ArrayList<>(profileList));

            if(InputReader.inputYesNo("Send group invite to " + chosenProfile.getName() + "?")) {
                if(doesInviteExist(chosenProfile.getId(), groupId)) {
                    System.out.println("That invite has already been sent!");
                }
                else if(new GroupAssociationController().doesGroupAssociationExist(chosenProfile.getId(), groupId)) {
                    System.out.println("That user is already part of that group!");
                }
                else {
                    createInvite(chosenProfile.getId(), groupId);
                }
            }
            else {
                System.out.println("Invitation Cancelled.");
            }
        }
        catch(SQLException e) {
            System.out.println("ERROR! Unable to create invite to group.");

            if(App.DEV_MODE)
                e.printStackTrace();
        }
    }

    /**
     * Checks the database to see if the invite between a {@link Profile} and {@link Group} with the given IDs exists.
     *
     * @param profileId
     *         The ID of the {@link Profile} in the invitation.
     * @param groupId
     *         The ID of the {@link Group} in the invitation.
     *
     * @return {@code true}, if the invite does exist on the database. {@code false} otherwise.
     *
     * @throws SQLException
     *         Occurs if there is an error when querying the database.
     */
    private boolean doesInviteExist(int profileId, int groupId) throws SQLException {
        ResultSet resultSet = MySQLHelper.executeQuery(
                String.format("SELECT * " +
                        "FROM meetup.groupInvitation " +
                        "WHERE groupid = %s " +
                        "AND profileid = %s", groupId, profileId)
        );

        return resultSet != null && resultSet.first();
    }

    /**
     * Adds a row to the groupInvitation table in the database with the given IDs
     *
     * @param profileId
     *         The ID of the {@link Profile} that is to receive the invitation.
     * @param groupId
     *         The ID of the {@link Group} that the invitation is to.
     */
    private void createInvite(int profileId, int groupId) {
        GroupInvitationMapper groupInvitationMapper = new GroupInvitationMapper();
        GroupInvitation groupInvitation = new GroupInvitation();
        groupInvitation.setProfileid(profileId);
        groupInvitation.setGroupid(groupId);
        MySQLHelper.executeUpdate(groupInvitationMapper.toInsertQueryQuery(groupInvitation));

        System.out.println("Invite was successfully sent!");
    }

    /**
     * Removes a row from the groupInvitation table in the database with the given IDs
     *
     * @param profileId
     *         The ID of the {@link Profile} that is to receive the invitation.
     * @param groupId
     *         The ID of the {@link Group} that the invitation is to.*
     */
    public void removeInvite(int profileId, int groupId) {
        MySQLHelper.executeUpdate(
                String.format("DELETE FROM meetup.groupInvitation " +
                        "WHERE profileid = %s " +
                        "AND groupid = %s", profileId, groupId)
        );

        System.out.println("Successfully removed invitation for profile ID: " + profileId + " to group ID: " + groupId + "!");
    }
}