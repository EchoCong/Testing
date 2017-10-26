import java.io.FileNotFoundException;
import java.util.List;

// import com.sun.xml.internal.ws.api.pipe.ThrowableContainerPropertySet;

public class HillCipher {

	static char[] finalGrid;
	private int pc = 0;

	private String keyLocation;
	private String messageLocation;
	private String filePath;
	private boolean bugParseInt;
	private boolean bugMatrixDimension;
	private boolean bugDivideByZero;
	private boolean bugNoMessage;

	public HillCipher(String path, boolean b0, boolean b1, boolean b2, boolean b3) {
		// TODO Auto-generated constructor stub
		// keyLocation = key;
		// messageLocation = message;
		filePath = path;
		bugParseInt = b0;
		bugMatrixDimension = b1;
		bugDivideByZero = b2;
		bugNoMessage = b3;
	}

	public void execute(List<String> instructions) throws FileNotFoundException {
		// grab file names for key and plaintext its assumed they are located in
		// same directory as src files
		// create new object for key and message based on files
		String addFilePath = filePath.substring(0, filePath.lastIndexOf("/")+1);
		System.out.println(addFilePath);

		final int progLength = instructions.size();

		while (pc < progLength) {
			String test = instructions.get(pc);
			String[] toks = test.replaceAll("^\\s+", "").replaceAll("\\s+$", "").split("\\s+");

			keyLocation = addFilePath + toks[0];
			messageLocation = addFilePath + toks[1];

			try {
				Key inKey = new Key(keyLocation, bugParseInt, bugMatrixDimension, pc);
				Message message = new Message(messageLocation, inKey, bugDivideByZero, bugNoMessage, pc);

				// print key
				// inKey.printKey();
				// System.out.println();
				// print plaintext
				// message.printPlainText();

				// run the encryption method
				try {
					encrypt(inKey, message);
				} catch (Exception e) {
				}
				pc++;

			} catch (Exception e) {
				if (bugParseInt || bugMatrixDimension || bugDivideByZero || bugNoMessage) {
					System.err.println("Exception while executing program. (stacktrace follows)");
					e.printStackTrace(System.err);
					if (pc != progLength) {
						System.err.printf("Number of instructions executed before exception: %d%n", pc + 1);
					}
					System.exit(1);
				}
			}

			// print cipherText
			// System.out.println();
			// System.out.println();
			// printCiphertext();
			// System.out.println();
		}

	}

	public int getCount() {
		return pc;
	}

	public static void encrypt(Key key, Message message) {
		// go through converted message split into chunks of the key size
		// multiply these chunks by the key mod each result by 26
		// result into a char array finalGrid

		// temp will store the chunks of the plaintext that will be multipled by
		// key
		int[][] temp = new int[key.size][1];
		// product will hold the product of the matrix multiplication between
		// key and message chunks
		int[][] product = new int[key.size][1];
		// instantiate finalGrid this will hold our ciphertext as a char array
		finalGrid = new char[message.convertedMessage.length];
		int findex = 0;

		// loop through message char array
		// we are adding key.size to i each time so we get the correct size
		// chunk to multiply with
		for (int i = 0; i < message.convertedMessage.length; i = i + key.size) {

			// populate the temp array with chunks
			for (int j = 0; j < key.size; j++) {
				temp[j][0] = message.convertedMessage[i + j][0];
			}

			product = multiply(key.grid, temp);

			// take product and add it to the final char array
			// being sure to mod each result by 26 and convert back to a letter
			for (int k = 0; k < key.size; k++) {
				finalGrid[findex] = Utility.findLetter(product[k][0] % 26);
				// we do not want the index of finalGrid to restart every
				// iteration of the larger loop
				// so I added its own index counter
				findex++;
			}
		}
	}

	// return C = A * B
	// gotten from
	// http://introcs.cs.princeton.edu/java/22library/Matrix.java.html
	public static int[][] multiply(int[][] A, int[][] B) {
		int mA = A.length;
		int nA = A[0].length;
		int mB = B.length;
		int nB = B[0].length;
		// if (nA != mB) throw new RuntimeException("Illegal matrix
		// dimensions.");
		int[][] C = new int[mA][nB];
		for (int i = 0; i < mA; i++)
			for (int j = 0; j < nB; j++)
				for (int k = 0; k < nA; k++)
					C[i][j] += (A[i][k] * B[k][j]);
		return C;
	}

	public static void printCiphertext() {
		System.out.println("Ciphertext:\n");
		// output must be 80 char per line
		// we start at 1 so the first line of output is 80 characters
		// as well as all of the following lines
		for (int i = 1; i < finalGrid.length + 1; i++) {

			if ((i % 80) == 0 && i != 0) {
				System.out.println("" + finalGrid[i - 1]);
			} else {
				System.out.print("" + finalGrid[i - 1]);
			}
		}
	}

}
