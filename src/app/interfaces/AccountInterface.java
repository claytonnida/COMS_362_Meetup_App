package app.interfaces;

import app.models.Profile;

public interface AccountInterface
{
	void setUsername(String input);

	String getUsername();

	void setPassword(String input);

	String getPassword();

	void setEmail(String input);

	String getEmail();

	int getProfileid();

	Profile getProfile();

	void setProfileid(int id);

	void setProfile(Profile newProfile);

	void removeProfile();
}
