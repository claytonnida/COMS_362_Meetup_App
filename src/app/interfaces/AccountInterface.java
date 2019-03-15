package app.interfaces;

import app.Profile;

public interface AccountInterface
{
	void setUsername(String input);

	String getUsername();

	void setPassword(String input);

	String getPassword();

	void setEmail(String input);

	String getEmail();
	
	Profile getProfile();
	
	void setProfile(Profile newProfile);
}
