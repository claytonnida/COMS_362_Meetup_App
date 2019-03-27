package app;

import app.models.Account;

public class AccountController {

	/**
	 * Checks the database to see whether an account with the given username already exists
	 *
	 * @param username
	 * 		The username to check the database for
	 *
	 * @return True, if the username does not already exist in the server. Otherwise, false.
	 */
	private static boolean isUsernameUnique(String username) {
		//TODO: Check the database for the username once it is implemented in future iteration

		return true;
	}

	/**
	 * Checks whether the two given password Strings match.
	 *
	 * @param password
	 * 		The password String to confirm
	 * @param comparisonPassword
	 * 		The String used to confirm the password
	 *
	 * @return True, if the two Strings match. Otherwise, false.
	 */
	private static boolean doPasswordsMatch(String password, String comparisonPassword) {

		// Passwords need to match exactly, so we don't normalize the Strings

		if(password == null) {
			throw new IllegalArgumentException("The password argument cannot be null");
		}
		if(password.isEmpty()) {
			throw new IllegalArgumentException("The password argument cannot be null");
		}

		if(comparisonPassword == null) {
			throw new IllegalArgumentException("The comparisonPassword argument cannot be null");
		}
		if(comparisonPassword.isEmpty()) {
			throw new IllegalArgumentException("The comparisonPassword argument cannot be null");
		}

		return password.equals(comparisonPassword);
	}

	/**
	 * Creates an {@link Account} instance and returns it.
	 *
	 * @param username
	 * 		The desired username to claim for the account
	 * @param password
	 * 		The String to be set as the @{link Account}'s password
	 * @param comparisonPassword
	 * 		The String used to confirm the password String
	 *
	 * @return The created @{link Account}
	 */
	public static Account createAccount(String username, String password, String comparisonPassword) {

		if(username == null) {
			throw new IllegalArgumentException("ERROR: Username cannot be null");
		}
		if(username.trim().isEmpty()) {
			throw new IllegalArgumentException("ERROR: Username cannot be null or only whitespace.");
		}

		if(password == null || comparisonPassword == null) {
			throw new IllegalArgumentException("ERROR: Password cannot be null");
		}
		if(password.trim().isEmpty() || comparisonPassword.trim().isEmpty()) {
			throw new IllegalArgumentException("ERROR: Password cannot be null or only whitespace.");
		}

		if(!isUsernameUnique(username)) {
			throw new IllegalArgumentException("ERROR: That username is taken Please enter a different username.");
		}

		if(!doPasswordsMatch(password, comparisonPassword)) {
			throw new IllegalArgumentException("ERROR: Passwords do not match.");
		}

		return new Account(username, password);
	}

	/**
	 * Adds the given {@link Account} to the database with the {@link String} used as it's primary key.
	 *
	 * @param username
	 * 		The username String that will be the @{link Account}'s primary key in the database
	 * @param account
	 * 		The {@link Account} to be added to the database.
	 */
	public static void addAccount(String username, Account account) {
		// TODO: Add given account to the database once it is implemented in a future iteration
	}
}
