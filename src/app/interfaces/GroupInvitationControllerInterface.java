package app.interfaces;

import app.models.Group;

public interface GroupInvitationControllerInterface {

    /**
     * Handles sending an invite to join a {@link Group} to another user.
     *
     * @param groupId The ID of the {@link Group} that the invite is to.
     */
    void inviteToGroup(int groupId);
}
