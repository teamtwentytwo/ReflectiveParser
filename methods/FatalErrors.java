package methods;

public class FatalErrors {
    public static int unrecognizedQualifier(String longQualifier){
        System.err.println("Unrecognized qualifier: --"+ longQualifier+ ".");
        return -1;
    }
    public static int unrecognizedQualifier(String letter,  String lettersInQualifier){
        System.err.println("Unrecognized qualifier "+ letter +" in "+ lettersInQualifier +".");
        return -1;
    }

    public static int invalidCommandLineArguments(){
        System.err.println("This program takes at most two command line arguments.");
        return -2;
    }
    public static int invalidOrder(){
        System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
        return -3;
    }
    public static int invalidHelp(){
        System.err.println("Qualifier --help (-h, -?) should not appear with any command-line arguments.");
        return -4;

    }
    public static int jarNotFound(String jarName){
        System.err.println("Could not load jar file: " + jarName);
        return -5;

    }
    public static int classNotFound(String className){
        System.err.println("Could not find class: " + className);
        return -6;
    }
}
