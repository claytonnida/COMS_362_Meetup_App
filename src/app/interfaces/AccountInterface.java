package app.interfaces;

import app.models.Profile;

public interface AccountInterface
{
	// TODO: Javadocs
	void setUsername(String input);

	// TODO: Javadocs
	String getUsername();

	// TODO: Javadocs
	void setPassword(String input);

	// TODO: Javadocs
	String getPassword();

	// TODO: Javadocs
	void setEmail(String input);

	// TODO: Javadocs
	String getEmail();

	// TODO: Javadocs
	int getProfileid();

	// TODO: Javadocs
	Profile getProfile();

	// TODO: Javadocs
	void setProfileid(int id);

	// TODO: Javadocs
	void setProfile(Profile newProfile);

	// TODO: Javadocs
	void removeProfile();
}
