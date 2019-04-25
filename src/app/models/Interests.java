package app.models;

import app.interfaces.InterestsInterface;

public class Interests implements InterestsInterface {
    private int interestID;
    private String interestName;
    private int interestOf;

    @Override
    public int getInterestID() {
        return interestID;
    }

    @Override
    public void setInterestID(int interestID) {
        this.interestID = interestID;
    }

    @Override
    public String getInterestName() {
        return interestName;
    }

    @Override
    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }
    
    @Override
    public int getInterestOf() {
    	return interestOf;
    }
    
    @Override
    public void setInterestOf(int interestOf) {
        this.interestOf = interestOf;
    }
}
