package app.models;

import app.interfaces.ProfileInterface;

import java.awt.image.BufferedImage;


public class Profile implements ProfileInterface
{
	private int id;
	private String aboutMe;
	private int age = 0;
	private String genderId;
	private String sexualPref;
	private String major;
	private String spiritAnimal;
	private String zodiac;
	private String name = "Anonymous";
	private int appearOffline;
	private BufferedImage picture;

	//If you change these values, you will also need to change ProfileController.editProfileFields(...)
	public static final String[] OPTIONS = {"About Me", "Age", "Gender Identity",
            "Sexual Preference", "Major", "Spirit Animal", "Zodiac Sign", "Picture", "done"};


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGenderId() {
		return genderId;
	}

	public void setGenderId(String genderId) {
		this.genderId = genderId;
	}

	public String getSexualPref() {
		return sexualPref;
	}

	public void setSexualPref(String sexualPref) {
		this.sexualPref = sexualPref;
	}

	public BufferedImage getPicture() { return picture; }

	public void setPicture(BufferedImage picture) { this.picture = picture; }

	@Override
	public String getMajor() {
		return major;
	}

	@Override
	public void setMajor(String major) {
		this.major = major;
	}

	public String getSpiritAnimal() {
		return spiritAnimal;
	}

	public void setSpiritAnimal(String spiritAnimal) {
		this.spiritAnimal = spiritAnimal;
	}

	@Override
	public String getZodiac() {
		return zodiac;
	}

	@Override
	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAppearOffline() {
		return appearOffline;
	}

	public void setAppearOffline(int appearOffline) {
		this.appearOffline = appearOffline;
	}

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
	@Override
	public String toString(){
		return getProfileDetails();
	}
}
