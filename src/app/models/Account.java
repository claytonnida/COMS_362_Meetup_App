package app.models;

import app.interfaces.AccountInterface;

public class Account implements AccountInterface
{
	private String username;
	private String password;
	private String email;
	private Profile myProfile;
	private int profileid;
	private int id;

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static Account getOfflineProfile(){
		Account account = new Account();
		account.setId(-1);
		account.setProfileid(-1);
		account.setUsername("Dev");
		account.setPassword("dev");

		Profile p = new Profile();
		p.setId(-1);
		p.setMajor("SE");
		p.setName("Cool Guy Dev");
		p.setAboutMe("Clayton turned the server off again...");
		account.setProfile(p);
		return account;
	}

	//Required for The ReflectMapper
	public Account(){}

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
	public void setProfileid(int id) {
		profileid = id;
	}

	@Override
	public void setProfile(Profile newProfile) {
		myProfile = newProfile;
	}

	@Override
	public int getProfileid(){
		return profileid;
	}

	@Override
	public void removeProfile() {
		setProfile(null);}

	@Override
	public String toString(){
		String str = String.format("Username: %s\nProfile: \n\t%s",getUsername(),
				((getProfile()==null)?
						"null":
						getProfile().toString().replaceAll("\n","\n\t")));
		return str;
	}
}
