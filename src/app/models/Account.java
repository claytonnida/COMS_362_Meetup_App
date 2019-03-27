package app.models;

import app.interfaces.AccountInterface;
import app.models.Profile;

public class Account implements AccountInterface
{
	private String username;
	private String password;
	private String email;
	private Profile myProfile;
	private int profile;
	private int id;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public int getProfileid() {
		return profile;
	}

	public void setProfileid(int profileid) {
		this.profile = profileid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void setUsername(String input) {
		username = input;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setPassword(String input) {
		password = input;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setEmail(String input) {
		email = input;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public Profile getProfile() {
		return myProfile;
	}

	@Override
	public void setProfile(Profile newProfile) {
		myProfile = newProfile;
	}

	@Override
	public void removeProfile() {setProfile(null);}

}
