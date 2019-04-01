package app.interfaces;

public interface GroupAssociationControllerInterface {

    /**
     * Removes the {@link app.models.GroupAssociation} between an {@link app.models.Account} and a {@link app.models.Group}.
     *
     * @param accountId The ID of the {@link app.models.Account} to disassociate.
     * @param groupId The ID of the {@link app.models.Group} to disassociate.
     */
    void leaveGroup(int accountId, int groupId);
}
