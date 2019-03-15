package app;

import app.Controllers.ProfileController;

public class App
{
	//TODO create account here or in Account.java

	public static void main(String[] args) 
	{
		InputReader inputReader = new InputReader();
		Account myAccount = null;
		
		System.out.println("Welcome!");

		if(InputReader.inputYesNo("Would you like to create an account?"))
		{
			//TODO myAccount = createAccout();
		System.out.println("Welcome! Would you like to create an account? (y/n)");

		if(inputReader.inputYesNoCheck(inputReader.readInputString(), "Would you like to create an account? (y/n)")) {
			while(true) {
				System.out.println("Please enter a username for the account.");
				String username = inputReader.readInputString();

				System.out.println("Please enter a password for the account.");
				String password = inputReader.readInputString();

				System.out.println("Enter your password again to confirm.");
				String comparisonPassword = inputReader.readInputString();
				try {
					myAccount = AccountController.createAccount(username, password, comparisonPassword);
					break;
				}
				catch(IllegalArgumentException e) {
					System.out.println("Password creation failed! " + e);
					System.out.println("Restarting Account creation");
				}
			}
		}
		else {
			System.out.println("No? Okay then. Have a good day!");
			inputReader.closeInputReader();
			System.exit(0);
		}

		
		if(InputReader.inputYesNo("Would you like to create your profile?"))
		{
			Profile myProfile = new Profile();
			myProfile.createProfile();
			myAccount.setProfile(myProfile);
		}
		else
		{
			System.out.println("No? That's okay! You can always create it later.");
		}
		inputReader.closeInputReader();
	}

	private static void shutdownApp(){
		InputReader.closeInputReader();
		System.exit(0);
	}
}
