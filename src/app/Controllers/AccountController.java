package app.Controllers;

import app.App;
import app.MySQL.MySQLHelper;
import app.interfaces.AccountControllerInterface;
import app.models.Account;
import app.models.Profile;
import app.models.mappers.AccountMapper;
import app.models.mappers.ReflectMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountController implements AccountControllerInterface {

	/**
	 * Checks the database to see whether an account with the given username already exists
	 *
	 * @param username
	 * 		The username to check the database for
	 *
	 * @return True, if the username does not already exist in the server. Otherwise, false.
	 */
	@Override
	public boolean isUsernameUnique(String username) {

		ResultSet rs = MySQLHelper.executeQuery("SELECT * FROM meetup.account WHERE username = \"" + username + "\"");

		try {
			return !rs.first();
		}
		catch(SQLException e) {
			if(App.DEV_MODE)
				e.printStackTrace();
		}

		return false;
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
	@Override
	public boolean doPasswordsMatch(String password, String comparisonPassword) {

		// Passwords need to match exactly, so we don't normalize the Strings.

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
	@Override
	public Account createAccount(String username, String password, String comparisonPassword) {

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
	 * @param account
	 * 		The {@link Account} to be added to the database.
	 */
	@Override
	public int addAccount(Account account) throws SQLException{
		int profileId = new ProfileController().saveProfile(account.getProfile());
		account.setProfileid(profileId);
		account.getProfile().setId(profileId);

		AccountMapper am = new AccountMapper();
		Statement stmt = MySQLHelper.createStatement();
		stmt.executeUpdate(am.toInsertQueryQuery(account));
		ResultSet rs = stmt.executeQuery("Select @@identity");
		rs.next();

		return rs.getInt(1);
	}

	/**
	 * @see AccountControllerInterface#fetchAccount(int)
	 */
	@Override
	public Account fetchAccount(int id)throws SQLException {
		ReflectMapper<Account> mapper = new ReflectMapper<>(Account.class);
		Account acc = mapper.toObject("Select * from meetup.account where id = "+id);

		try {
			ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
			Profile prof = pmapper.toObject("Select * from meetup.profile where profileid = " + acc.getProfileid());
			acc.setProfile(prof);
		}catch (Exception e){
			System.out.println("ERROR: Failed to Load Profile.");
			if(App.DEV_MODE)
				e.printStackTrace();
		}

		return acc;
	}

	/**
	 * @see AccountControllerInterface#fetchAccount(java.lang.String, java.lang.String)
	 */
	@Override
	public Account fetchAccount(String user, String pass) throws SQLException {
	    Account acc = null;


            ReflectMapper<Account> mapper = new ReflectMapper<>(Account.class);
            String query = String.format("Select * from meetup.account where username='%s' AND password='%s'", user, pass);
            acc = mapper.toObject(query);


            if(acc != null) {

                ReflectMapper<Profile> pmapper = new ReflectMapper<>(Profile.class);
                Profile prof = pmapper.toObject("Select * from meetup.profile where id = " + acc.getProfileid());
                acc.setProfile(prof);
            }
		return acc;
	}
}
