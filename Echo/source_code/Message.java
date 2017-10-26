import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Message {

	String fullMessage;
	String strFinalMessage;
	char[] finalMessage;
	int[][] convertedMessage;
	char[] cipherMessage;

	@SuppressWarnings("resource")
	public Message(String inFile, Key key, boolean bugDivideByZero, boolean bugNoMessage, int pc) throws BugException, FileNotFoundException {
		// set up two local string variables for use in this method
		String scanMessage = "";
		String result = "";

		// Open the file for reading save full message
		Scanner scanner = new Scanner(new File(inFile));
		while (scanner.hasNextLine()) {
			scanMessage += scanner.nextLine();
		}

		// take out everything but letters and set to lowercase characters
		// result = scanMessage.replaceAll("[^\\p{Alpha}]+", "").replaceAll("\\s+", "+").toLowerCase();
		result = scanMessage.replaceAll("[^\\p{Alpha}]+", "").replaceAll("\\s+", "+").toLowerCase();

		this.strFinalMessage = result;
		if(result.length()==0 && bugNoMessage){
			throw new BugException("No message to be encrypted!");
		}

		try {
			int pad = result.length() % key.size;

			// find out if message needs padding or not
			// if it does then we need to instanciate a larger version than just
			// the
			// result.length
			if (pad != 0) {
				this.finalMessage = new char[result.length() + pad];
				// keep adding padding until its no longer needed
				while (result.length() % key.size != 0) {
					result += 'x';
				}
			} else {
				this.finalMessage = new char[result.length()];
			}
			// set our class variable strFinal with this new finished string
			this.strFinalMessage = result;

			// turn final string into char array
			this.finalMessage = result.toCharArray();
			// create converted array full of index representation of letters
			convertMessage();

			scanner.close();
		} catch (ArithmeticException s) {
			if(bugDivideByZero) throw new BugException("Dimension 0 cannot be modded!");
		}
	}

	public void printPlainText() {

		System.out.println("Plaintext:\n");
		// output must be 80 char per line
		for (int i = 1; i < this.finalMessage.length; i++) {

			if ((i % 80) == 0 && i != 0) {
				System.out.println("" + this.finalMessage[i - 1]);
			} else {
				System.out.print("" + this.finalMessage[i - 1]);
			}

			// old code used to visualze the chunks to make sure it was padded
			// correctly
			// what i is modded by is a hard coded value of the key size
			/*
			 * if (i % 4 == 0) { System.out.print("|"); }
			 */
		}
	}

	public void convertMessage() {

		this.convertedMessage = new int[this.finalMessage.length][1];
		// simply go through the message and swap the char for their
		// corrisponding index
		for (int i = 0; i < finalMessage.length; i++) {
			this.convertedMessage[i][0] = Utility.findAlphaIndex(this.finalMessage[i]);
		}
	}

	public void printCipherText() {

		System.out.println("Ciphertext:\n");
		// output must be 80 char per line
		for (int i = 1; i < this.cipherMessage.length + 1; i++) {

			if ((i % 80) == 0 && i != 0) {
				System.out.println("" + this.cipherMessage[i - 1]);
			} else {
				System.out.print("" + this.cipherMessage[i - 1]);
			}
		}
	}
}
