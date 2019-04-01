package app.Controllers;

import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.GroupControllerInterface;
import app.models.Group;
import app.models.Profile;
import app.models.mappers.GroupMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static app.MySQL.MySQLHelper.*;

public class GroupController implements GroupControllerInterface {

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
            //List<Group> list = gm.createObjectList(executeQuery("Select * from meetup.group"));
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

                GroupMapper gm = new GroupMapper();
                String insertQuery = gm.toInsertQueryQuery(group);
                boolean success = MySQLHelper.executeUpdate(insertQuery);
                if(success)
                    editGroup = false;
            }
        }

        System.out.println("Group creation complete.");
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
}
