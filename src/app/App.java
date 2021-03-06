package app;


import app.Controllers.AccountController;
import app.Controllers.GroupController;
import app.Controllers.ProfileController;
import app.MySQL.MySQLHelper;
import app.models.Account;
import app.models.Profile;
import app.models.mappers.ProfileMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App
{
	public static final boolean DEV_MODE = true;

	public static Map<String,Object> sessionVariables = new HashMap<>();

	public static void main(String[] args) {

		System.out.println("Welcome!");

		startup();

		runApp();
	}

	private static void startup(){
		Account myAccount = null;

		AccountController accountController = new AccountController();

		String input = InputReader.readFromOptions("Welcome!", new String[]{"Login", "Sign Up", "Exit"});

		switch (input) {
			case "Sign Up":
				while (true) {
					System.out.println();
					String username = InputReader.collectInput("Please enter a username for the account.");

					String password = InputReader.collectInput("Please enter a password for the account.");

					String comparisonPassword = InputReader.collectInput("Enter your password again to confirm.");
					try {
						myAccount = accountController.createAccount(username, password, comparisonPassword);
						myAccount.setProfile(new Profile());

						try {
							accountController.addAccount(myAccount);
						}catch (Exception e){
							System.out.println(e.getMessage());
						}

						MySQLHelper.executeUpdate("update meetup.profile set isOnline = 1 where id = " + myAccount.getProfileid());

						sessionVariables.put("account", myAccount);
						break;
					} catch (IllegalArgumentException e) {
						System.out.println("Password creation failed! " + e);
						System.out.println("Restarting Account creation");
					}
				}
				break;
			case "Login":
				boolean keepTrying = true;
				while(keepTrying && myAccount == null) {
					String username = InputReader.collectInput("Please enter your username.");
					String password = InputReader.collectInput("Please enter your password.");

					if(username.equals("dev")&&password.equalsIgnoreCase("dev")){
						myAccount = Account.getOfflineProfile();
						sessionVariables.put("account", myAccount);
					}else {
						try {
							Account acc = accountController.fetchAccount(username, password);

							if (acc == null) {
								throw new Exception();
							}
							myAccount = acc;
							sessionVariables.put("account", myAccount);

							MySQLHelper.executeUpdate("update meetup.profile set isOnline = 1 where id = " + myAccount.getProfileid());

							System.out.println("Successfully loaded everything");
						} catch (Exception e) {
							if(App.DEV_MODE)
								e.printStackTrace();
							System.out.println("Couldn't fetch profile");
							keepTrying = InputReader.inputYesNo("Try Again?");
						}
					}
				}
				if(myAccount == null)
					startup();
				break;
			case "Exit":
				exitApp();
				break;
		}
	}

	private static void exitApp() {
		System.out.println("Okay then. Have a good day!");

		if(sessionVariables.containsKey("account"))
		MySQLHelper.executeUpdate("update meetup.profile set isOnline = 0 where id = " +
				((Account) sessionVariables.get("account")).getProfileid());

		InputReader.closeInputReader();
		System.exit(0);
	}

	/**
	 * Leads user through prompts to use the app
	 */
	private static void runApp(){
		boolean play = true;
		ProfileController pc = new ProfileController();
		while(play){
			switch (InputReader.readFromOptions("What would you like to do?",new String[]
					{"Edit My Profile","Edit Online Status","View Invites",
							"Browse Profiles", "Filter Profiles", "Manage Groups","Exit"})){
				case "Edit My Profile":
					new ProfileController().editProfileFields(((Account)sessionVariables.get("account")).getProfile());
					break;
				case "Manage Groups":
					GroupController gc = new GroupController();
					gc.manageGroups(((Account)sessionVariables.get("account")));
					break;
				case "Edit Online Status":
					pc.editOnlineStatus(((Account)sessionVariables.get("account")).getProfile());
					break;
				case "View Invites":
					new ProfileController().viewInvitations(((Account)sessionVariables.get("account")).getProfile().getId());
					break;
				case "Browse Profiles":
					pc.browseProfiles(((Account)sessionVariables.get("account")));
                    break;
				case "Filter Profiles":
					String filters[] = {"name", "aboutMe", "Age", "genderId", "sexualPref", "major", "spiritAnimal", "zodiac", "interests"};
					String choice = pc.chooseFilter(filters);
					String selection = null;
					selection = InputReader.collectInput("What would you like to filter?");
					ProfileMapper pm = new ProfileMapper();
					try {
						ArrayList<Profile> profileList = (ArrayList<Profile>) pm.createObjectList(
								"Select * from meetup.profile where " + choice + " like '%" + selection + "%' and id != "
										+((Account)sessionVariables.get("account")).getProfile().getId());
						Profile p = pc.selectProfile(profileList,((Account)sessionVariables.get("account")));
						System.out.println(p);
					}
					catch(SQLException e){
						e.printStackTrace();
					}
					break;
				case "Exit":
					if(InputReader.inputYesNo("Are you sure you want to quit?")) {
						exitApp();
					}
					break;
			}
		}
	}
}
