package app.Controllers;

import app.MySQL.MySQLHelper;
import app.interfaces.GroupAssociationControllerInterface;

public class GroupAssociationController implements GroupAssociationControllerInterface {


    /**
     * Removes the {@link app.models.GroupAssociation} between an {@link app.models.Account} and a {@link app.models.Group}.
     *
     * @param accountId The ID of the {@link app.models.Account} to disassociate.
     * @param groupId The ID of the {@link app.models.Group} to disassociate.
     */
    @Override
    public void leaveGroup(int accountId, int groupId) {
        // TODO: Remove the group association with the given account ID and group ID
    }
}
