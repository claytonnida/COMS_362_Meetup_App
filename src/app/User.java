// TODO: Ew
//package app;
//
//import profiles.*;
//import java.util.*;
//
//public class User {
//	
//	public Profile user_profile;
//	String user_email;
//	private String user_password;
//	private String username;
//	
//
//	//Creating a new User
//	public User(){
//		System.out.println("Please enter your email address");
//		Scanner input = new Scanner(System.in);
//		String s = input.next();
//		//if email is a valid email
//		this.user_email = s;
//		System.out.println("Email accepted!\n");
//		boolean password_complete = false;
//		while(!password_complete){
//			System.out.println("Please enter your new password");
//			String pass = input.next();
//			System.out.println("Please confirm your new password");
//			String confirm = input.next();
//			if(pass.equals(confirm)){
//				this.user_password = pass;
//				password_complete = true;
//				System.out.println("Your password has been set!\n");
//			}
//			else{
//				System.out.println("Your passwords did not match, please try again\n");
//			}
//		}
//		boolean user_name_selected = false;
//		while(!user_name_selected){
//		System.out.println("Please enter your desired username");
//		String user_name = input.next();
//		/*
//		 * if username is not taken ->
//		 * complete creation
//		 * else ->
//		 * username is taken error
//		 */
//		System.out.println("You entered " + user_name + " as your user name. Are you sure you want this to be your username? (y/n)");
//		String response = input.next();
//			if(response.equals("y")){
//				this.username = user_name;
//				System.out.println("Username set successfully!\n");
//				user_name_selected = true;
//				}
//			else{
//			
//			}
//		}
//		System.out.println("Would you like to create your profile now? (y/n)");
//		String response = input.next();
//		if(response.equals("y")){
//			System.out.println("Okay, let's get started!\n");
//			this.user_profile = this.create_profile();
//		}
//		else{
//			System.out.println("Okay, don't worry, you can always create your profile later.\n");
//		}
//	}
//	
//	//Re-Creating an existing user
//	public User(String username, String user_password){
//		/*
//		 * if user is valid ->
//		 * retrieve user data from database
//		 * else ->
//		 * send incorrect login error
//		 */
//		
//	}
//	
//	public Profile create_profile(){
//		Profile p = new Profile();
//		System.out.println("Would you like to enter your name? (y/n)");
//		Scanner input = new Scanner(System.in);
//		String response;
//		response = input.next();
//		if(response.equals("y")){
//			p.set_name();
//		}
//		System.out.println("Would you like to select your profile picture from pictures uploaded to the project? (y/n)");
//		response = input.next();
//		if(response.equals("y")){
//			p.set_profile_picture();
//		}
//		/*
//		 * Continue this process for all other aspects of the profile
//		 */
//		this.user_profile = p;
//		System.out.println("Congratulations! Your profile has been created successfully!");
//		return p;
//	}
//	
//	void delete_profile(){
//		/*
//		 * Set each element of the user's profile to null
//		 * then set the user_profile to null
//		 * By doing both we should be able to avoid leaving personal data in the system
//		 */
//		this.user_profile.remove_name();
//		this.user_profile.remove_profile_picture();
//		this.user_profile = null;
//		System.out.println("Profile successfully deleted");
//	}
//
//	
//	
//	public static void main(String[] args){
//		//Test
//		User me = new User();
//		me.delete_profile();
//	}
//	
//}
