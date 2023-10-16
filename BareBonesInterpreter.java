/** Space Cadets Challenge 2
 *  Basic interpreter for "BareBones Language".
 *  regular lines -> keyword(\s(condition\sdo)|(varName))?;
 *  List of possible error messages:
 *  1. Value cannot be negative -
 *  2. var not found -
 *  3. File not found -
 *  4. Not a valid file type -
 *  5. Unexpected indentation
 *  6. Syntax Error -
 *  7. Unexpected end
 */

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
    Integer endLineNo = 0;
    Stack<Integer> whileStack = new Stack<>();
    String operations = "incr|decr|clear";
    String iterations = "while";
    boolean skip = false;

    public String openFile(String filePath) {
        try {
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
            return "File not found";
        }
    }

    public void interpret() {
        while (lineNo < code.size()) {
            String s = code.get(lineNo);
            if (indentation >= 3) {
                if (s.substring(indentation - 3).startsWith("end;")) {
                    indentation -= 3;
                    endLineNo = lineNo;
                    if (!skip) {
                        lineNo = whileStack.pop();
                    }
                    s = code.get(lineNo);
                    if (skip) {
                        skip = false;
                    }
                }
                s = s.substring(indentation);
            }
            if (!skip) {
                String errorCheck = execute(s);
            }
            lineNo++;
        }
    }

    public String execute(String s) {
        String[] stuff = s.split("\\s|;");
        String keyword = stuff[0];
        String errorCheck = syntaxErrorCheck(keyword, s);
        if (errorCheck.equals("pass")) {
            return errorCheck;
        }
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
        }
        return errorCheck;
    }

    public String syntaxErrorCheck(String keyword, String line) {
        if (keyword.matches(operations)) {
            String operationRegex = keyword + "\\s[a-z]+;";
            if (line.matches(operationRegex)) {
                return "pass";
            } else {
                return "Syntax Error";
            }
        } else if (keyword.matches(iterations)) {
            String iterationRegex = keyword + "\\s[a-z]+\\snot\\d;";
            if (line.matches(iterationRegex)) {
                return "pass";
            } else {
                return "Syntax Error";
            }
        }
        return "Keyword Error";
    }

    public void clear(String name) {
        variables.put(name, 0);
    }

    public String incr(String name) {
        if (variables.get(name) != null) {
            variables.put(name, (variables.get(name) + 1));
            return "pass";
        } else {
            return "Var not found";
        }
    }

    public String decr(String name) {
        if (variables.get(name) != null) {
            if (variables.get(name) == 0) {
                return "Values assigned to variables cannot be negative";
            }
            variables.put(name, (variables.get(name) - 1));
            return "pass";
        } else {
            return "Var not found";
        }
    }

    public void bbwhile(String name, Integer num) {
        indentation += 3;
        if (variables.get(name).equals(num)) {
            skip = true;
        } else {
            whileStack.push(lineNo);
        }
    }

    public static void main(String[] args) {
        System.out.println("Please enter code path: ");
        Scanner inputListener = new Scanner(System.in);
        String codePath = inputListener.nextLine();
        BareBonesInterpreter brb = new BareBonesInterpreter();
        brb.openFile(codePath);
        brb.interpret();
    }
}
