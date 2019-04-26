package app;

import app.interfaces.Selectable;

import java.util.List;
import java.util.Scanner;

public class InputReader {

	//static allows us to have a single input reader without opening and
	//closing it, or needing to instantiate an object before calling
	//utility methods
	private static Scanner scanner = new Scanner(System.in);

	// TODO: Javadocs
	public static Selectable readFromOptions(String prompt,List<Selectable> list){
		System.out.print(String.format("%s\n", prompt));
		String options[] = new String[list.size()+1];
		for(int i = 0; i < list.size(); i++){
			options[i] = list.get(i).getSelectionPrompt();
		}
		options[list.size()] = "Cancel";

		//List options for user
		for(int i = 0; i < options.length; i++) {
			//note that indexing starts at 1 and not 0 for option listing
			System.out.println(String.format("\t[%d]\t%s", (i + 1), options[i]));
		}

		//Collect user's decision
		System.out.print("Enter the number of your selection:\t");
		String input = scanner.nextLine();
		//sanitize input of non-digits to make easier usability
		input = input.replaceAll("\\D", "");
		int intput = 0;//haha get it?
		try {
			//subtract 1 to account for the offset of indexing options
			intput = Integer.parseInt(input.trim()) - 1;
		}
		catch(Exception e) {
			System.out.println("That was not a number.");
			return readFromOptions(prompt, list);
		}

		//evaluate user's decision
		if(options[intput].equalsIgnoreCase("Cancel")) {
			return null;
		}
		if(intput >= 0 && intput < options.length-1) {
			return list.get(intput);
		}
		else {
			System.out.println("The number you entered is out of range.");
			return readFromOptions(prompt, list);
		}
	}

	/**
	 * Displays the prompt and then takes user input
	 * @param prompt
	 * @return
	 */
	public static String collectInput(String prompt) {
		System.out.print(String.format("%s\t", prompt));
		String input = scanner.nextLine();
		return input;
	}

	/**
	 * Displays the prompt appended by string literal " [y/n]"
	 *
	 *
	 * @param prompt
	 * @return
	 *      true, if user enters string containing y
	 *      false, if user enters string containing n
	 *      true, if user enters string containing y and n
	 */
	public static boolean inputYesNo(String prompt) {
		System.out.print(String.format("%s [y/n]\t", prompt));
		String input = scanner.nextLine();

		//check input contains y or n, to allow easier usability
		if(input.toLowerCase().contains("y")) {
			return true;
		}
		if(input.toLowerCase().contains("n")) {
			return false;
		}

		System.out.println("I'm sorry. You need to enter either 'y' or 'n'.");
		return inputYesNo(prompt);
	}

	/**
	 * Displays the prompt and takes integer input.
	 * @param prompt
	 * @return
	 */
	public static int readInputInt(String prompt) {
		System.out.print(String.format("%s\t", prompt));
		String input = scanner.nextLine();

		try {
			return Integer.parseInt(input.trim());
		} catch(Exception e) {
			System.out.println("That was not a number.");
			return readInputInt(prompt);
		}
	}

	/**
	 * Displays the prompt and enumerates the provided options
	 * Returns the string value of the option selected by user.
	 * @param prompt
	 * @param options
	 * @return
	 */
	public static String readFromOptions(String prompt, String[] options) {
		System.out.print(String.format("%s\n", prompt));

		//List options for user
		for(int i = 0; i < options.length; i++) {
			//note that indexing starts at 1 and not 0 for option listing
			System.out.println(String.format("\t[%d]\t%s", (i + 1), options[i]));
		}

		//Collect user's decision
		System.out.print("Enter the number of your selection:\t");
		String input = scanner.nextLine();
		//sanitize input of non-digits to make easier usability
		input = input.replaceAll("\\D", "");
		int intput = 0;//haha get it?
		try {
			//subtract 1 to account for the offset of indexing options
			intput = Integer.parseInt(input.trim()) - 1;
		}
		catch(Exception e) {
			System.out.println("That was not a number.");
			return readFromOptions(prompt, options);
		}

		//evaluate user's decision
		if(intput >= 0 && intput < options.length) {
			return options[intput];
		}
		else {
			System.out.println("The number you entered is out of range.");
			return readFromOptions(prompt, options);
		}
	}

	/**
	 * Prints the object and asks user if that is what they want
	 * @param input
	 * @return
	 */
	public static boolean requestConfirmation(Object input){
		return InputReader.inputYesNo(String.format("You entered: \n\t%s\nConfirm?",
                input.toString().replaceAll("\n","\n\t")));
	}

	/**
	 * Asks user if they would like to cancel
	 * Returning TRUE means user wishes to cancel
	 * @return
	 */
	public static boolean requestCancel() {
		return InputReader.inputYesNo("Would you like to cancel?");
	}

	/**
	 * Closes the input reader's {@link Scanner}.
	 */
	public static void closeInputReader() {
		 scanner.close();
	 }
}
