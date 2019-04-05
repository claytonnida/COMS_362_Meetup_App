package app.interfaces;

import app.models.Account;
import app.models.Group;
import app.models.Profile;

import java.util.List;

public interface GroupControllerInterface {

    // TODO: Add JavaDocs
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

    // TODO: Add JavaDocs
    void createGroup(Profile p);

    // TODO: Add JavaDocs
    public void removeGroup(Group group);

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
    void rankGroup(int rank);

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
