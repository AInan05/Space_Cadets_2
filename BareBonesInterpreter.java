/** Space Cadets Challenge 2
 *  Basic interpreter for "BareBones Language".
 */
// import necessary libraries
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class BareBonesInterpreter {

    Integer indentation = 0;
    Dictionary<String, Integer> variables = new Hashtable<>();
    File bareBonesFile;
    List<String> code = new ArrayList<>();
    Integer lineNo = 0;
    Stack<Integer> whileStack = new Stack<>();
    String operations = "incr|decr|clear";
    String iterations = "while";
    boolean skip = false;
    boolean endFound = false;

    /**
     * Open file method that checks for the correct file type and if the file exists.
     * If conditions are satisfied open the file.
     */

    public String openFile(String filePath) {
        try {
            // Check for the correct file type.
            if (!filePath.endsWith(".txt")){
                return "Invalid file type";
            }
            bareBonesFile = new File(filePath);
            Scanner codeReader = new Scanner(bareBonesFile);
            while (codeReader.hasNextLine()) {
                code.add(codeReader.nextLine());
            }
            return "pass";
        } catch (FileNotFoundException e) {
            // If file does not exist.
            return "File not found";
        }
    }

    public String interpret() {
        // While program has lines to interpret.
        while (lineNo < code.size()) {
            // Get line.
            String s = code.get(lineNo);
            String errorCheck = "pass";
            // Check for end statements.
            if (indentation >= 3) {
                if (s.substring(indentation - 3).startsWith("end;")) {
                    // Decrease the indentation level.
                    indentation -= 3;
                    endFound = true;
                }
            }
            // Remove whitespaces.
            s = s.substring(indentation);
            // If skip mode is inactivated or an end statement is encountered, execute the code.
            if (!skip || endFound) {
                errorCheck = execute(s, endFound);
            }
            // Return if an error was encountered.
            if (!errorCheck.equals("pass")) {
                return errorCheck;
            }
            // Increment line number.
            lineNo++;
        }
        return "pass";
    }

    public String execute(String s, boolean endFound) {
        // Divide the line into parts.
        String[] stuff = s.split("\\s|;");
        String keyword = stuff[0];
        // Run error check.
        String errorCheck = syntaxErrorCheck(keyword, s);
        // Return if an error was encountered.
        if (!errorCheck.equals("pass")) {
            return errorCheck;
        }
        // Call methods.
        switch(keyword) {
            case "clear":
                clear(stuff[1]);
                System.out.println(variables);
                break;
            case "incr":
                errorCheck = incr(stuff[1]);
                System.out.println(variables);
                break;
            case "decr":
                errorCheck = decr(stuff[1]);
                System.out.println(variables);
                break;
            case "while":
                bbwhile(stuff[1], Integer.valueOf(stuff[3]));
                break;
            case "end":
                if (!endFound){
                    return "Unexpected end";
                }
                end();
                break;
        }
        return errorCheck;
    }

    public String syntaxErrorCheck(String keyword, String line) {
        if (keyword.matches(operations)) {
            // Check if line matches regex.
            String operationRegex = keyword + "\\s([a-z]|[A-Z])+;";
            if (line.matches(operationRegex)) {
                return "pass";
            } else {
                return "Syntax Error";
            }
        } else if (keyword.matches(iterations)) {
            // Check if line matches regex.
            String iterationRegex = keyword + "\\s(([a-z]|[A-Z])+\\snot\\s\\d\\sdo);";
            if (line.matches(iterationRegex)) {
                return "pass";
            } else {
                return "Syntax Error";
            }
        } else if (keyword.matches("end")) {
            if (line.equals("end;")) {
                return "pass";
            } else {
                return "Syntax Error";
            }
        }
        // Unknown keyword.
        return "Keyword Error";
    }

    public void clear(String name) {
        // Assign var 0.
        variables.put(name, 0);
    }

    public String incr(String name) {
        if (variables.get(name) != null) {
            // Increase var.
            variables.put(name, (variables.get(name) + 1));
            return "pass";
        } else {
            // Var does not exist.
            return "Var not found";
        }
    }

    public String decr(String name) {
        if (variables.get(name) != null) {
            // Check if var is 0.
            if (variables.get(name) == 0) {
                return "Values assigned to variables cannot be negative";
            }
            // Decrease var.
            variables.put(name, (variables.get(name) - 1));
            return "pass";
        } else {
            // Var does not exist.
            return "Var not found";
        }
    }

    public void bbwhile(String name, Integer num) {
        // Increase indentation level.
        indentation += 3;
        // Push line no into the while stack.
        whileStack.push(lineNo);
        if (variables.get(name) != null){
            // If condition is false, activate skip mode.
            if (variables.get(name).equals(num)) {
                skip = true;
            }
        }
    }

    public void end() {
        endFound = false;
        if (skip) {
            // Set skip to false and remove the last while from while stack.
            skip = false;
            whileStack.pop();
        } else {
            // Set line number into the last while from the while stack - 1.
            lineNo = whileStack.pop() - 1;
        }
    }

    public static void main(String[] args) {
        String errorCheck;
        System.out.println("Please enter code path: ");
        Scanner inputListener = new Scanner(System.in);
        String codePath = inputListener.nextLine();
        // Initiate object
        BareBonesInterpreter brb = new BareBonesInterpreter();

        errorCheck = brb.openFile(codePath);
        if (errorCheck.equals("pass")) {
            errorCheck = brb.interpret();
        }
        if (errorCheck.equals("pass")) {
            System.out.println("The program was executed successfully.");
        } else {
            System.out.println("Error at line " + (brb.lineNo + 1) + ": " + errorCheck);
        }
    }
}
