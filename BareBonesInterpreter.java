/** Space Cadets Challenge 2
 *  Basic interpreter for "BareBones Language".
 */

import java.util.Scanner;
import java.util.Dictionary;
import java.util.Hashtable;
import java.io.File;
import java.io.FileNotFoundException;

public class BareBonesInterpreter {

    Dictionary<String, Integer> variables = new Hashtable<>();
    File bareBonesFile;

    public void interpret(String filePath) {
        try {
            bareBonesFile = new File(filePath);
            Scanner codeReader = new Scanner(bareBonesFile);
            while (codeReader.hasNextLine()) {
                String line = codeReader.nextLine();
                String[] stuff = line.split(" ");
                execute(stuff[0], stuff[1]);
                System.out.println(variables);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void execute(String keyword, String name) {
        switch(keyword) {
            // deal with ; at the end later
            case "clear":
                clear(name);
                break;
            case "incr":
                incr(name);
                break;
            case "decr":
                decr(name);
                break;
        }
    }

    public void clear(String name) {
        variables.put(name, 0);
    }

    public void incr(String name) {
        if (variables.get(name) != null) {
            variables.put(name, (variables.get(name) + 1));
        }// else {
         // error message
        //}
    }

    public void decr(String name) {
        if (variables.get(name) != null) {
            variables.put(name, (variables.get(name) - 1));
        }// else {
        // error message
        //}
    }

    public static void main(String[] args) {
        System.out.println("Please enter code path: ");
        Scanner inputListener = new Scanner(System.in);
        String codePath = inputListener.nextLine();
        BareBonesInterpreter brb = new BareBonesInterpreter();
        brb.interpret(codePath);
    }
}

// Test file: C:\\Users\\user\\Desktop\\test.txt
