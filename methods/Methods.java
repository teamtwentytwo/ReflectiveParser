package methods;

import java.io.*;
import java.text.ParseException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;

public class Methods {
    // Fields
    static String className = "Commands";
    static String jarName = null;
    static boolean  verbose = false;
    static Class  command;

    static final String INITIALIZATIONTEXT = "q           : Quit the program.\n" +
            "v           : Toggle verbose mode (stack traces).\n" +
            "f           : List all known functions.\n" +
            "?           : Print this helpful text.\n" +
            "<expression>: Evaluate the expression.\n" +
            "Expressions can be integers, floats, strings (surrounded in double quotes) or function\n" +
            "calls of the form '(identifier {expression}*)'.\n";
    static final String SYNOPSIS = "Synopsis:\n" +
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
    static final String HELPTEXT = "This program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\n" +
            "methods in <class-name>, and executes them, printing the result to sysout.\n";



    public static void main(String[] argv) {


		// Read short and long flags
        boolean help = false;
        int i;
        for (i = 0; i < argv.length; i++) {
            String argument = argv[i];
            //Check long flags
            if (argument.startsWith("--")) {
                argument = argument.substring(2);
                if (argument.toLowerCase().equals("help")) {
                    help = true;
                } else if (argument.toLowerCase().equals("verbose")) {
                    verbose = true;
                } else {
                    System.exit(FatalErrors.unrecognizedQualifier(argument));
                }
            } else if (argument.startsWith("-")) {
                for (char c : argument.substring(1).toCharArray()) {
                    if (c == '?' | c == 'h') {
                        help = true;
                    } else if (c == 'v') {
                        verbose = true;
                    } else {
                        System.exit(FatalErrors.unrecognizedQualifier(c, argument));
                    }
                }
            } else {
                break;
            }
        }
		// Read positional arguments
        for (int p = 0; i < argv.length; i++, p++){
            if (argv[i].startsWith("-")){
                System.exit(FatalErrors.invalidOrder());
            }
            if (p == 0){
                jarName = argv[i];
            }else if (p == 1){
                className = argv[i];
            }else {
                System.exit(FatalErrors.invalidCommandLineArguments());
            }
        }


		// Help mode; print synopsis, help text, and exit
		if (help) {
			if (jarName != null) System.exit(FatalErrors.invalidHelp());
			System.out.print(SYNOPSIS);
			System.out.print("\n");
			System.out.print(HELPTEXT);
			System.exit(0);
		}
		// Invalid arguments; print synopsis and exit
		if (jarName == null) {
			System.out.print(SYNOPSIS);
			System.exit(0);
		}

        //Getting input
        String input;
        Scanner s = new Scanner(System.in);

        try {
            JarFile j = new JarFile(jarName);
            initialize();
        } catch (ClassNotFoundException e) {
            System.exit(FatalErrors.classNotFound(className));
        } catch (IOException e) {
            System.exit(FatalErrors.jarNotFound(jarName));
        }


        System.out.print(INITIALIZATIONTEXT);
		while (true){
            System.out.print("> ");
			if (s.hasNextLine()) {
				input = s.nextLine();
			}
			else {
				System.out.print("\n");
				break;
			}
            switch (input) {
                case "v":
                    toggleVerbose();
                    break;
                case "h":
                case "?":
                    System.out.println(HELPTEXT);
                    break;
                case "q":
               	    System.out.println("bye.");
                    System.exit(0);
                case "f":
                    System.out.println(functionList());
                    break;
                default:
                    try {
			Parser p = new Parser();
			p.provide(input);
			p.closeInput();
			Expression expression = p.parse();
			Object value = expression.evaluate(command);
			System.out.println(value.toString());
                    } catch (ParseException e) {		
                        System.out.print(formatErrorMessage(input, getStackTrace(e), e.getErrorOffset(), verbose));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
	}

    private static void initialize() throws ClassNotFoundException, IOException {
        File jarFile = new File(jarName);
        URL fileURL = jarFile.toURI().toURL();
        String jarURL = "jar:" + fileURL + "!/";
        URL urls [] = { new URL(jarURL) };
        URLClassLoader ucl = new URLClassLoader(urls);
        command = Class.forName(className, true,   ucl);
    }


    private static String functionList(){
        String functions = "";
        for (Method method : command.getDeclaredMethods()){
            String parameterTypes = "";
            for (Class temp : method.getParameterTypes()){
                parameterTypes += " " + temp.getSimpleName();
            }
            functions += "(" + method.getName() + parameterTypes + ") : " + method.getReturnType().getSimpleName() + "\n";
        }
        return functions;
    }



    //Helper Functions
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
    /**
     * formatMessage method, that constructs the error mesage
     * 
     * @param input The original input given as a string
     * @param stackTrace The stack trace generated by the exception, to extract message from
     * @param location The location/offset where the error occured
     * @param verbose The state of verbose that gives determines whether error message is detailed or simple
     * @return String The error message that is returned
     */
    public static String formatErrorMessage(String input, String stackTrace, int location, boolean verbose){
        int begin = stackTrace.indexOf(':');
        int end = stackTrace.indexOf('\n');
        //Extracting the simple error message from the stack trace
        String description = stackTrace.substring(begin + 2, end) + " at offSet " + location + "\n" + input + "\n";
        String pointer = "";
        //Generating the location pointer based on the location variable given at construct time
        for (int i = 0; i < location; i++) {
            pointer = pointer + "-";    
        }
        pointer = pointer + "^";
        //Returning detailed message with original stack trace if verbose is on/true
        if (verbose){
            return (description + pointer + "\n" + stackTrace);
        }else {
            return (description + pointer + "\n");
        }
    }

     /**
     * getStackTrace method, that turns stack trace into a String
     * 
     * @param e     The Exception that contains the stack trace
     * @return String The stack trace that is returned
     */
    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString(); // stack trace as a string
    }
}
