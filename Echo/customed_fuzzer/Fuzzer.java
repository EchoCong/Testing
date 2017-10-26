import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Fuzzer {
	private static String replacesInput = "ABCDEFGHIJKLMNOPQRSTUVWXY Z01asc23456789~!@#$%^&a*()_+a";
	private static String validInput_lisa = "Asdfkjij[ndfjskf sdlfjsef sdfsleosf     sdlfslfkjsldfsdfadf sdfeslaqpk";
	private static String validInput_aosen = "6;true;WelcomeToBeijing2008";
	private static String validInput_sky = "QXJ0IG9mIENvbXB1dGVyIFByb2dyYW1taW5nLCBWb2x1bWUgMSwgRmFzY2ljbGUgMSwgVGhlOiBNTUlYIC0tIEEgUklTQyBDb21wdXRlciBmb3IgdGhlIE5ldyBNaWxsZW5uaXVt";

	private static int NUM = 100;

	private String whatToWrite_lisa = "";
	private String whatToWrite_aosen = "";
	private String whatToWrite_sky = "";

	private Random r = new Random();

	public static void main(String[] args) {
		Fuzzer fuzzer = new Fuzzer();
		fuzzer.run();
	}

	/***********************************
	 * Write instructions to file *
	 ***********************************/
	private void writeToFile(String whatToWrite, String name, int num) {
		try {
			Files.write(Paths.get("fuzz_Echo_" + name + "_"+ Integer.toString(num+1) +".s"), whatToWrite.getBytes());
		} catch (IOException e) {
			System.out.println("Write to file error!");
		}
	}

	/**************************
	 * Main Function *
	 **************************/
	private void run() {
		int i = 0;
		// Row 1-300 : "MOV" instructions
		for (int num = 0; num < 4; num++) {
			while (i++ < NUM) {
				Instruction instruction = Instruction.randomInstruction();
				// System.out.println(instruction.toString());
				// System.out.println(instruction.getOpcode());
				if (instruction.getOpcode().equals("mutate")) {
					whatToWrite_lisa += mutationEvent(validInput_lisa) + "\n";
				} else if (instruction.getOpcode().equals("delete")) {
					whatToWrite_lisa += deleteEvent(validInput_lisa) + "\n";
				} else if (instruction.getOpcode().equals("add")) {
					whatToWrite_lisa += addEvent(validInput_lisa) + "\n";
				} else if (instruction.getOpcode().equals("empty")) {
					whatToWrite_lisa += "\n";
				}
			}
			writeToFile(whatToWrite_lisa, "Lisa", num);
			while (i++ < NUM) {
				Instruction instruction = Instruction.randomInstruction();
				// System.out.println(instruction.toString());
				// System.out.println(instruction.getOpcode());
				if (instruction.getOpcode().equals("mutate")) {
					whatToWrite_aosen += mutationEvent(validInput_aosen) + "\n";
				} else if (instruction.getOpcode().equals("delete")) {
					whatToWrite_aosen += deleteEvent(validInput_aosen) + "\n";
				} else if (instruction.getOpcode().equals("add")) {
					whatToWrite_aosen += addEvent(validInput_aosen) + "\n";
				} else if (instruction.getOpcode().equals("empty")) {
					whatToWrite_aosen += "\n";
				}
			}
			writeToFile(whatToWrite_aosen, "Aosen", num);

			while (i++ < NUM) {
				Instruction instruction = Instruction.randomInstruction();
				// System.out.println(instruction.toString());
				// System.out.println(instruction.getOpcode());
				if (instruction.getOpcode().equals("mutate")) {
					whatToWrite_sky += mutationEvent(validInput_sky) + "\n";
				} else if (instruction.getOpcode().equals("delete")) {
					whatToWrite_sky += deleteEvent(validInput_sky) + "\n";
				} else if (instruction.getOpcode().equals("add")) {
					whatToWrite_sky += addEvent(validInput_sky) + "\n";
				} else if (instruction.getOpcode().equals("empty")) {
					whatToWrite_sky += "\n";
				}
			}
			writeToFile(whatToWrite_sky, "Sky", num);
		}
	}

	private String deleteEvent(String validInput) {
		int l = r.nextInt(5) + 1;
		int originLocation = r.nextInt(validInput.length() - l);
		String validBegin = validInput.substring(0, originLocation);
		String validEnd = validInput.substring(originLocation + l);
		String deleteInput = validBegin + validEnd;

		// System.out.printf("originLocation: %d %s\n", originLocation,
		// validInput.charAt(originLocation));
		// System.out.println("DELETE INPUT: " + deleteInput);
		// System.out.println("");

		return deleteInput;
	}

	private String mutationEvent(String validInput) {
		int originLocation = r.nextInt(validInput.length());
		int replacedLocation = r.nextInt(replacesInput.length());

		String insertInput = String.valueOf(replacesInput.charAt(replacedLocation));
		String validBegin = validInput.substring(0, originLocation);
		String validEnd = validInput.substring(originLocation + 1);
		String newInput = validBegin + insertInput + validEnd;

		// System.out.printf("originLocation: %d %s\n", originLocation,
		// validInput.charAt(originLocation));
		// System.out.printf("replacedLocation: %d %s\n", replacedLocation,
		// replacesInput.charAt(replacedLocation));
		// System.out.println("ADD INPUT: " + newInput);
		// System.out.println("");

		return newInput;
	}

	private String addEvent(String validInput) {
		int l = r.nextInt(5) + 1;
		int originLocation = r.nextInt(validInput.length());
		int replacedLocation = r.nextInt(replacesInput.length() - l);

		String insertInput = replacesInput.substring(replacedLocation, replacedLocation + l);
		String validBegin = validInput.substring(0, originLocation);
		String validEnd = validInput.substring(originLocation);
		String addInput = validBegin + insertInput + validEnd;

		// System.out.printf("originLocation: %d %s\n", originLocation,
		// validInput.charAt(originLocation));
		// System.out.printf("replacedLocation: %d %s\n", replacedLocation,
		// replacesInput.charAt(replacedLocation));
		// System.out.println("MUTATE INPUT: " + addInput);
		// System.out.println("");

		return addInput;
	}
}

