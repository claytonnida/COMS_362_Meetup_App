package app.interfaces;

import app.models.Group;
import app.models.Profile;

public interface GroupAssociationControllerInterface {

    /**
     * Removes the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
     *
     * @param profileId The ID of the {@link Profile} to disassociate.
     *
     * @param groupId The ID of the {@link Group} to disassociate.
     */
    void leaveGroup(int profileId, int groupId);
}
