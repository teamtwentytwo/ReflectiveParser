package methods;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Methods {
    //TODO: ERROR CHECKING
    //TODO: Implement changing of className.
    //TODO: Check if name include .class if not append.
    //Fields
    static String className = "Commands";
    static String jarName;
    static boolean  verbose = false;
    static Class  command;

    static final String INITIALIZATIONTEXT = "q           : Quit the program.\n" +
            "v           : Toggle verbose mode (stack traces).\n" +
            "f           : List all known functions.\n" +
            "?           : Print this helpful text.\n" +
            "<expression>: Evaluate the expression.\n" +
            "Expressions can be integers, floats, strings (surrounded in double quotes) or function\n" +
            "calls of the form '(identifier {expression}*)'.";
    static final String HELPTEXT = "Synopsis:\n" +
            "  methods\n" +
            "  methods { -h | -? | --help }+\n" +
            "  methods {-v --verbose}* <jar-file> [<class-name>]\n" +
            "Arguments:\n" +
            "  <jar-file>:   The .jar file that contains the class to load (see next line).\n" +
            "  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"methods.Commands\"]\n" +
            "Qualifiers:\n" +
            "  -v --verbose: Print out detailed errors, warning, and tracking.\n" +
            "  -h -? --help: Print out a detailed help message.\n" +
            "Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n" +
            "\n" +
            "This program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding\n" +
            "methods in <class-name>, and executes them, printing the result to sysout.";
    static final String SYNOPSIS = "Synopsis:\n" +
            "  methods\n" +
            "  methods { -h | -? | --help }+\n" +
            "  methods {-v --verbose}* <jar-file> [<class-name>]\n" +
            "Arguments:\n" +
            "  <jar-file>:   The .jar file that contains the class to load (see next line).\n" +
            "  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"methods.Commands\"]\n" +
            "Qualifiers:\n" +
            "  -v --verbose: Print out detailed errors, warning, and tracking.\n" +
            "  -h -? --help: Print out a detailed help message.\n" +
            "Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.\n";



    public static void main(String[] argv) {
        boolean help = false;
        boolean noqualifier = true;


        //Check flags
        int i = 0;
        for (String arg : argv) {
            String argument = arg;
            //Check long flags
            if (argument.startsWith("--")) {
                argument = argument.substring(2);
                if (argument.toLowerCase().equals("help")) help = true;
                if (argument.toLowerCase().equals("verbose")) verbose = true;
            } else if (argument.startsWith("-")) {
                for (char c : argument.substring(1).toCharArray()) {
                    if (c == '?' | c == 'h') {
                        help = true;
                        noqualifier = false;
                    }
                    if (c == 'v') {
                        verbose = true;
                        noqualifier = false;
                    }
                }
            } else {
                if (i == 0) {
                    i++;
                    jarName = argument;
                }else if (i == 1 ) {
                    i++;
                    className  = argument;
                }else{
                    System.exit(FatalErrors.invalidCommandLineArguments());
                }
            }
        }

        //Print necessary text
        if (noqualifier) System.out.println(SYNOPSIS);
        if (help) System.out.println(HELPTEXT);

        //Getting input
        String input;
        Scanner s = new Scanner(System.in);




        try {
            initialize();
        } catch (ClassNotFoundException e) {
            System.exit(FatalErrors.classNotFound(className));
        } catch (IOException e) {
            System.exit(FatalErrors.jarNotFound(jarName));
        }


        System.out.println(INITIALIZATIONTEXT);

		while (true){

            System.out.print("> ");
            input = s.next();
            switch (input) {
                case "v":
                    toggleVerbose();
                    break;
                case "h":
                case "?":
                    System.out.println(HELPTEXT);
                    break;
                case "q":
                    System.exit(0);
                case "f":
                    System.out.println(functionList());
                    break;
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

}
