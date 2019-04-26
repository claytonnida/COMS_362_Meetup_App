package app.Controllers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONArray;

import app.App;
import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.ProfileControllerInterface;
import app.models.Account;
import app.models.Group;
import app.models.GroupInvitation;
import app.models.Profile;
import app.models.mappers.GroupInvitationMapper;
import app.models.mappers.ProfileMapper;
import javafx.util.Pair;

public class ProfileController implements ProfileControllerInterface {

    /**
     * Creates instance of a {@link Profile} and prompts users to fill out its fields.
     *
     * @return
     *      Instance of {@link Profile}.
     */
    @Override
    public Profile createProfile() {
        System.out.println("Time to create your profile!");

        Profile newProfile = new Profile();
        boolean editProf = true;
        while(editProf){
            editProfileFields(newProfile);

            boolean confirm = InputReader.requestConfirmation(newProfile);
            if(confirm){
                //TODO push changes to database
                System.out.println("Profile confirmed.");
                editProf = false;
            }
        }
        System.out.println("Profile creation complete.");

        return newProfile;
    }

    /**
     * A series of prompts to guide user through editing their profile.
     *
     * @param p {@link Profile} to edit the fields of.
     */
    @Override
    public void editProfileFields(Profile p){
        boolean edit = true;
        while(edit) {
            String option = InputReader.readFromOptions("Which field would you like to edit?", Profile.OPTIONS);

            switch(option) {
                case "done":
                    edit = false;
                    break;
                case "Name":
                    editName(p);
                    break;
                case "About Me":
                    editAboutMe(p);
                    break;
                case "Interests":
                    editInterests(p);
                    break;
                case "Age":
                    editAge(p);
                    break;
                case "Gender Identity":
                    editGenderId(p);
                    break;
                case "Sexual Preference":
                    editSexPref(p);
                    break;
                case "Major":
                    editMajor(p);
                    break;
                case "Spirit Animal":
                    editSpiritAnimal(p);
                    break;
                case "Zodiac Sign":
                    editZodiacSign(p);
                    break;
                case "Picture":
                    setPicture(p);
                    break;
                case "Blocked Users":
                    blockUser(p);
                    break;
                case "Unblocked Users":
                    unblockUser(p);
                    break;
            }
        }

        try {
            if(InputReader.requestConfirmation(p))
                saveProfile(p);
            else
                System.out.println("Profile changes discarded.");
        }catch (SQLException e){
            System.out.println("Couldn't save profile to server at this time.");
            if(App.DEV_MODE)
                e.printStackTrace();
        }
    }

    /**
     * A series of prompts to guide the user through creating their Profile picture
     */
    public static void setPicture(Profile p) {
        //String input = InputReader.collectInput("Is the picture you wish to use saved in the file structure? [y/n]");
        //boolean upload_complete = false;
        //if (input.equals("y")) {
        if(p.getPictureURL() == null) {
            String input = InputReader.collectInput("Please paste the URL of the image you wish to save");
            try {
                ///////

                File image_file = new File(input);
                String file_name = input;

                //TODO: Make sure to remove this before submitting
                //URL url = new URL(input);
                //URLConnection connection = url.openConnection();
                //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
                BufferedImage input_picture = ImageIO.read(image_file);
                boolean confirm = InputReader.requestConfirmation(input);
                if (confirm) {
                    p.setPicture(input_picture);
                    p.setProfile_pic(input_picture);
                    p.setPictureURL(file_name.replaceAll("\\\\","/"));
                    System.out.println(p.getPictureURL());
                    //Quick Test
                    //
                    //BufferedImage test_image = ImageIO.read(new File(p.getPictureURL()));
                    //displayPicture(test_image);
                    //
                    System.out.println("Your profile picture was added successfully! The following picture will be displayed.");
                    displayPicture(p.getPicture());
                } else {
                    boolean cancel = InputReader.requestCancel();
                    if (cancel) {
                        return;
                    } else {
                        setPicture(p);
                    }
                }
            } catch (IOException e) {
                System.out.println("The file name you specified was invalid, please try again");
                setPicture(p);
            }
        }
        else{
            try {
                File f = new File(p.getPictureURL());
                BufferedImage img = ImageIO.read(f);
                displayPicture(img);
                boolean confirm = InputReader.requestConfirmation("Your current profile image is displayed as shown. Are you sure you want to change?");
                if(confirm){
                    p.setPictureURL(null);
                    setPicture(p);
                }
                else{
                    System.out.println("Your Profile picture will remain unchanged");
                }
            }
            catch(IOException e){
                System.out.println("Oops! Looks like an error occured. Your old Profile picture may no longer be stored in your computer");
                p.setPictureURL(null);
                setPicture(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their online status.
     *
     * @param p
     * 		{@link Profile} to edit the online status of.
     */
    @Override
    public void editOnlineStatus(Profile p) {
            System.out.print("Your are currently appearing:\t");
            System.out.println((p.getAppearOffline() == 1 ? "Offline" : "Online"));

            String input = (InputReader.readFromOptions("Select Online Status", new String[]{"Online", "Appear Offline"}));
        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            if(input.contains("Offline")){
                p.setAppearOffline(1);
            }else{
                p.setAppearOffline(0);
            }
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editOnlineStatus(p);
            }
        }
    }

    /**
     * This is a helper method that will display images using JFrame
     * @param img BufferedImage that is to be displayed
     */
    public static void displayPicture(BufferedImage img){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(1, 1));
        ImagePanel panel = new ImagePanel(img);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * A series of prompts to guide user through editing their zodiac sign.
     *
     * @param p {@link Profile} to set the zodiac sign of.
     */
    @Override
    public void editZodiacSign(Profile p){
        System.out.print("Your current zodiac sign is:\t");
        System.out.println(p.getZodiac());

        String input = (InputReader.collectInput("Please enter a new zodiac sign:"));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            p.setZodiac(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editZodiacSign(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their spirit animal.
     *
     * @param p {@link Profile} to set the spirit animal of.
     */
    @Override
    public void editSpiritAnimal(Profile p){
        System.out.print("Your current spirit animal is:\t");
        System.out.println(p.getSpiritAnimal());

        String input = (InputReader.collectInput("Please enter a new spirit animal:"));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            p.setSpiritAnimal(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editSpiritAnimal(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their name.
     *
     * @param p {@link Profile} to set the name of.
     */
    @Override
    public void editName(Profile p){
        System.out.print("Your current name is:\t");
        System.out.println(p.getName());

        String input = (InputReader.collectInput("Please enter a new name:"));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            p.setName(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editName(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their major.
     *
     * @param p {@link Profile} to set the major of.
     */
    @Override
    public void editMajor(Profile p){
        System.out.print("Your current major is:\t");
        System.out.println(p.getMajor());

        String input = (InputReader.collectInput("Please enter a new major:"));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            p.setMajor(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editMajor(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their sexual preference.
     *
     * @param p {@link Profile} to set the sexual preferences of.
     */
    @Override
    public void editSexPref(Profile p){
        System.out.print("Your current sexual preference is:\t");
        System.out.println(p.getSexualPref());
        String input = (InputReader.collectInput("Please enter a new sexual preference:"));

        boolean  confirm = InputReader.requestConfirmation(input);
        if(confirm){
            p.setSexualPref(input);
        }else{
            boolean cancel = InputReader.requestCancel();
            if(cancel){
                return;
            }else{
                editSexPref(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their about me section.
     *
     * @param p {@link Profile} to set the "about me" section of.
     */
    @Override
    public void editAboutMe(Profile p) {
        System.out.println("Your current 'About Me' section is:");
        System.out.println(p.getAboutMe());
        String input = (InputReader.collectInput("Please describe yourself."));

        boolean confirm = InputReader.requestConfirmation(input);
        if(confirm) {
            p.setAboutMe(input);
        }
        else {
            boolean cancel = InputReader.requestCancel();
            if(cancel) {
                return;
            }
            else {
                editAboutMe(p);
            }
        }
    }

    // TODO: Javadocs
    public void sortByInterestCommonality(Profile target, List<Profile> otherProfies){
        //Build a sortable list
        List<Pair<Integer,Profile>> profilePairs = new ArrayList<>();
        for(Profile p: otherProfies){
            int similarities = 0;
            for(int i = 0; i < target.getInterests().length(); i++){
                String myInterest = target.getInterests().getString(i);
                for(int j = 0; j < p.getInterests().length(); j++){
                    if(myInterest.equalsIgnoreCase(p.getInterests().getString(j))){
                        similarities++;
                    }
                }
            }
            Pair<Integer,Profile> pair = new Pair<>(similarities,p);
            profilePairs.add(pair);
        }

        otherProfies.clear();
        Collections.sort(profilePairs, new Comparator<Pair<Integer, Profile>>() {
            @Override
            public int compare(Pair<Integer, Profile> o1, Pair<Integer, Profile> o2) {
                return o2.getKey() - o1.getKey();
            }
        });

        //Put profiles in proper order
        for(Pair<Integer,Profile> pair: profilePairs){
            if(pair.getKey()==0)continue;
            otherProfies.add(pair.getValue());
        }
    }

    //TODO: Javadocs
    @Override
    public void editInterests(Profile p) {
    	String option;
    	boolean confirm;
        JSONArray interests = p.getInterests();
        System.out.println("Your current 'Interests' are:\n" + interests.toString());
        String[] options = new String[]{"Add Interest", "Remove Interest", "Done"};
        option = InputReader.readFromOptions("What would you like to do?", options);

        switch(option)
        {
        	 case "Add Interest":
        		 String input = (InputReader.collectInput("Enter the Interest you would like to add:\n"));
        		 confirm = InputReader.requestConfirmation(input);
        	        if(confirm) {
        	        	if(!interests.toString().contains(input))
        	        	{
        	        		interests.put(input);
            	            p.setInterests(interests.toString());
            	            editInterests(p);
        	        	}
        	        	else
        	        	{
        	        		System.out.println("You are already interested in that!");
        	        	}

        	        }
        	        else {
        	            boolean cancel = InputReader.requestCancel();
        	            if(cancel) {
        	                return;
        	            }
        	            else {
        	            	editInterests(p);
        	            }
        	        }
        		 break;
        	 case "Remove Interest":
        		 String[] removeOptions = new String[interests.length()];
        		 for (int i = 0; i < interests.length(); i++)
				 {
					removeOptions[i] = interests.getString(i);
				 }

        		 option = InputReader.readFromOptions("Enter the Interest you would like to remove:", removeOptions);
        		 confirm = InputReader.requestConfirmation(option);
        	        if(confirm) {
        	        	for (int i = 0; i < interests.length(); i++)
         				{
         					if(removeOptions[i].equals(option))
         					{
         						 interests.remove(i);
         						 p.setInterests(interests.toString());
         						editInterests(p);
         					}
         				}
        	        }
        	        else {
        	            boolean cancel = InputReader.requestCancel();
        	            if(cancel) {
        	                return;
        	            }
        	            else {
        	            	editInterests(p);
        	            }
        	        }

        		 break;
        	 case "Done":
        		 confirm = InputReader.requestConfirmation("Are you done? Your Interests are now:\n" + interests.toString());
     	        if(confirm) {
        		 break;
     	        }
     	       else {
   	            boolean cancel = InputReader.requestCancel();
   	            if(cancel) {
   	                return;
   	            }
   	            else {
   	            	editInterests(p);
   	            }
   	        }
        }



    }

    /**
     * Add {@link Profile} to the blocked list.
     *
     * @param p {@link Profile} to save in the database.
     *
     * @throws SQLException
     */
    @Override
    public void blockUser(Profile p) throws SQLException {
        System.out.print("You're current 'Blocked Users' are: ");
        JSONArray blockedUsers = p.getBlockedUsers();

        String input = (InputReader.collectInput("Block a user"));

        boolean confirm = InputReader.requestConfirmation(input);
        if(confirm) {
        	blockedUsers.put(input);
            p.addBlockedUsers(blockedUsers.toString());
        }
        else {
            boolean cancel = InputReader.requestCancel();
            if(cancel) {
                return;
            }
            else {
            	blockUser(p);
            }
        }
    }

    /**
     * Remove {@link Profile} from the blocked list.
     *
     * @param p {@link Profile} to remove in the database.
     *
     * @throws SQLException
     */
    @Override
    public void unblockUser(Profile p) throws SQLException {
        System.out.print("You're current 'Blocked Users' are: ");
        JSONArray blockedUsers = p.getBlockedUsers();

        String input = (InputReader.collectInput("Unblock a user"));

        boolean confirm = InputReader.requestConfirmation(input);
        if(confirm) {
            int userPos = 0;
            for (int i = 0; i < blockedUsers.length(); i++) {
                if (blockedUsers.getJSONObject(i).toString().equals(input)) {
                    userPos = i;
                    break;
                }
            }
            if (userPos != 0) {
                blockedUsers.remove(userPos);
                p.removeBlockedUsers(blockedUsers.toString());
            }
        }
        else {
            boolean cancel = InputReader.requestCancel();
            if(cancel) {
                return;
            }
            else {
            	unblockUser(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their gender identity.
     *
     * @param p {@link Profile} to set the gender identity of.
     */
    @Override
    public void editGenderId(Profile p){
        System.out.print("Your current gender identity is:\t");
        System.out.println(p.getGenderId());
        String input = (InputReader.collectInput("Please enter a new gender identity:"));

        boolean confirm = InputReader.requestConfirmation(input);
        if(confirm) {
            p.setGenderId(input);
        }
        else {
            boolean cancel = InputReader.requestCancel();
            if(cancel) {
                return;
            }
            else {
                editGenderId(p);
            }
        }
    }

    /**
     * A series of prompts to guide user through editing their age.
     *
     * @param p {@link Profile} to set the age of.
     */
    @Override
    public void editAge(Profile p) {
        System.out.print("Your current age is:\t");
        System.out.println(p.getAge());
        int input = (InputReader.readInputInt("Please enter a age:"));

        boolean confirm = InputReader.requestConfirmation(input);
        if(confirm) {
            p.setAge(input);
        }
        else {
            boolean cancel = InputReader.requestCancel();
            if(cancel) {
                return;
            }
            else {
                editAge(p);
            }
        }
    }

    /**
     * Update the {@link Profile} as a row in the database.
     *
     * @param p {@link Profile} to update.
     *
     * @throws SQLException
     */
    @Override
    public void updateProfile(Profile p) throws SQLException{

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(p.getProfile_pic(), "png", out);
            byte[] buf = out.toByteArray();
            // setup stream for blob
            ByteArrayInputStream inStream = new ByteArrayInputStream(buf);

            ProfileMapper pm = new ProfileMapper();
            String query = pm.toUpdateQueryQuery(p);
            PreparedStatement ps = MySQLHelper.getConnection().prepareStatement(query);
            ps.setBinaryStream(1,inStream,inStream.available());
            ps.executeUpdate();

        }catch (Exception e){

            ProfileMapper pm = new ProfileMapper();
            String query = pm.toUpdateQueryQuery(p);
            query.replaceAll("profile_pic = \\?,","");
            PreparedStatement ps = MySQLHelper.getConnection().prepareStatement(query);
            ps.executeUpdate();
        }

    }

    /**
     * Insert the {@link Profile} as a row to the database.
     *
     * @param p {@link Profile} to insert.
     *
     * @throws SQLException
     */
    @Override
    public void insertProfile(Profile p )throws SQLException{
        ProfileMapper pm = new ProfileMapper();
        Statement stmt = MySQLHelper.createStatement();
        String query = pm.toInsertQueryQuery(p);
        stmt.executeUpdate(query);

    }

    /**
     * Lists {@link Profile}s.
     */
    @Override
    public void listProfiles() {
        List<Integer> profileIdList = new ArrayList<>();

        try {
            Statement stmt = MySQLHelper.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from meetup.profile");

            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 1; i < rsmd.getColumnCount(); i++) {
                    if (rsmd.getColumnName(i).equals("id")) {
                        profileIdList.add(rs.getInt(i));
                    }

                    System.out.print(rs.getString(i) + ",\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            if (App.DEV_MODE)
                e.printStackTrace();
        }
    }

    /**
     * This is a helper class for the setPicture method
     * It creates a useable/displayable Panel to display the Profile picture
     */
    static class ImagePanel extends JPanel {
        private final BufferedImage image;

        ImagePanel(BufferedImage image)
        {
            this.image = image;
        }

        @Override
        public Dimension getPreferredSize() {
            if (super.isPreferredSizeSet()) {
                return super.getPreferredSize();
            }
            return new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }

    /**
     * Sends the profile to database.
     * If profile.id == 0, this method will create an Insert query
     * else this method will create an Update query
     *
     * @param p {@link Profile} to save in the database.
     *
     * @return The ID of the {@link Profile} that has been saved or updated in the database.
     *
     * @throws SQLException
     */
    @Override
    public int saveProfile(Profile p)throws SQLException {
        ProfileMapper pm = new ProfileMapper();

        try {
            if (p.getId() == 0) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(p.getProfile_pic(), "png", out);
                byte[] buf = out.toByteArray();
                // setup stream for blob
                ByteArrayInputStream inStream = new ByteArrayInputStream(buf);

                pm = new ProfileMapper();
                String query = pm.toInsertQueryQuery(p);
                PreparedStatement ps = MySQLHelper.getConnection().prepareStatement(query);
                ps.setBinaryStream(1, inStream, inStream.available());
                ps.executeUpdate();

                ResultSet rs = ps.executeQuery("Select @@identity");
                rs.next();
                return rs.getInt(1);
            } else {
                updateProfile(p);
                return p.getId();
            }
        }catch (Exception e){
            if(App.DEV_MODE)
                e.printStackTrace();
        }

        return p.getId();
    }

    //TODO: Add this to the UI! Dan!
    /**
     * Returns a version of the given {@link List<Integer>} with "offline" connections removed.
     *
     * @param profileIdList {@link List<Integer>} of {@link Profile} IDs to filter.
     *
     * @return
     *      {@link List<Integer>} of IDs associated with online {@link Profile}s.
     */
    @Override
    public List<Integer> filterOnlineConnections(List<Integer> profileIdList) {

        if(profileIdList == null) {
            throw new IllegalArgumentException("ERROR! Given pid List cannot be null!");
        }

        Iterator<Integer> iterator = profileIdList.iterator();

        while(iterator.hasNext()) {
            Integer pid = iterator.next();
            if(appearsOffline(pid) || !checkOnlineStatus(pid)) {
                profileIdList.remove(pid);
            }
        }

        return profileIdList;
    }


    /**
     * Queries the database for the "appearsOffline" column for the given Profile ID.
     *
     * @param pid The ID of the {@link Profile} to query the database for.
     *
     * @return
     *      {@code true} if the "appearsOffline" column for given {@link Profile} in the database is set to {@code true}. {@code false}, otherwise.
     */
    @Override
    public boolean appearsOffline(int pid) {

        ResultSet rs = MySQLHelper.executeQuery("SELECT appearOffline FROM meetup.profile where id = " + pid);

        try {
            rs.next();
            return rs.getInt(1) == 1;
        }
        catch(SQLException e) {
            if(App.DEV_MODE)
                e.printStackTrace();
            return false;
        }
        catch(NullPointerException e) {
            System.out.println("ERROR! The given Profile ID does not exist or the \"apearOffline\" column has a value of null!");
            return false;
        }
    }

    /**
     * Pings the server to see if {@link Profile} associated with given ID is online.
     *
     * @param profileId The ID of the {@link Profile} to ping the server for.
     *
     * @return
     *      {@code true} if the given {@link Profile} is online. {@code false}, otherwise.
     */
    @Override
    public boolean checkOnlineStatus(int profileId) {
        // TODO: Ping the server for the actual connection status

        ResultSet rs = MySQLHelper.executeQuery("SELECT isOnline FROM meetup.profile where id = " + profileId);

        try {
            rs.next();
            return rs.getInt(1) == 1;
        }
        catch(SQLException e) {
            if(App.DEV_MODE)
                e.printStackTrace();
            return false;
        }
        catch(NullPointerException e) {
            if(App.DEV_MODE)
                e.printStackTrace();
            System.out.println("ERROR! The given Profile ID does not exist or the \"apearOffline\" column has a value of null!");
            return false;
        }
    }

    public Profile selectProfile(List<Profile> profiles, Account account){

        Profile p = (Profile)InputReader.readFromOptions("Choose a profile",new ArrayList<>(profiles));
        return p;
    }

    /**
     * @see ProfileControllerInterface#viewInvitations(int)
     */
    public void viewInvitations(int profileId) {

        try {
            List<GroupInvitation> invitationList = new GroupInvitationMapper().createObjectList("SELECT * FROM meetup.groupInvitation WHERE profileid = " + profileId);

            GroupInvitation groupInvitation = (GroupInvitation) InputReader.readFromOptions("Select an invitation from the list to respond.",
                    new ArrayList<>(invitationList));

            if(groupInvitation != null) { // True, when the user has selected an invitation. False, if they select "Cancel"
                respondToGroupInvite(groupInvitation.getProfileid(), groupInvitation.getGroupid());
            }

            System.out.println(groupInvitation == null ? null : groupInvitation.getSelectionPrompt());

        }
        catch(SQLException e) {
            System.out.println("Error! Unable to get invitation list!");

            if(App.DEV_MODE) e.printStackTrace();
        }
    }

    /**
     * @see ProfileControllerInterface#respondToGroupInvite(int, int)
     */
    public void respondToGroupInvite(int profileId, int groupId) {
        boolean acceptGroupInvite = InputReader.inputYesNo("Would you like to join the group " + groupId + "?");

        if(acceptGroupInvite) new GroupAssociationController().joinGroup(profileId, groupId);

        new GroupInvitationController().removeInvite(profileId, groupId);
    }

    // TODO: Javadocs
    public void viewSuggestedProfiles(Account acc) {
        Profile myProfile = acc.getProfile();
        ProfileMapper pm = new ProfileMapper();
        JSONArray blocked = myProfile.getBlockedUsers();
        try {
            List<Profile> profiles = pm.createObjectList("Select * from meetup.profile where id != " + myProfile.getId());
            sortByInterestCommonality(myProfile,profiles);

            //Remove blocked users
            for (int i = 0;i < profiles.length(); i++) {
                for (int j = 0;j < blocked.length(); j++) {
                    if (profiles.get(i).getName().equalsIgnoreCase(blocked.getJSONObject(j).toString())) {
                        profiles.remove(i);
                    }
                }
            }

            if(profiles.size() == 0){
                System.out.println("Sorry, no one has the same interests as you. You're one of a kind!");
                return;
            }
            Profile selected = (Profile)InputReader.readFromOptions("Select a profile to view",
                    new ArrayList<>(profiles));

            System.out.println(selected);
        }catch (Exception e){
            if(App.DEV_MODE)
                e.printStackTrace();
            System.out.println("Sorry, we're having trouble doing that right now.");
        }
    }

    // TODO: Javadocs
    public void browseProfiles(Account acc) {
        Profile myProfile = acc.getProfile();
        JSONArray blocked = myProfile.getBlockedUsers();
        switch (InputReader.readFromOptions("Please choose one.",
                new String[]{"All Profiles","Suggested Profiles","Random Match"})){
            case "Suggested Profiles":
                if(acc.getProfile().getInterests().length()==0) {
                    System.out.println("You need to set some interests before we can do that!");
                    editInterests(acc.getProfile());
                }else
                    viewSuggestedProfiles(acc);
                break;
            case "All Profiles":
                try {
                    ProfileMapper pm = new ProfileMapper();
                    List<Profile> profileList = pm.createObjectList("Select * from meetup.profile where id != " +
                            acc.getProfile().getId());
                    //Remove blocked users
                    for (int i = 0;i < profileList.length(); i++) {
                        for (int j = 0;j < blocked.length(); j++) {
                            if (profileList.get(i).getName().equalsIgnoreCase(blocked.getJSONObject(j).toString())) {
                                profileList.remove(i);
                            }
                        }
                    }
                    Profile p = selectProfile(profileList,acc);
                    System.out.println(p);
                    boolean confirm = InputReader.requestConfirmation("Would you like to rate this profile?");
                    if(confirm)
                    {
                    	rateProfile(p);
                    }
                }catch (Exception e){
                    System.out.println("Can't browse files at this time.");
                }
        }


    }

    public String chooseFilter(String[] filters){
        String choice = InputReader.readFromOptions("Please select your filter", filters);
        return choice;
    }

    /**
     * @see app.interfaces.GroupControllerInterface#rankGroup(app.models.Group)
     */
    @Override
    public void rateProfile(Profile p) {
    	String prompt = "Please enter a Rating of 1-5. (5 being the highest)";
    	int rank = InputReader.readInputInt(prompt);
    	System.out.println("Rating entered was " +rank);
    	if(rank != 1 && rank != 2 && rank != 3 && rank != 4 && rank != 5)
    	{
    		System.out.println("I'm sorry, that wasn't a correct input.");
    		rateProfile(p);
    	}
    	boolean confirm = InputReader.requestConfirmation(rank);
        if(confirm){
        	try {
        		Statement stmt = MySQLHelper.createStatement();
        		ResultSet rs = stmt.executeQuery("SELECT * FROM meetup.profile WHERE id=" + p.getId() + ";");
        		rs.next();
        		int rankTotal = rs.getInt("rankTotal") + rank;
        		int numRanks = rs.getInt("numRanks") + 1;
        		stmt.executeUpdate("UPDATE meetup.profile SET rankTotal ="+rankTotal+", numRanks ="+numRanks+", rankAvg="+((double)rankTotal/(double)numRanks) +" WHERE id="+ p.getId() +";");

        		System.out.println("You rated " + p.getName() + " with a rating of " + rank +".");
        	}catch (Exception e){
        		System.out.println("Failed to rate profile.");
        		if(App.DEV_MODE)
        		    e.printStackTrace();
        	}
        }
    }

}
