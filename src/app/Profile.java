package app;

import java.lang.reflect.Field;

public class Profile implements ProfileInterface
{
	private String aboutMe;
	private int age = 0;
	private String genderId;
	private String sexualPref;
	private String major;
	private String spiritAnimal;
	private String zodiacSign;
	private final String[] options = {"aboutme", "age", "genderid", "sexualpref", "major", "spiritanimal", "zodiacsign", "done"};

	@Override
	public void createProfile()
	{

		System.out.println("Time to create your profile!");

		boolean editProf = true;
		while(editProf){
			editProfileFields();

			boolean confirm = InputReader.requestConfirmation(this);
			if(confirm){
				//TODO push changes to database
				System.out.println("Profile confirmed.");
				editProf = false;
			}
		}
		System.out.println("Profile creation complete.");
	}


	/**
	 * A series of prompts to guide user through editing their profile
	 */
	public void editProfileFields(){
		boolean edit = true;
		while(edit) {
			String option = InputReader.readFromOptions("Which field would you like to edit?", options);

			switch(option) {
				case "done":
					edit = false;
					break;
				case "aboutMe":
					editAboutMe();
					break;
				case "age":
					editAge();
					break;
				case "genderId":
					editGenderId();
					break;
				case "sexualPref":
					editSexPref();
					break;
				case "major":
					editMajor();
					break;
				case "spiritAnimal":
					editSpiritAnimal();
					break;
				case "zodiacSign":
					editZodiacSign();
					break;
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their zodiac sign
	 */
	private void editZodiacSign(){
		System.out.print("Your current zodiac sign is:\t");
		System.out.println(sexualPref);

		String input = (InputReader.collectInput("Please enter a new zodiac sign:"));

		boolean  confirm = InputReader.requestConfirmation(input);
		if(confirm){
			setZodiac(input);
		}else{
			boolean cancel = InputReader.requestCancel();
			if(cancel){
				return;
			}else{
				editZodiacSign();
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their spirit animal
	 */
	private void editSpiritAnimal(){
		System.out.print("Your current spirit animal is:\t");
		System.out.println(sexualPref);

		String input = (InputReader.collectInput("Please enter a new spirit animal:"));

		boolean  confirm = InputReader.requestConfirmation(input);
		if(confirm){
			setSpiritAnimal(input);
		}else{
			boolean cancel = InputReader.requestCancel();
			if(cancel){
				return;
			}else{
				editSpiritAnimal();
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their major
	 */
	private void editMajor(){
		System.out.print("Your current major is:\t");
		System.out.println(sexualPref);

		String input = (InputReader.collectInput("Please enter a new major:"));

		boolean  confirm = InputReader.requestConfirmation(input);
		if(confirm){
			setMajor(input);
		}else{
			boolean cancel = InputReader.requestCancel();
			if(cancel){
				return;
			}else{
				editMajor();
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their sexual preference
	 */
	private void editSexPref(){
		System.out.print("Your current sexual preference is:\t");
		System.out.println(sexualPref);
		String input = (InputReader.collectInput("Please enter a new sexual preference:"));

		boolean  confirm = InputReader.requestConfirmation(input);
		if(confirm){
			setSexualPref(input);
		}else{
			boolean cancel = InputReader.requestCancel();
			if(cancel){
				return;
			}else{
				editSexPref();
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their about me section
	 */
	private void editAboutMe() {
		System.out.println("Your current 'About Me' section is:");
		System.out.println(aboutMe);
		String input = (InputReader.collectInput("Please describe yourself."));

		boolean confirm = InputReader.requestConfirmation(input);
		if(confirm) {
			setAboutMe(input);
		}
		else {
			boolean cancel = InputReader.requestCancel();
			if(cancel) {
				return;
			}
			else {
				editAboutMe();
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their gender identity
	 */
	private void editGenderId(){
		System.out.print("Your current gender identity is:\t");
		System.out.println(genderId);
		String input = (InputReader.collectInput("Please enter a new gender identity:"));

		boolean confirm = InputReader.requestConfirmation(input);
		if(confirm) {
			setGenderId(input);
		}
		else {
			boolean cancel = InputReader.requestCancel();
			if(cancel) {
				return;
			}
			else {
				editGenderId();
			}
		}
	}

	/**
	 * A series of prompts to guide user through editing their age
	 */
	private void editAge() {
		System.out.print("Your current age is:\t");
		System.out.println(genderId);
		int input = (InputReader.readInputInt("Please enter a age:"));

		boolean confirm = InputReader.requestConfirmation(input);
		if(confirm) {
			setAge(input);
		}
		else {
			boolean cancel = InputReader.requestCancel();
			if(cancel) {
				return;
			}
			else {
				editAge();
			}
		}
	}




	@Override
	public void removeProfile(Account myAccount)
	{
		// myAccount.setProfile(null);
	}


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
