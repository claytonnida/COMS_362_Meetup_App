package app;

import app.Controllers.ProfileController;

public class App
{
	//TODO create account here or in Account.java

	public static void main(String[] args) 
	{
		InputReader inputReader = new InputReader();
		Account myAccount;
		
		System.out.println("Welcome!");
		
		if(InputReader.inputYesNo("Would you like to create an account?"))
		{
			//TODO myAccount = createAccout();
		}
		else
		{
			System.out.println("No? Okay then. Have a good day!");
			inputReader.closeInputReader();
			System.exit(0);
		}

		
		if(InputReader.inputYesNo("Would you like to create your profile?"))
		{
			Profile myProfile = ProfileController.createProfile();
			//TODO uncomment below when createAccount() is done
			//myAccount.setProfile(myProfile);
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
