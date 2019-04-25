package app.Controllers;

import static app.MySQL.MySQLHelper.executeQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.App;
import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.GroupControllerInterface;
import app.models.Account;
import app.models.Group;
import app.models.GroupAssociation;
import app.models.Profile;
import app.models.mappers.GroupAssociationMapper;
import app.models.mappers.GroupMapper;
import app.serverclient.ChatGUI;

public class GroupController implements GroupControllerInterface {

    //TODO: Javadoc
    public void inviteToGroup(int groupId) {
        new GroupAssociationController().inviteToGroup(groupId);
    }

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
            if(App.DEV_MODE)
                e.printStackTrace();
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
                    i--;
                }else
                if(list.get(i).getIsPublic().equals("Private")){
                    list.remove(i);
                    i--;
                }
            }
            return list;
        } catch (SQLException e) {
            System.out.println("Sorry we cannot contact the database right now.");
            if(App.DEV_MODE)
                e.printStackTrace();
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

    /**
    *Joins the {@link app.models.GroupAssociation} between a {@link Profile} and a {@link Group}.
    *
    * @param profileId
    *     The ID of the {@link Profile} to associate with a Group.
    * @param groupId
    *     The ID of the {@link Group} to associate.
    */
    @Override
    public void joinGroup(int profileId, int groupId) {
        new GroupAssociationController().joinGroup(profileId, groupId);
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

            boolean confirm = InputReader.requestConfirmation(group.getName());
            if(confirm){
                
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
            if(App.DEV_MODE)
                e.printStackTrace();
            return false;
        }
    }

    
    /** 
     * @see app.interfaces.GroupControllerInterface#removeGroup(app.models.Group)
     */
    @Override
    public void removeGroup(Group group) {
        boolean confirm = InputReader.requestConfirmation(group.getName());
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
    }

    
    /**
     * @see app.interfaces.GroupControllerInterface#rankGroup(app.models.Group)
     */
    @Override
    public void rankGroup(Group group) {
    	String prompt = "Please enter a ranking of 1-5. (5 being the highest)";
    	int rank = InputReader.readInputInt(prompt);
    	System.out.println("Rank entered was " +rank);
    	if(rank != 1 && rank != 2 && rank != 3 && rank != 4 && rank != 5)
    	{
    		System.out.println("I'm sorry, that wasn't a correct input.");
        	rankGroup(group);
    	}
    	boolean confirm = InputReader.requestConfirmation(rank);
        if(confirm){
        	try {
        		Statement stmt = MySQLHelper.createStatement();
        		ResultSet rs = stmt.executeQuery("SELECT * FROM meetup.group WHERE id=" + group.getId() + ";");
        		rs.next();
        		int rankTotal = rs.getInt("rankTotal") + rank;
        		int numRanks = rs.getInt("numRanks") + 1;
        		stmt.executeUpdate("UPDATE meetup.group SET rankTotal ="+rankTotal+", numRanks ="+numRanks+", rankAvg="+((double)rankTotal/(double)numRanks) +" WHERE id="+ group.getId() +";");

        		System.out.println("You rated " + group.getName() + " with a rating of " + rank +".");
        	}catch (Exception e){
        		System.out.println("Failed to rank group.");
        		if(App.DEV_MODE)
        		    e.printStackTrace();
        	}
        }
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

    /**
    * Changes the group visibility to public.
    *
    * @param g The {@link Group} whose fields will be modified.
    */
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
        new String[]{"Groups I'm In","My Groups","Create a Group","All Groups","Search For Groups","Exit"})){
            case "Create a Group":
            gc.createGroup(account.getProfile());
            break;
            case "My Groups":
            groups = getGroupsByUser(account);
            group = selectGroup(groups,account);
            if(group==null) {
                manageGroups(account);
            }
            else {
                manageGroup(account,group);
            }
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
            case "Groups I'm In":
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

    public List<Group> getGroupsByUser(Account account){
        GroupMapper gm = new GroupMapper();
        try {
            return gm.createObjectList("Select * from meetup.group where created_by = "+account.getProfile().getId());
        }catch (SQLException e){
            System.out.println("Sorry, couldn't get your groups");
            if(App.DEV_MODE)
                e.printStackTrace();
            return new ArrayList<>();
        }
    }

    

    public void joinGroup(int profileId, int groupId){
        GroupAssociationController gac = new GroupAssociationController();
        gac.joinGroup(profileId, groupId);
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

    /**
    * Guide user through prompts to manage a particular group
    * @param account
    * @param group
    */
    public void manageGroup(Account account, Group group){
        String[] options = new String[]{"Open Chat","Edit Group","Leave Group","Join Group","Rank Group","Delete Group","Exit"};
        GroupMapper gm = new GroupMapper();

        //Ask user what they would like to do
        switch (InputReader.readFromOptions("Edit "+group.getName(), options)){
<<<<<<< HEAD
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
            case "Leave Group":
            leaveGroup(account.getProfileid(),group.getId());
            break;
            //  case "Edit Group":
            //      if(isOwnerOfGroup(account,group)) {
            //            editGroupFields(group);
            //            String query = gm.toUpdateQueryQuery(group);
            //           MySQLHelper.executeUpdate(query);
            //            break;
            //        }else {
            //             System.out.println("Cannot edit this group because you are not the owner");
            //         }
=======
            case "Open Chat":
                try {
                    ChatGUI tg = new ChatGUI(group,((Account)App.sessionVariables.get("account")).getProfile());
                    tg.loadMessages();
                    tg.open();
                }catch (Exception e){
                    System.out.println("Oops, couldn't open chat at this time!");
                    if(App.DEV_MODE)
                    e.printStackTrace();
                }
                break;
            case "Join Group":
                joinGroup(account.getProfile().getId(),group.getId());
                manageGroups(account);
                break;
            case "Leave Group":
                leaveGroup(account.getProfileid(),group.getId());
                manageGroups(account);
                break;
>>>>>>> 80fe2523bbb5ed148209ed26b2bbd72856d8f60c

            case "Edit Group":
                if(isOwnerOfGroup(account,group)) {
                    editGroupFields(group);
                    String query = gm.toUpdateQueryQuery(group);
                    MySQLHelper.executeUpdate(query+" where id = "+group.getId());
                }else {
                    System.out.println("Cannot edit this group because you are not the owner");
                }
                manageGroups(account);
                break;

            case "Rank Group":
            	rankGroup(group);
                manageGroups(account);
            	break;
            case "Delete Group":
            if(isOwnerOfGroup(account,group)) {
                removeGroup(group);
                break;
            }else {
                System.out.println("Cannot delete this group because you are not the owner");
            }
            //TODO Implement remove group

            //don't add a break; - That way we go back to manageGroups if editGroupFields is denied
            case "Exit":
            manageGroups(account);
            break;
        }
    }

    public boolean isOwnerOfGroup(Account a, Group g){
        try{

            //Check if user is owner of the group, if not, they cannot edit
            int id = a.getProfile().getId();
            ResultSet rs = MySQLHelper.executeQuery("Select * from meetup.group where created_by = "+id
            +" and id = "+g.getId());

            //rs.next() = false when no rows are returned. ie user is not owner
            return rs.next();
        }catch (SQLException e){
            if(App.DEV_MODE)
                e.printStackTrace();
            return false;
        }
    }
}
