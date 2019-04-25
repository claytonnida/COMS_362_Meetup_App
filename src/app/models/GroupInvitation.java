package app.models;

import app.App;
import app.interfaces.GroupInvitationInterface;
import app.interfaces.Selectable;
import app.models.mappers.GroupMapper;

import java.sql.SQLException;

public class GroupInvitation implements GroupInvitationInterface, Selectable {
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

    @Override
    public String getSelectionPrompt() {
        try {
            Group group = new GroupMapper().createObjectList("SELECT * FROM meetup.group WHERE id = " + groupid).get(0);
            return group.getName();
        } catch(Exception e) { // Could be an SQLException or a NullPointerException
            if(App.DEV_MODE) e.printStackTrace();
            return "";
        }
    }
}
