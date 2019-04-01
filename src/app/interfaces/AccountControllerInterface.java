package app.interfaces;


import app.models.Account;

import java.sql.SQLException;

public interface AccountControllerInterface {

	/**
	 * Checks the database to see whether an account with the given username already exists.
	 *
	 * @param username
	 * 		The username to check the database for.
	 *
	 * @return True, if the username does not already exist in the server. Otherwise, false.
	 */
	boolean isUsernameUnique(String username);

	/**
	 * Checks whether the two given password Strings match.
	 *
	 * @param password
	 * 		The password String to confirm.
	 * @param comparisonPassword
	 * 		The String used to confirm the password.
	 *
	 * @return True, if the two Strings match. Otherwise, false.
	 */
	boolean doPasswordsMatch(String password, String comparisonPassword);

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
	Account createAccount(String username, String password, String comparisonPassword);

	/**
	 * Adds the given {@link Account} to the database with the {@link String} used as it's primary key.
	 *
	 * @param account
	 * 		The {@link Account} to be added to the database.
	 */
	int addAccount(Account account) throws SQLException;

	/**
	 * Selects the Account from database and attempts to fill the Profile field
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	Account fetchAccount(int id) throws SQLException;

	// TODO: Add javadoc
	Account fetchAccount(String user, String pass) throws SQLException;

}
