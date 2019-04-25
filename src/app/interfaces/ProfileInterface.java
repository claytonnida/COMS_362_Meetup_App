package app.interfaces;

public interface ProfileInterface
{
	int getIsOnline();

	void setIsOnline(int isOnline);

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

	int getAppearOffline() ;

	void setAppearOffline(int appearOffline);

	String getProfileDetails();
}
