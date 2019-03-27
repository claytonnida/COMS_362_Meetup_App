package app.models;

import app.interfaces.ProfileInterface;



public class Profile implements ProfileInterface
{
	private String profileid;
	private String aboutme;
	private int age = 0;
	private String genderid;
	private String sexualpref;
	private String major;
	private String spiritanimal;
	private String zodiac;

	//If you change these values, you will also need to change ProfileController.editProfileFields(...)
	public static final String[] OPTIONS = {"About Me", "Age", "Gender Identity",
            "Sexual Preference", "Major", "Spirit Animal", "Zodiac Sign", "done"};





	public void setProfileid(String profileid){
		this.profileid = profileid;
	}

	public String getProfileid(){
		return profileid;
	}

	@Override
	public void setAboutme(String input)
	{
		aboutme = input;
	}

	@Override
	public String getAboutme()
	{
		return aboutme;
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
	public void setSexualpref(String input)
	{
		sexualpref = input;
	}

	@Override
	public String getSexualpref()
	{
		return sexualpref;
	}

	@Override
	public void setGenderid(String input)
	{
		genderid = input;
	}

	@Override
	public String getGenderid()
	{
		return genderid;
	}

	@Override
	public void setSpiritanimal(String input)
	{
		spiritanimal = input;
	}

	@Override
	public String getSpiritanimal()
	{
		return spiritanimal;
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
		zodiac = input;
	}

	@Override
	public String getZodiac()
	{
		return zodiac;
	}

	@Override
	public String getProfileDetails(){
		String profileDetails = "About Me: " + getAboutme() +
				"\nAge: " + getAge() +
				"\nGender Identity: " + getGenderid() +
				"\nSexual Preference: " + getSexualpref() +
				"\nMajor: " + getMajor() +
				"\nSpirit Animal: " + getSpiritanimal() +
				"\nZodiac Sign: " + getZodiac();

		return profileDetails;
	}
	@Override
	public String toString(){
		return getProfileDetails();
	}
}
