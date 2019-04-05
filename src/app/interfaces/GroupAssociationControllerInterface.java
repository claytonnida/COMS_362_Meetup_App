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

    /**
    *Joins the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
    *
    * @param profileId
    *     The ID of the {@link Profile} to associate with a Group.
    * @param groupId
    *     The ID of the {@link Group} to associate.
    */
<<<<<<< HEAD
    void joinGroup(int profileId, int groupId);
=======
    public void joinGroup(int profileId, int groupId);
    
>>>>>>> 179a1e69ad845c6dc896a33a6b8a77cb310cf735
}
