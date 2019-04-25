package app.interfaces;

import app.models.Group;
import app.models.Profile;

import java.util.List;

public interface GroupControllerInterface {

    /**
     * This method searches and lists every group that contains any form of the user's input. If the user's input
     * is blank, the method will simply list every group
     *
     * @param sub_string The search criteria provided by the user for their search
     */
    List<Group> searchGroup(String sub_string);

	/**
	 * Removes the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
	 *
	 * @param profileId
	 * 		The ID of the {@link Profile} to disassociate.
	 * @param groupId
	 * 		The ID of the {@link Group} to disassociate.
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

    // TODO: Add JavaDocs
    void createGroup(Profile p);

    /**
     * Removes the group from the system. This will also remove any associations 
     * from anyone that was a member of the group.
     * 
     * @param group
     * 		The group to be removed.
     */
    public void removeGroup(Group group);

    /**
     * Gives the group a rating of 1-5.
     * 
     * @param group
     * 		The group to rank.
     */
    void rankGroup(Group group);

    /**
     * A series of prompts to guide user through editing their profile
     *
     * @param g The {@link Group} whose fields will be modified.
     */
    void editGroupFields(Group g);

    // TODO: Add JavaDocs
    void editGroupName(Group g);

    // TODO: Add JavaDocs
    void editGroupVisibility(Group g);

}
