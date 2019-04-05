package app;


import app.Controllers.AccountController;
import app.Controllers.GroupController;
import app.Controllers.ProfileController;
import app.MySQL.MySQLHelper;
import app.models.Account;
import app.models.Profile;
import app.models.mappers.ProfileMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App
{
	public static final boolean DEV_MODE = true;
	//TODO create account here or in Account.java
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


						//TODO update to database
						try {
							accountController.addAccount(myAccount);
						}catch (Exception e){
							System.out.println(e.getMessage());
						}

						// TODO: Remove once server is implemented.
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

							// TODO: Remove once server is implemented.
							MySQLHelper.executeUpdate("update meetup.profile set isOnline = 1 where id = " + myAccount.getProfileid());

							System.out.println("Successfully loaded everything");
						} catch (Exception e) {
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
		System.out.println("No? Okay then. Have a good day!");

		// TODO: Remove once server is implemented.
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
					{"Edit My Profile","Edit Online Status",
							"Browse Profiles","Manage Groups","Exit"})){
				case "Edit My Profile":
					new ProfileController().editProfileFields(((Account)sessionVariables.get("account")).getProfile());
					break;
				case "Manage Groups":
					//TODO this
					GroupController gc = new GroupController();
					gc.manageGroups(((Account)sessionVariables.get("account")));
					break;
				case "Edit Online Status":
					pc.editOnlineStatus(((Account)sessionVariables.get("account")).getProfile());
					break;
				case "Browse Profiles":
					//TODO implement fully and elsewhere
					try {
						ProfileMapper pm = new ProfileMapper();
						List<Profile> profileList = pm.createObjectList("Select * from meetup.profile where id != " +
								((Account) sessionVariables.get("account")).getProfile().getId());
						Profile p = pc.selectProfile(profileList,((Account)sessionVariables.get("account")));
					}catch (Exception e){
						System.out.println("Can't browse files at this time.");
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
