package app.Controllers;

import app.InputReader;
import app.MySQL.MySQLHelper;
import app.interfaces.ProfileControllerInterface;
import app.models.Profile;
import app.models.mappers.ProfileMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

public class ProfileController implements ProfileControllerInterface {

    /**
     * Creates instance of a {@link Profile} and prompts users to fill out its fields.
     *
     * @return
     *      Instance of {@link Profile}.
     */
    @Override
    public Profile createProfile()
    {

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
            }
        }

        try {
            if(InputReader.requestConfirmation(p))
                saveProfile(p);
            else
                System.out.println("Profile changes discarded.");
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Couldn't save profile to server at this time.");
        }
    }


    /**
     * A series of prompts to guide user through editing their online status.
     *
     * @param p {@link Profile} to edit the online status of.
     */
    @Override
    public void editOnlineStatus(Profile p){
        System.out.print("Your are currently appearing:\t");
        System.out.println((p.getAppearOffline()==1?"Offline":"Online"));

        String input = (InputReader.readFromOptions("Select Online Status",new String[]{"Online","Appear Offline"}));


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

            ProfileMapper pm = new ProfileMapper();
            Statement stmt = MySQLHelper.createStatement();
            String query = pm.toUpdateQueryQuery(p);
            stmt.executeUpdate(query);
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
     * Lists {@link Profile}s
     */
    @Override
    public void listProfiles(){
        try{
            Statement stmt = MySQLHelper.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from meetup.profile");
            while(rs.next()){
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i = 1; i < rsmd.getColumnCount(); i++){
                    System.out.print(rs.getString(i)+",\t");
                }
                System.out.println();
            }
        }catch (Exception e){
            e.printStackTrace();
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
    public int saveProfile(Profile p)throws SQLException{
        ProfileMapper pm = new ProfileMapper();

        if(p.getId() == 0) {
            String query = pm.toInsertQueryQuery(p);
            Statement stmt = MySQLHelper.createStatement();
            stmt.executeUpdate(query);

            ResultSet rs = stmt.executeQuery("Select @@identity");
            rs.next();
            return rs.getInt(1);
        }else {
            String query = pm.toUpdateQueryQuery(p) + " where id="+p.getId();
            MySQLHelper.createStatement().executeUpdate(query);
            return p.getId();
        }
    }

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
     * @param pid The ID of the {@link Profile} to ping the server for.
     *
     * @return
     *      {@code true} if the given {@link Profile} is online. {@code false}, otherwise.
     */
    @Override
    public boolean checkOnlineStatus(int pid) {
        // TODO: Ping the server for the actual connection status

        return true;
    }

}
