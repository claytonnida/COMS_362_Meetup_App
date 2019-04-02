package app.Controllers;

import app.App;
import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.GroupControllerInterface;
import app.models.Account;
import app.models.Group;
import app.models.GroupAssociation;
import app.models.Profile;
import app.models.mappers.AccountMapper;
import app.models.mappers.GroupAssociationMapper;
import app.models.mappers.GroupMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static app.MySQL.MySQLHelper.executeQuery;

public class GroupController implements GroupControllerInterface {

    public List<Group> getGroupsForUser(Profile p){
        try {

            //TODO make this a join statement
            GroupMapper gm = new GroupMapper();
            ResultSet rs = MySQLHelper.createStatement().executeQuery(
                    "Select groupid from meetup.groupAssociation where profileid = "+p.getId());

            List<Group> groups = new ArrayList<>();
            while(rs.next()){
                ResultSet gs = MySQLHelper.executeQuery("Select * from meetup.group where id = "+rs.getInt("groupid"));
                groups.addAll(gm.createObjectList(gs));
            }

            return groups;
        }catch (SQLException e){
            System.out.println("Failed to fetch groups");
            return new ArrayList<>();
        }
    }

    /**
     * This method searches and lists every group that contains any form of the user's input. If the user's input
     * is blank, the method will simply list every group
     *
     * @param sub_string The search criteria provided by the user for their search
     */
    @Override
    public List<Group> searchGroup(String sub_string) {
        try {
            GroupMapper gm = new GroupMapper();
            List<Group> list = gm.createObjectList(executeQuery("Select * from meetup.group"));
            for(int i = 0; i < list.size(); i++){
                if(!list.get(i).getName().toLowerCase().contains(sub_string.toLowerCase())) {
                    list.remove(i);
                }
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Sorry we cannot contact the database right now.");
        }
        return new ArrayList<Group>();
    }

    /**
     * Removes the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
     *
     * @param profileId
     * 		The ID of the {@link Profile} to disassociate.
     * @param groupId
     * 		The ID of the {@link Group} to disassociate.
     */
    @Override
    public void leaveGroup(int profileId, int groupId) {
        new GroupAssociationController().leaveGroup(profileId, groupId);
    }

    // TODO: Add JavaDocs
    @Override
    public void createGroup(Profile p) {
        Group group = new Group();
        group.setCreated_by(p.getId());

        System.out.println("Creating a new group");
        boolean editGroup = true;
        while(editGroup){
            editGroupFields(group);

            boolean confirm = InputReader.requestConfirmation(group);
            if(confirm){
                //TODO push changes to database
                System.out.println("Group confirmed.");


                boolean success = createGroupInDB(group);
                if(success)
                    editGroup = false;
            }else {
                if(!InputReader.inputYesNo("Continue Editing?")){
                    editGroup = false;
                }
            }
        }

        System.out.println("Group creation complete.");
    }

    public static boolean createGroupInDB(Group group){
        try{
            GroupMapper gm = new GroupMapper();
            String insertQuery = gm.toInsertQueryQuery(group);

            Statement stmt = MySQLHelper.createStatement();
            stmt.executeUpdate(insertQuery);
            ResultSet rs = stmt.executeQuery("Select @@identity");
            rs.next();

            int id = rs.getInt(1);
            GroupAssociation ga = new GroupAssociation();
            ga.setGroupid(id);
            ga.setProfileid(group.getCreated_by());
            GroupAssociationMapper gam = new GroupAssociationMapper();
            String insert = gam.toInsertQueryQuery(ga);
            stmt.executeUpdate(insert);

            return true;
        }catch (Exception e){
            System.out.println("Failed to send the group to the server.");
            e.printStackTrace();
            return false;
        }
    }

    // TODO: Add JavaDocs
    //TODO CLAYTON: this should take the group id and not the group name,
    //  Otherwise you will delete all groups with that name
    @Override
    public void removeGroup(Group group, Account account) {
    	if(group.getCreated_by() == account.getId())
    	{
                boolean confirm = InputReader.requestConfirmation(group);
                if(confirm){
                  
                	try {
                		Statement stmt = MySQLHelper.createStatement();
                		stmt.executeUpdate("DELETE FROM meetup.group WHERE id =" + group.getId() +";");
                		stmt.executeUpdate("DELETE FROM meetup.groupAssociation WHERE groupid =" + group.getId() +";");
                		System.out.println("Group " + group.getName() + " was deleted.");
                	}catch (Exception e){
                		System.out.println("Failed to remove group.");
                		e.printStackTrace();
                	}
               
                }
    	}else {
    		System.out.println("I'm sorry, you are not the creator of this group. So you cannot remove it.");
    	}
    	
    }

    // TODO: Add JavaDocs
    @Override
    public void rankGroup(int rank) {

    }

    /**
     * A series of prompts to guide user through editing their profile
     *
     * @param g The {@link Group} whose fields will be modified.
     */
    @Override
    public void editGroupFields(Group g){

        boolean edit = true;
        String[] options = new String[]{"done","name","visibility"};
        while(edit) {
            String option = InputReader.readFromOptions("Which field would you like to edit?", options);

            switch(option) {
                case "done":
                    edit = false;
                    break;
                case "name":
                    editGroupName(g);
                    break;
                case "visibility":
                    editGroupVisibility(g);
            }
        }
    }

    // TODO: Add JavaDocs
    @Override
    public void editGroupName(Group g){
        System.out.print("Your current group name is:\t");
        System.out.println(g.getName());

        String input = (InputReader.collectInput("Please enter a group name:"));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            g.setName(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editGroupName(g);
            }
        }
    }

    // TODO: Add JavaDocs
    @Override
    public void editGroupVisibility(Group g){
        System.out.print("Your current group visibility is:\t");
        System.out.println(g.getName());

        String input = (InputReader.readFromOptions("Select Visibility",Group.visibilityOptions));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            g.setIsPublic(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editGroupName(g);
            }
        }
    }

    public void manageGroups(Account account){
        GroupController gc = new GroupController();
        List<Group> groups = null;
        Group group;

        switch (InputReader.readFromOptions("What do you want to do?",
                new String[]{"Create a Group","All Groups","My Groups","Search For Groups","Exit"})){
            case "Create a Group":
                gc.createGroup(account.getProfile());
                break;
            case "Search For Groups":
                groups = gc.findGroups();
                group = selectGroup(groups,account);
                if(group==null) {
                    manageGroups(account);
                }
                else {
                    manageGroup(account,group);
                }
                // TODO Dan, after searching groups, you should open up GroupController.showGroups(...)
                //   - Supply your list of groups as arg
                break;
            case "My Groups":
                groups = gc.getGroupsForUser(account.getProfile());
                group = selectGroup(groups,account);
                if(group==null) {
                    manageGroups(account);
                }
                else {
                    manageGroup(account,group);
                }
                break;
            case "All Groups":
                groups = gc.searchGroup("");
                group = selectGroup(groups,account);
                if(group==null) {
                    manageGroups(account);
                }
                else {
                    manageGroup(account,group);
                }
                break;
        }
    }

    public List<Group> findGroups(){
        String input = InputReader.collectInput("Please enter a sub string to search all Groups containing the search value");
        GroupController gc = new GroupController();
        return gc.searchGroup(input);
    }

    public Group selectGroup(List<Group> groups, Account account){

        Group g = (Group)InputReader.readFromOptions("Choose a group",new ArrayList<>(groups));
        return g;
    }

    public void manageGroup(Account account, Group group){
        switch (InputReader.readFromOptions("Edit "+group.getName(),
                new String[]{"Edit Group","Leave Group","Delete Group","Exit"})){
            case "Edit Group":
                try{
                    int id = ((Account)App.sessionVariables.get("account")).getProfile().getId();
                    ResultSet rs = MySQLHelper.executeQuery("Select * from meetup.group where created_by = "+id
                            +" and id = "+group.getId());
                    if(!rs.next()){
                        System.out.println("You are not the owner of this group.");
                        return;
                    }
                }catch (SQLException e){
                    manageGroup(account,group);
                }
                editGroupFields(group);
                break;
            case "Delete Group":
                removeGroup(group, account);
                break;
            case "Leave Group":
                leaveGroup(account.getProfileid(),group.getId());
                break;
            case "Exit":
                manageGroups(account);
                break;
        }
    }
}
