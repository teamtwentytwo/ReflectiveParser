import java.lang.reflect.Method;
import java.util.Scanner;

public class Parser {
    //TODO: ERROR CHECKING
    //TODO: Implement changing of className.

    //Fields
    static String className = "Commands";
    static boolean  verbose = false;
    static Object o;
    static Class  c;
    public static void main(String[] argv) {
        boolean help = false;
        boolean noqualifier = true;

        String INITIALIZATIONTEXT = "q           : Quit the program.\n" +
                "v           : Toggle verbose mode (stack traces).\n" +
                "f           : List all known functions.\n" +
                "?           : Print this helpful text.\n" +
                "<expression>: Evaluate the expression.\n" +
                "Expressions can be integers, floats, strings (surrounded in double quotes) or function\n" +
                "calls of the form '(identifier {expression}*)'.";
        String HELPTEXT = "Synopsis:\n" +
                "  methods\n" +
                "  methods { -h | -? | --help }+\n" +
                "  methods {-v --verbose}* <jar-file> [<class-name>]\n" +
                "Arguments:\n" +
                "  <jar-file>:   The .jar file that contains the class to load (see next line).\n" +
                "  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\n" +
                "Qualifiers:\n" +
                "  -v --verbose: Print out detailed errors, warning, and tracking.\n" +
                "  -h -? --help: Print out a detailed help message.\n" +
                "Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n" +
                "\n" +
                "This program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\n" +
                "methods in <class-name>, and executes them, printing the result to sysout.";
        String SYNOPSIS = "Synopsis:\n" +
                "  methods\n" +
                "  methods { -h | -? | --help }+\n" +
                "  methods {-v --verbose}* <jar-file> [<class-name>]\n" +
                "Arguments:\n" +
                "  <jar-file>:   The .jar file that contains the class to load (see next line).\n" +
                "  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]\n" +
                "Qualifiers:\n" +
                "  -v --verbose: Print out detailed errors, warning, and tracking.\n" +
                "  -h -? --help: Print out a detailed help message.\n" +
                "Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n";


        for (String argument : argv){
            if (argument.contains("-")){
                int index = 0;
                for (int i = 0 ; i < argument.length(); i++){
                    if (argument.charAt(i) == '-'){
                        index = i;
                    }
                }
                for (char c: argument.substring(index+1).toCharArray()){
                    if (c == '?' | c == 'h'){
                        help = true;
                        noqualifier = false;
                    }
                    if ( c == 'v'){
                        verbose = true;
                        noqualifier = false;
                    }
                }
            }
            if (argument.toLowerCase().contains("--help")) help = true;
            if (argument.toLowerCase().contains("--verbose")) verbose = true;
        }
        if (noqualifier) System.out.println(SYNOPSIS);
        if (help) System.out.println(HELPTEXT);

        String input;
        Scanner s = new Scanner(System.in);

        //initialize();
        //Print ONLY if Program initialized properly.
        System.out.println(INITIALIZATIONTEXT);

		while (true){

            System.out.print("> ");
            input = s.next();
            switch (input) {
                case "v":
                    toggleVerbose();
                    break;
                case "q":
                    System.exit(0);
                case "f":
                    System.out.println(functionList());
                    break;
            }

        }
	}

    private static String functionList(){
        String functions = "";
        try {
            c = Class.forName(className);
            for (Method method : c.getDeclaredMethods()){
                String parameterTypes = "";
                for (Class temp : method.getParameterTypes()){
                    parameterTypes += " " + temp.getSimpleName();
                }
                functions += "(" + method.getName() + parameterTypes + ") : " + method.getReturnType().getSimpleName() + "\n";
            }
            return functions;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return functions;

    }
    private static void initialize(){


    }

    private static void toggleVerbose(){
        if (!verbose) {
            verbose = true;
            System.out.println("Verbose on");
        }
        else {
            verbose = false;
            System.out.println("Verbose off");

        }
    }

}
