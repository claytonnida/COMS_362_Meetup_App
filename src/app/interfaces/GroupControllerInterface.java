package app.interfaces;

import app.models.Group;
import app.models.Profile;

public interface GroupControllerInterface {

    // TODO: Add JavaDocs
    void searchGroup(String sub_string);

    // TODO: Add JavaDocs
    void leaveGroup(int accountId, int groupId);

    // TODO: Add JavaDocs
    void createGroup(Profile p);

    // TODO: Add JavaDocs
    void removeGroup(String gname);

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
