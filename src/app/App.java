package app;


import app.Controllers.AccountController;
import app.Controllers.GroupController;
import app.Controllers.ProfileController;
import app.models.Account;
import app.models.Profile;
import app.models.mappers.AccountMapper;
import app.models.mappers.ProfileMapper;
import com.sun.org.apache.bcel.internal.classfile.PMGClass;

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

						sessionVariables.put("account", myAccount);
						break;
					} catch (IllegalArgumentException e) {
						System.out.println("Password creation failed! " + e);
						System.out.println("Restarting Account creation");
					}
				}
				break;
			case "Login":
				Account account = null;
				boolean keepTrying = true;
				while (keepTrying && account == null){
					String username = InputReader.collectInput("Please enter your username.");
					String password = InputReader.collectInput("Please enter your password.");

					if(username.equals("dev")&&password.equalsIgnoreCase("dev")){
						account = Account.getOfflineProfile();
						sessionVariables.put("account",account);
					}else {
						try {
							Account acc = accountController.fetchAccount(username, password);

							if (acc == null) {
								throw new Exception();
							}
							account = acc;
							sessionVariables.put("account", account);
							System.out.println("Successfully loaded everythinng");
						} catch (Exception e) {
							System.out.println("Couldn't fetch profile");
							keepTrying = InputReader.inputYesNo("Try Again?");
						}
					}
				}
				if(account==null)
					startup();
				break;
			case "Exit":
				System.out.println("No? Okay then. Have a good day!");
				InputReader.closeInputReader();
				System.exit(0);


		}
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
					if(InputReader.inputYesNo("Are you sure you want to quit?"))
						play = false;
					break;
			}
		}
	}
}
