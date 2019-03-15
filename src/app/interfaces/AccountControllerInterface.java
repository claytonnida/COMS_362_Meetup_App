package app.interfaces;

import app.Account;

public interface AccountControllerInterface {

	/**
	 * Checks the database to see whether an account with the given username already exists
	 *
	 * @param username
	 * 		The username to check the database for
	 *
	 * @return True, if the username does not already exist in the server. Otherwise, false.
	 */
	boolean isUsernameUnique(String username);

	/**
	 * Checks whether the two given password Strings match.
	 *
	 * @param pwd1
	 * 		The password String to confirm
	 * @param pwd2
	 * 		The String used to confirm the password
	 *
	 * @return True, if the two Strings match. Otherwise, false.
	 */
	boolean doPasswordsMatch(String pwd1, String pwd2);

	/**
	 * Creates an {@link Account} instance and returns it.
	 *
	 * @param username
	 * 		The desired username to claim for the account
	 * @param pw1
	 * 		The String to be set as the @{link Account}'s password
	 * @param pw2
	 * 		The String used to confirm the password String
	 *
	 * @return The created @{link Account}
	 */
	Account createAccount(String username, String pw1, String pw2);

	/**
	 * Adds the given {@link Account} to the database with the {@link String} used as it's primary key.
	 *
	 * @param username
	 * 		The username String that will be the @{link Account}'s primary key in the database
	 * @param account
	 * 		The {@link Account} to be added to the database.
	 */
	void addAccount(String username, Account account);

}
