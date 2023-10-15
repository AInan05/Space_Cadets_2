/** Space Cadets Challenge 2
 *  Basic interpreter for "BareBones Language".
 *  regular lines -> keyword(\s(condition\sdo)|(varName))?;
 */

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class BareBonesInterpreter {

    Integer indentation = 0;
    Dictionary<String, Integer> variables = new Hashtable<>();
    File bareBonesFile;
    List<String> code = new ArrayList<>();
    Integer lineNo = 0;
    Integer endLineNo = 0;
    Stack<Integer> whileStack = new Stack<>();

    public void openFile(String filePath) {
        try {
            bareBonesFile = new File(filePath);
            Scanner codeReader = new Scanner(bareBonesFile);
            while (codeReader.hasNextLine()) {
                code.add(codeReader.nextLine());
            }
                // System.out.println(line.matches(this.lineRegex));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    public void interpret() {
        while (lineNo < code.size()) {
            String s = code.get(lineNo);
            if (indentation >= 3) {
                if (!s.substring(indentation - 3).startsWith("end;")) {
                    s = s.substring(indentation);
                } else {
                    indentation -= 3;
                    endLineNo = lineNo;
                    lineNo = whileStack.pop();
                    s = code.get(lineNo);
                    s = s.substring(indentation);
                }
            }
            String[] stuff = s.split("\\s|;");
            execute(stuff);
            lineNo++;
        }
    }

    public void execute(String[] stuff) {
        String keyword = stuff[0];
        switch(keyword) {
            // deal with ; at the end later
            case "clear":
                clear(stuff[1]);
                System.out.println(variables);
                break;
            case "incr":
                incr(stuff[1]);
                System.out.println(variables);
                break;
            case "decr":
                decr(stuff[1]);
                System.out.println(variables);
                break;
            case "while":
                bbwhile(stuff[1], Integer.valueOf(stuff[3]));
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

    public void bbwhile(String name, Integer num) {
        if (variables.get(name).equals(num)) {
            lineNo = endLineNo;
        } else {
            whileStack.push(lineNo);
            indentation += 3;
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
