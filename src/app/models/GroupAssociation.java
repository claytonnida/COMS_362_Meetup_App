package app.models;

import app.interfaces.GroupAssociationInterface;

public class GroupAssociation implements GroupAssociationInterface {
    private int user_id;
    private int group_id;

    @Override
    public int getUser_id() {
        return user_id;
    }

    @Override
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public int getGroup_id() {
        return group_id;
    }

    @Override
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
