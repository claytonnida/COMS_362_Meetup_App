package app.models;

import app.interfaces.GroupAssociationInterface;

public class GroupAssociation implements GroupAssociationInterface {
    private int profileid;
    private int groupid;

    @Override
    public int getProfileid() {
        return profileid;
    }

    @Override
    public void setProfileid(int profileid) {
        this.profileid = profileid;
    }

    @Override
    public int getGroupid() {
        return groupid;
    }

    @Override
    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }
}
