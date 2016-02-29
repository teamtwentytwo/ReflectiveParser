import java.io.File;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by hilmi on 2016-02-25.
 */
public class Parser {

    Class theCommands;
    Map<String, Method> stringToMethod;
    Method methodApplying;

    public Parser(String filename) {
        stringToMethod = new HashMap<>();
        try {
            theCommands = Class.forName(filename);
            for (Method method : theCommands.getDeclaredMethods()){
                stringToMethod.put(method.getName(), method);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
        while (true){
            Scanner reader = new Scanner(System.in);  // Reading from System.in
            System.out.print("Parse: ");
            String line = reader.nextLine();
            List<String> parsedArguments = new ArrayList<>();
            Collections.addAll(parsedArguments, line.split("\\s"));
            try {
                tryConvert(parsedArguments, theCommands.getMethod(parsedArguments.get(0), int.class, int.class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            //doMethod(parsedArguments);
        }
    }
    /*
    public void doMethod(List<String> line){
        if (checkMethod(line)){
            try {
                Method methodToRun = theCommands.getMethod(line.get(0), Class.forName(line.get(1)));
                methodToRun.invoke();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }else{

        }

    }
    */

    public boolean tryConvert(List<String> arguments, Method methodToApply){
        Class[] c = methodToApply.getParameterTypes();
        ArrayList parameterValues = new ArrayList();
        for (Class parameter : c){
            for (String argument : arguments){
                if (Objects.equals(parameter.getName(), "int") && tryParseInt(argument)){

                }else if (Objects.equals(parameter.getName(), "float") && tryParseFloat(argument)){

                }
            }
        }
        System.out.println();
        return true;
    }

    public boolean checkMethod(List<String> line){
        Method[] methods = theCommands.getDeclaredMethods();
        boolean exist = false;
        String methodName = line.get(0);
        String[] arguments = new String[line.size()-1];
        for (Method method : methods){
            if (methodName.equals(method.getName())){
                exist = true;
            }
        }
        System.out.println(exist ? methodName + " exists." : methodName + " does not exist.");
        return exist;


    }
    //Utility Functions
    boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean tryParseFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

