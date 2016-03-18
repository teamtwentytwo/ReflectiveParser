package methods;

/**
 * Class containing the various errors that will cause the program to crash and stop.
 */
class FatalErrors {
    /**
     * Method will be called when a long Qualifier (ex. --verbose) is unknown to the program.
     *
     * @param longQualifier is a String that is not recognized by the parser.
     * @return the exit code.
     */
    static int unrecognizedQualifier(String longQualifier){
        System.err.println("Unrecognized qualifier: --"+ longQualifier+ ".");
		System.err.print(Methods.SYNOPSIS);
        return -1;
    }

    /**
     * Method will be called when a flag (ex. -f) is unknown to the program.
     *
     * @param letter the letter in question (ex. 'q')
     * @param lettersInQualifier the entire qualifier (ex. -hfv)
     * @return the exit code.
     */
    static int unrecognizedQualifier(char letter,  String lettersInQualifier){
        System.err.println("Unrecognized qualifier '"+ letter +"' in '"+ lettersInQualifier +"'.");
		System.err.print(Methods.SYNOPSIS);
        return -1;
    }

    /**
     * Method will be called when invalid command line arguments are called.
     *
     * @return the exit code.
     */
    static int invalidCommandLineArguments(){
        System.err.println("This program takes at most two command line arguments.");
		System.err.print(Methods.SYNOPSIS);
        return -2;
    }

    /**
     * Method will be called when the order of the command line arguments is invalid
     *
     * @return the exit code.
     */
    static int invalidOrder(){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
		System.err.print(Methods.SYNOPSIS);
        return -3;
    }

    /**
     * Method will be called when help is called with command line arguments following it. (ex. methods -h commands.jar Commands)
     *
     * @return the exit code.
     */
    static int invalidHelp(){
        System.err.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.");
		System.err.print(Methods.SYNOPSIS);
        return -4;
    }

    /**
     * The jar that is passed through command line arguments cannot be located
     *
     * @param jarName the jar name in question.
     * @return the exit code.
     */
    static int jarNotFound(String jarName){
        System.err.println("Could not load jar file: " + jarName);
        return -5;
    }

    /**
     * The class that is passed through command line arguments cannot be located.
     *
     * @param className the class name in question.
     * @return the exit code.
     */
    static int classNotFound(String className){
        System.err.println("Could not find class: " + className);
        return -6;
    }
}
