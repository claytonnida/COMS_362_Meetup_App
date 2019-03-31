package app.interfaces;


import java.awt.image.BufferedImage;

public interface ProfileInterface
{
	int getId() ;

	void setId(int id) ;

	String getAboutMe() ;

	void setAboutMe(String aboutMe) ;

	int getAge() ;

	void setAge(int age) ;

	String getGenderId() ;

	void setGenderId(String genderId) ;

	String getSexualPref() ;

	void setSexualPref(String sexualPref) ;

	String getMajor() ;

	void setMajor(String major) ;

	String getSpiritAnimal() ;

	void setSpiritAnimal(String spiritAnimal) ;

	String getZodiac() ;

	void setZodiac(String zodiac) ;

	String getName() ;

	void setName(String name) ;

	BufferedImage getPicture() ;

	void setPicture(BufferedImage picture) ;

	int getAppearOffline() ;

	void setAppearOffline(int appearOffline);
}
