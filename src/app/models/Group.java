package app.models;

import app.interfaces.GroupInterface;
import app.interfaces.Selectable;

public class Group implements GroupInterface, Selectable {

    private int id;
    private String name = "New Group";
    private String isPublic = "Public";
    private int created_by;
    private int rankTotal;
    private int numRanks;
    private double rankAvg;


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
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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
    public double getRankAvg() {
    	return rankAvg;
    }

    @Override
    public void setRankAvg(double rankAvg) {
    	this.rankAvg = rankAvg;
    }

    @Override
    public String getSelectionPrompt() {
        String rank = String.format("%.1f",getRankAvg());
        return getName() + "  Rating: " + rank + "/5";
    }

    @Override
    public String toString(){
        String str = "Name:\t"+getName()+
                "\nVisibility:\t"+getIsPublic();
        return str;
    }
}
