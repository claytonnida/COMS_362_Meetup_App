package app.models;

import app.interfaces.ProfileInterface;
import app.interfaces.Selectable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Profile implements ProfileInterface, Selectable
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
    private int isOnline;
	private String name = "Anonymous";
	private BufferedImage picture;

	private String pictureURL;
	private BufferedImage profile_pic;

	//If you change these values, you will also need to change ProfileController.editProfileFields(...)
	public static final String[] OPTIONS = {"Name", "About Me", "Age", "Gender Identity",
            "Sexual Preference", "Major", "Spirit Animal", "Zodiac Sign", "Picture", "done"};

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public Profile(){
    	Path path = Paths.get("./mystery.jpg").normalize().toAbsolutePath();
    	try {
			BufferedImage image = ImageIO.read(path.toFile());
			setProfile_pic(image);
		}catch (Exception e){
    		e.printStackTrace();
		}
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getAboutMe() {
		return aboutMe;
	}

	@Override
	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	@Override
	public int getAge() {
		return age;
	}

	@Override
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String getGenderId() {
		return genderId;
	}

	@Override
	public void setGenderId(String genderId) {
		this.genderId = genderId;
	}

	@Override
	public String getSexualPref() {
		return sexualPref;
	}

	@Override
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

	@Override
	public String getSpiritAnimal() {
		return spiritAnimal;
	}

	@Override
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getAppearOffline() {
		return appearOffline;
	}


	@Override
	public void setAppearOffline(int appearOffline) {
		this.appearOffline = appearOffline;
	}

	/**
	 * Gets the Profile's picture URL stored on the local machine
	 * @return URL for the user's current profile picture on the local
	 */
	public String getPictureURL() {
		return pictureURL;
	}

	/**
	 * Sets the Profile' picture stored on the local machine
	 * @param pictureURL URL given for storage
	 */
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

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

	public String showOnlineStatus(){
		if(getAppearOffline() == 1 || isOnline == 0){
			return "Offline";
		}else{
			return "Online";
		}
	}
	@Override
	public String toString(){
		return getProfileDetails();
	}

	@Override
	public String getSelectionPrompt() {
		return String.format("%s\t\t(%s)",getName(),showOnlineStatus());
	}

	public BufferedImage getProfile_pic() {
		return profile_pic;
	}

	/**
	 * Automatically scales the provided image to 40x40
	 * @param profile_pic
	 */
	public void setProfile_pic(BufferedImage profile_pic) {
        Image scaledImage = profile_pic.getScaledInstance(40,40,Image.SCALE_SMOOTH);

        BufferedImage bimage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(scaledImage, 0, 0, null);
        bGr.dispose();
		this.profile_pic = bimage;
	}
}
