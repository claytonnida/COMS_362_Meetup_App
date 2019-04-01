package app.models;

import app.interfaces.GroupInterface;
import app.interfaces.Selectable;

public class Group implements GroupInterface, Selectable {

    private int group_id;
    private String name;
    private String isPublic;
    private int created_by;

    public static final String[] visibilityOptions = new String[]{"Public","Private"};

    @Override
    public int getCreated_by() {
        return created_by;
    }

    @Override
    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    @Override
    public int getGroup_id() {
        return group_id;
    }

    @Override
    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIsPublic() {
        return isPublic;
    }

    @Override
    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public String getSelectionPrompt() {
        return getName();
    }
}
