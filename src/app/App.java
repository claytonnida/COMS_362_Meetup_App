package app;


import app.Controllers.AccountController;
import app.Controllers.GroupController;
import app.Controllers.ProfileController;
import app.MySQL.MySQLHelper;
import app.models.Account;
import app.models.Profile;

import java.util.HashMap;
import java.util.Map;

public class App
{
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
		MySQLHelper.executeUpdate("update meetup.profile set isOnline = 0 where id = " +
				((Account) sessionVariables.get("account")).getProfileid());

		InputReader.closeInputReader();
		System.exit(0);
	}

	private static void runApp(){
		boolean play = true;
		while(play){
			switch (InputReader.readFromOptions("What would you like to do?",new String[]
					{"Edit My Profile","Edit Online Status","Manage Groups","Exit"})){
				case "Edit My Profile":
					new ProfileController().editProfileFields(((Account)sessionVariables.get("account")).getProfile());
					break;
				case "Manage Groups":
					//TODO this
					GroupController gc = new GroupController();
					gc.manageGroups(((Account)sessionVariables.get("account")));
					break;
				case "Edit Online Status":
					ProfileController pc = new ProfileController();
					pc.editOnlineStatus(((Account)sessionVariables.get("account")).getProfile());
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
