package Dictionary;

import java.io.Console;
import java.util.Scanner;

public class CommunicationWithConsole {


    public CommunicationWithConsole() {
    }
    private final Scanner in = new Scanner(System.in);
    private final Console console = System.console();

    public String inputInConsole(String selectionType, String pattern) {
        System.out.println(selectionType);

        String s = consoleChooser();

        if (!s.matches(pattern)){
            inputInConsole(selectionType, pattern);
        }
        return s;
    }

    public String consoleChooser() {
        {
            if (console != null) {
                return console.readLine();
            } else {
                return in.nextLine();
            }
        }
    }

    /**
     * Printing messages
     */



    private static final String ERR_INVALID_OPERATION = "Invalid operation";
    private static final String INPUT_KEY = "Input key";
    private static final String INPUT_VALUE = "Input value";
    private static final String PRINT_ERR_KEY_NOT_FOUND = "Key not found";
    private static final String MASK_ERROR = "The entered key or value do NOT match the constraints";

    public void errMessageUnsupportedOperation() {
        System.out.println(ERR_INVALID_OPERATION);
    }
    public void inputKey(){
        System.out.println(INPUT_KEY);
    }
    public void inputValue(){
        System.out.println(INPUT_VALUE);
    }
    public void printErrKeyNotFound(){
        System.out.println(PRINT_ERR_KEY_NOT_FOUND);
    }
    public void printErrMask(){
        System.out.println(MASK_ERROR);
    }
    public void printDeleteEntry(String s){
        System.out.println("String with key " + s + " has been deleted");
    }

}


