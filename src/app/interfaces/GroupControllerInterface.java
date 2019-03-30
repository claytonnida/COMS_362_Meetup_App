package app.interfaces;

import app.models.Profile;

public interface GroupControllerInterface {

    void searchGroup(String sub_string);

    void leaveGroup(int accountId, int groupId);

    void createGroup(Profile p);

    void removeGroup(String gname);

    void rankGroup(int rank);








}
