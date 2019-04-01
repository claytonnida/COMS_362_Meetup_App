package app.models;

import app.interfaces.ProfileInterface;

import java.awt.image.BufferedImage;


public class Profile implements ProfileInterface
{
	private int id;
	private int age = 0;
	private int appearOffline;

	private String aboutMe;
	private String genderId;
	private String sexualPref;
	private String major;
	private String spiritAnimal;
	private String zodiac;
	private String name = "Anonymous";
	private BufferedImage picture;

	//If you change these values, you will also need to change ProfileController.editProfileFields(...)
	public static final String[] OPTIONS = {"Name", "About Me", "Age", "Gender Identity",
            "Sexual Preference", "Major", "Spirit Animal", "Zodiac Sign", "Picture", "done"};


	// TODO: Javadoc
	@Override
	public int getId() {
		return id;
	}

	// TODO: Javadoc
	@Override
	public void setId(int id) {
		this.id = id;
	}

	// TODO: Javadoc
	@Override
	public String getAboutMe() {
		return aboutMe;
	}

	// TODO: Javadoc
	@Override
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	// TODO: Javadoc
	@Override
	public int getAge() {
		return age;
	}

	// TODO: Javadoc
	@Override
	public void setAge(int age) {
		this.age = age;
	}

	// TODO: Javadoc
	@Override
	public String getGenderId() {
		return genderId;
	}

	// TODO: Javadoc
	@Override
	public void setGenderId(String genderId) {
		this.genderId = genderId;
	}

	// TODO: Javadoc
	@Override
	public String getSexualPref() {
		return sexualPref;
	}

	// TODO: Javadoc
	@Override
	public void setSexualPref(String sexualPref) {
		this.sexualPref = sexualPref;
	}

	// TODO: Javadoc
	public BufferedImage getPicture() { return picture; }

	public void setPicture(BufferedImage picture) { this.picture = picture; }

	// TODO: Javadoc
	@Override
	public String getMajor() {
		return major;
	}

	// TODO: Javadoc
	@Override
	public void setMajor(String major) {
		this.major = major;
	}

	// TODO: Javadoc
	@Override
	public String getSpiritAnimal() {
		return spiritAnimal;
	}

	// TODO: Javadoc
	@Override
	public void setSpiritAnimal(String spiritAnimal) {
		this.spiritAnimal = spiritAnimal;
	}

	// TODO: Javadoc
	@Override
	public String getZodiac() {
		return zodiac;
	}

	// TODO: Javadoc
	@Override
	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	// TODO: Javadoc
	@Override
	public String getName() {
		return name;
	}

	// TODO: Javadoc
	@Override
	public void setName(String name) {
		this.name = name;
	}

	// TODO: Javadoc
	@Override
	public int getAppearOffline() {
		return appearOffline;
	}

	// TODO: Javadoc
	@Override
	public void setAppearOffline(int appearOffline) {
		this.appearOffline = appearOffline;
	}

	// TODO: Javadoc
	@Override
	public String getProfileDetails(){
		String profileDetails = "Name: " + getName() +
				"\nAbout Me: " + getAboutMe() +
				"\nAge: " + getAge() +
				"\nGender Identity: " + getGenderId() +
				"\nSexual Preference: " + getSexualPref() +
				"\nMajor: " + getMajor() +
				"\nSpirit Animal: " + getSpiritAnimal() +
				"\nZodiac Sign: " + getZodiac();

		return profileDetails;
	}

	// TODO: Javadoc
	@Override
	public String toString(){
		return getProfileDetails();
	}
}
