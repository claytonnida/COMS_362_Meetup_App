package app;

import app.interfaces.ProfileInterface;



public class Profile implements ProfileInterface
{
	private String aboutMe;
	private int age = 0;
	private String genderId;
	private String sexualPref;
	private String major;
	private String spiritAnimal;
	private String zodiacSign;

	//If you change these values, you will also need to change ProfileController.editProfileFields(...)
	public static final String[] OPTIONS = {"About Me", "Age", "Gender Identity",
            "Sexual Preference", "Major", "Spirit Animal", "Zodiac Sign", "done"};






	@Override
	public void setAboutMe(String input)
	{
		aboutMe = input;
	}

	@Override
	public String getAboutMe()
	{
		return aboutMe;
	}

	@Override
	public void setAge(int input)
	{
		age = input;
	}

	@Override
	public int getAge()
	{
		return age;
	}

	@Override
	public void setSexualPref(String input)
	{
		sexualPref = input;
	}

	@Override
	public String getSexualPref()
	{
		return sexualPref;
	}

	@Override
	public void setGenderId(String input)
	{
		genderId = input;
	}

	@Override
	public String getGenderId()
	{
		return genderId;
	}

	@Override
	public void setSpiritAnimal(String input)
	{
		spiritAnimal = input;
	}

	@Override
	public String getSpiritAnimal()
	{
		return spiritAnimal;
	}

	@Override
	public void setMajor(String input)
	{
		major = input;
	}

	@Override
	public String getMajor()
	{
		return major;
	}

	@Override
	public void setZodiac(String input)
	{
		zodiacSign = input;
	}

	@Override
	public String getZodiac()
	{
		return zodiacSign;
	}

	@Override
	public String getProfileDetails(){
		String profileDetails = "About Me: " + getAboutMe() +
				"\nAge: " + getAge() +
				"\nGender Identity: " + getGenderId() +
				"\nSexual Preference: " + getSexualPref() +
				"\nMajor: " + getMajor() +
				"\nSpirit Animal: " + getSpiritAnimal() +
				"\nZodiac Sign: " + getZodiac();

		return profileDetails;
	}
	@Override
	public String toString(){
		return getProfileDetails();
	}
}
