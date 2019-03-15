package app.interfaces;

import app.Account;

public interface ProfileInterface
{
	void createProfile();

	void removeProfile(Account myAccount);

	String getProfileDetails();

	void setAboutMe(String input);

	String getAboutMe();

	void setAge(int input);

	int getAge();

	void setSexualPref(String input);

	String getSexualPref();

	void setGenderId(String input);

	String getGenderId();

	void setSpiritAnimal(String input);

	String getSpiritAnimal();

	void setMajor(String input);

	String getMajor();

	void setZodiac(String input);

	String getZodiac();
}
