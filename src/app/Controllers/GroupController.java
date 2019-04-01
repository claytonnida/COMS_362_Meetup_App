package app.Controllers;

import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.GroupControllerInterface;
import app.interfaces.Selectable;
import app.models.Account;
import app.models.Group;
import app.models.GroupAssociation;
import app.models.Profile;
import app.models.mappers.GroupAssociationMapper;
import app.models.mappers.GroupMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static app.MySQL.MySQLHelper.*;

import java.sql.ResultSet;
import java.sql.Statement;

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

    // TODO: Add JavaDocs
    /**
     * This method searches and lists every group that contains any form of the user's input. If the user's input
     * is blank, the method will simply list every group
     * @param sub_string the search criteria provided by the user for their search
     */
    @Override
    public List<Group> searchGroup(String sub_string) {
        try {
            GroupMapper gm = new GroupMapper();
            List<Group> list = gm.createObjectList(executeQuery("Select * from meetup.group where name like '%"+sub_string+"&'"));
            return list;
        } catch (SQLException e) {
            System.out.println("Sorry we cannot contact the database right now.");
        }
        return new ArrayList<Group>();
    }

    @Override
    public void leaveGroup(int accountId, int groupId) {
        new GroupAssociationController().leaveGroup(accountId, groupId);
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
    @Override
    public void removeGroup(String gname) {

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

        switch (InputReader.readFromOptions("What do you want to do?",
                new String[]{"Create a Group","My Groups","Search For Groups","Exit"})){
            case "Create a Group":
                gc.createGroup(account.getProfile());
                break;
            case "Search For Groups":
                // TODO Dan, after searching groups, you should open up GroupController.showGroups(...)
                //   - Supply your list of groups as arg
                break;
            case "My Groups":
                List<Group> groups = gc.getGroupsForUser(account.getProfile());
                selectGroup(groups,account);
                //TODO
                break;
        }
    }

    public void selectGroup(List<Group> groups, Account account){

        Group g = (Group)InputReader.readFromOptions("Choose a group",new ArrayList<Selectable>(groups));
        System.out.println("You chose "+g.getName());
    }
}
