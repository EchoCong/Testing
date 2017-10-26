import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Key {

	int size;
	int[][] grid;

	public Key(String keyFile, boolean bugParseInt, boolean bugMatrixDimension, int pc)
			throws FileNotFoundException, BugException {

		// Open the file for reading
		Scanner scanner = new Scanner(new File(keyFile));

		// Get size of matrix will always be a square matrix
		try {
			this.size = scanner.nextInt();

			// Allocate the grid.
			this.grid = new int[size][size];

			// Iterate through the rows.
				for (int row = 0; row < this.size; row++) {
					// Iterate through the columns.
					for (int column = 0; column < this.size; column++) {
						// Read each value
						this.grid[row][column] = scanner.nextInt();
					}
				}
				scanner.close();
		} catch(InputMismatchException e){
			if (bugParseInt)
				throw new BugException("Key input file contains no integer elements.");
		} catch(Exception e){
			if (bugMatrixDimension)
				throw new BugException("Illegal matrix dimensions.");
		}
	}

	public void printKey() {
		// same as all other print methods
		System.out.println("Key Matrix:\n");
		for (int row = 0; row < this.size; row++) {
			for (int column = 0; column < this.size; column++) {
				if (column == this.size - 1) {
					System.out.println(this.grid[row][column]);
				} else {
					System.out.print(this.grid[row][column] + " ");
				}
			}
		}
	}

}
