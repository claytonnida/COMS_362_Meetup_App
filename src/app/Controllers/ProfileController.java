package app.Controllers;

import app.InputReader;
import app.MySQL.MySQLHelper;
import app.models.Profile;
import app.models.mappers.ProfileMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileController {

    public static Profile createProfile()
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
     * A series of prompts to guide user through editing their profile
     */
    public static void editProfileFields(Profile p){
        boolean edit = true;
        while(edit) {
            String option = InputReader.readFromOptions("Which field would you like to edit?", Profile.OPTIONS);

            switch(option) {
                case "done":
                    edit = false;
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
                case "Picture":
                    setPicture(p);
                    break;
            }
        }
    }

    /**
     * A series of prompts to guide the user through creating their Profile picture
     */
    public static void setPicture(Profile p) {
        String input = InputReader.collectInput("Is the picture you wish to use saved in the file structure? [y/n]");
        boolean upload_complete = false;
        if (input.equals("y")) {
            input = InputReader.collectInput("Good! Now please enter the name of your picture file. (include .jpg, .png, etc.)");
            try {
                ///////

                BufferedImage input_picture = ImageIO.read(new File(input));
                boolean confirm = InputReader.requestConfirmation(input);
                if (confirm) {
                    p.setPicture(input_picture);
                    /*
                     * This section will need to be changed once the server is set up to something more like:
                     * try {
                     * 		URL url = new URL(getCodeBase(), "pictures/sobble_is_best.png");
                     * 		img = ImageIO.read(url);
                     * }catch(IOException e){
                     * 		System.out.println("The picture you specified seems to be unavailable, please try again");
                     * }
                     */
                    ///////
                    System.out.println("Your profile picture was added successfully! The following picture will be displayed.");
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    frame.getContentPane().setLayout(new GridLayout(1, 1));
                    ImagePanel panel = new ImagePanel(p.getPicture());
                    frame.getContentPane().add(panel);
                    frame.pack();
                    frame.setVisible(true);
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
            System.out.println("No worries! Just follow these simple steps to upload your picture to the file structure:");
            System.out.println("1- Open up your File Explorer to the location of your picture");
            System.out.println("2- Make sure you have this project open in your IDE");
            System.out.println("3- Simply drag your image over the 'COMS_362_Meetup_App' and drop it");
            System.out.println("4- Confirm the addition of the file to the project and then rerun the application. You should now be able to access your picture!");
        }
    }


    /**
     * A series of prompts to guide user through editing their zodiac sign
     */
    private static void editZodiacSign(Profile p){
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
     * A series of prompts to guide user through editing their spirit animal
     */
    private static void editSpiritAnimal(Profile p){
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
     * A series of prompts to guide user through editing their major
     */
    private static void editMajor(Profile p){
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
     * A series of prompts to guide user through editing their sexual preference
     */
    private static void editSexPref(Profile p){
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
     * A series of prompts to guide user through editing their about me section
     */
    private static void editAboutMe(Profile p) {
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
     * A series of prompts to guide user through editing their gender identity
     */
    private static void editGenderId(Profile p){
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
     * A series of prompts to guide user through editing their age
     */
    private static void editAge(Profile p) {
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
     * Update the profile as a row in the database
     * @param p
     * @throws SQLException
     */
    public static void updateProfile(Profile p)throws SQLException{

            ProfileMapper pm = new ProfileMapper();
            Statement stmt = MySQLHelper.createStatement();
            String query = pm.toUpdateQueryQuery(p);
            stmt.executeUpdate(query);
    }

    /**
     * Insert the profile as a row to the database
     * @param p
     * @throws SQLException
     */
    public static void insertProfile(Profile p )throws SQLException{
        ProfileMapper pm = new ProfileMapper();
        Statement stmt = MySQLHelper.createStatement();
        String query = pm.toInsertQueryQuery(p);
        stmt.executeUpdate(query);

    }

    public static void listProfiles(){
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
     * This is a helper class for the setPicture method
     * It creates a useable/displayable Panel to display the Profile picture
     */
    static class ImagePanel extends JPanel
    {
        private final BufferedImage image;

        ImagePanel(BufferedImage image)
        {
            this.image = image;
        }

        @Override
        public Dimension getPreferredSize()
        {
            if (super.isPreferredSizeSet())
            {
                return super.getPreferredSize();
            }
            return new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
        }
    }

}
