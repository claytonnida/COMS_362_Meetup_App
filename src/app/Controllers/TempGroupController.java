package app.Controllers;

import app.InputReader;
import app.MySQL.MySQLHelper;
import app.models.Group;
import app.models.Profile;
import app.models.mappers.GroupMapper;

import java.sql.SQLException;

public class TempGroupController {

    public static void createGroup(Profile p){
        Group group = new Group();

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

    /**
     * A series of prompts to guide user through editing their profile
     */
    public static void editGroupFields(Group g){
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

    public static void editGroupName(Group g){
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



    public static void editGroupVisibility(Group g){
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
