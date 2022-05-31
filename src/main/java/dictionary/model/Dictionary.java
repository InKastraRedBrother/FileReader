package dictionary.model;

import dictionary.controller.IOStream;

import java.io.*;
import java.util.Scanner;

/**
 * Class provides Dictionary API.
 */
public class Dictionary {
    private static final String KEY_VALUE_SEPARATOR = ":";
    private static final String KEY_VALUE_ADDED = "Запись: ключ" + KEY_VALUE_SEPARATOR + "значение добавлена - ";
    private final static String FOUND_LINE = "Строка с запрощенным ключём найдена - ";
    private final static String NOT_FOUND_LINE = "Строка с запрощенным ключём НЕ найдена - ";
    private static final String INPUT_KEY = "Input key";
    private static final String INPUT_VALUE = "Input value";
    private static final String PRINT_ERR_KEY_NOT_FOUND = "Key not found";
    private static final String MASK_ERROR = "The entered key or value do NOT match the constraints";
    private static final String LINE_REMOVED = "Row has been removed with the requested key - ";
    /**
     * Name of temporary file
     */
    public static final String TEMPORARY_FILE_NAME = "temp";

    /**
     * Contains file name
     */
    private final String fileName;

    /**
     * Mask to restrict character input
     */
    private final String pattern;

    IOStream ioStream;
    /**
     * Initializes a newly created object. Creates a file based on the passed parameter
     *
     * @param fileName fileName
     * @param pattern  pattern
     */
    public Dictionary(String fileName, String pattern, IOStream ioStream) {
        this.fileName = fileName;
        this.pattern = pattern;
        this.ioStream = ioStream;
        ioStream.createFile(this.fileName);
    }

    /**
     * Show all file lines to console from the file which specified in the constructor
     */
    public void showAll() {

        BufferedReader br = ioStream.getBufferedReader(fileName);
        String lineList;
        StringBuilder sf = new StringBuilder();
        while (true) {
            try {
                if ((lineList = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sf.append(lineList).append("\n");
        }
        System.out.println(sf);

        ioStream.closeBufferedReader();
    }

    /**
     * Search line which specified in console input from file
     */
    public void search() {

        String match = null;
        BufferedReader br = ioStream.getBufferedReader(fileName);


        System.out.println(INPUT_KEY);
        String key = consoleChooser();
        String line;
        while (true) {
            try {
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line.contains(key + KEY_VALUE_SEPARATOR)) {
                match = line;
            }
        }

        if (match != null) {
            System.out.println(FOUND_LINE + key + " " + match);
        } else {
            System.out.println(NOT_FOUND_LINE + key);
        }

        ioStream.closeBufferedReader();

    }

    /**
     * Add line which is entered into the console and matches pattern to the end of the file
     */
    public void add() {

        FileWriter writer = ioStream.getFileWriter(fileName);

        System.out.println(INPUT_KEY);
        String key = consoleChooser();
        System.out.println(INPUT_VALUE);
        String value = consoleChooser();

        try {
            if (key.matches(pattern)) {
                writer.write("\n" + key + KEY_VALUE_SEPARATOR + value);
                System.out.println(KEY_VALUE_ADDED + key + KEY_VALUE_SEPARATOR + value);
            } else {
                System.out.println(MASK_ERROR);
            }
        } catch (Exception e) {
            System.out.println(MASK_ERROR);
        }
        ioStream.closeFileWriter();
    }

    /**
     * Create temporary file. Write to temporary file every line, excluding line that need to be deleted.
     * Deleting main file. Temporary file will be renamed to the name of the main file
     */
    public void deleteEntry() {
        File tempFile = ioStream.createFile(TEMPORARY_FILE_NAME);
        File file = ioStream.createFile(fileName);

        System.out.println(INPUT_KEY);
        String s = consoleChooser();
        if (s.matches(pattern)) {

            FileWriter fileWriter = ioStream.getFileWriter(TEMPORARY_FILE_NAME);
            BufferedReader bufferedReader = ioStream.getBufferedReader(fileName);

            String line;
            int counter = 0;
            boolean isExist = false;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (line.contains(s)) {
                    isExist = true;
                }
                if (!line.contains(s) && !line.isBlank()) {
                    try {
                        if (counter == 0) {
                            fileWriter.write(line);
                        } else {
                            fileWriter.write(System.lineSeparator());
                            fileWriter.write(line);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    counter++;
                }
            }
            if (isExist) {
                System.out.println(LINE_REMOVED + s);
            } else {
                System.out.println(PRINT_ERR_KEY_NOT_FOUND + s);
            }

            ioStream.closeBufferedReader();
            ioStream.closeFileWriter();

            file.delete();
            tempFile.renameTo(new File(ioStream.pathToDictionary + fileName + ioStream.FILE_FORMAT));

        } else {
            System.out.println(MASK_ERROR);
        }

    }
    public String consoleChooser() {
        final Scanner in = new Scanner(System.in);
        final Console console = System.console();
        if (console != null) {
            return console.readLine();
        } else {
            return in.nextLine();
        }
    }
}

