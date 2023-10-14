/**The Plan.
 * Make a Scanner object to read the contents of the text file containing the "Bare Bones" code.
 * Separate each line into keywords and put them in an array.
 * Create methods for each keyword.
 * Execute methods using the parameters provided in each line of code.
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class BareBonesInterpreter {

    public static void main(String[] args) throws FileNotFoundException {
        File bareBonesText = new File("C:\\Users\\user\\Desktop\\test.txt");
        Scanner codeReader = new Scanner(bareBonesText);
        while (codeReader.hasNextLine()) {
            String line = codeReader.nextLine();
            System.out.println(line);
        }
    }
}

