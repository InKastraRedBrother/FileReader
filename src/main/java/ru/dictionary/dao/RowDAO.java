package ru.dictionary.dao;

import org.springframework.stereotype.Component;
import ru.dictionary.exception.DictionaryNotFoundException;
import ru.dictionary.model.Row;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.PatternSyntaxException;

import static ru.dictionary.dao.Util.Util.ELEMENTS_SEPARATOR;

@Component
public class RowDAO implements InterfaceDAOWord {

    private static final String TEMPORARY_FILENAME = "temp.txt";
    private final static String PATH_TO_ROW_STORAGE_DIRECTORY = System.getProperty("user.dir");
    private final static String ROW_STORAGE_PATH_AND_FILENAME = PATH_TO_ROW_STORAGE_DIRECTORY + File.separator + "row.txt";

    private final Codec codec;

    /**
     * Empty constructor that create directory for storage files, if they not exist.
     *
     * @throws DictionaryNotFoundException If a security manager exists and its SecurityManager.checkRead(String) method denies read access to the file(SecurityException).
     *                                     If the <code>pathname</code> argument is <code>null</code> (NullPointerException).
     */
    public RowDAO() {
        this.codec = new Codec();

        File directory = new File(PATH_TO_ROW_STORAGE_DIRECTORY);
        File rowFile = getRowStorageTxtFile();

        try {
            if ((!directory.mkdir() == directory.exists()) && !(!rowFile.createNewFile() == rowFile.exists())) {
                System.out.println("throw new DictionaryNotFoundException();");
            }
        } catch (IOException e) {
            throw new DictionaryNotFoundException("Error creating storage");
        }
    }

    private File getRowStorageTxtFile() {
        return new File(ROW_STORAGE_PATH_AND_FILENAME);
    }

    /**
     * Get all rows from file.
     *
     * @return String that have all rows.
     * @throws DictionaryNotFoundException if no line was found (NoSuchElementException).
     *                                     if scanner is closed (IllegalStateException).
     */
    public List<Row> findAll() {
        List<Row> listRow = new ArrayList<>();
        File rowFile = getRowStorageTxtFile();
        try (Scanner sc = new Scanner(rowFile, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                listRow.add(codec.convertFromStorageFormatToObjectFormat(sc.nextLine()));
            }
        } catch (NullPointerException | NoSuchElementException | IllegalStateException | IOException e) {
            throw new DictionaryNotFoundException("findAll");
        }
        return listRow;
    }

    /**
     * Save given pair - key value in storage.
     *
     * @param row class that contains String key, String value
     * @return boolean. if row added - true.
     * @throws DictionaryNotFoundException if the file exists but is a directory rather than
     *                                     a regular file, does not exist but cannot be created,
     *                                     or cannot be opened for any other reason (IOException).
     *                                     If a security manager exists and its SecurityManager.checkRead(String) method denies read access to the file(SecurityException).
     */
    public boolean save(Row row) {
        File rowsStorage = getRowStorageTxtFile();
        try (FileWriter fileWriter = new FileWriter(rowsStorage, StandardCharsets.UTF_8, true)) {
            if (rowsStorage.length() != 0) {
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.write(codec.convertFromObjectFormatToStorageFormat(row));
        } catch (IOException | SecurityException e) {
            throw new DictionaryNotFoundException("problem with save method");
        }
        return true;
    }

    @Override
    public Optional<Row> findByKey(String key, String fileName) {
        return Optional.empty();
    }

    @Override
    public boolean deleteByKey(String inputtedKey, String fileName) {
        return false;
    }

    /**
     * compare input row with rows in file.
     *
     * @param inputtedKeyForSearch by what parameter to search for a string.
     * @return String message that contains null or searched row.
     * @throws DictionaryNotFoundException if no line was found (NoSuchElementException).
     *                                     if scanner is closed (IllegalStateException).
     *                                     if the file is not found (IOException).
     */
//    public Optional<Row> findByKey(String inputtedKeyForSearch, String fileName) {
//        File file = createFile(fileName);
//        try (Scanner sc = new Scanner(file, StandardCharsets.UTF_8)) {
//            while (sc.hasNextLine()) {
//                String line = sc.nextLine();
//                Row row = codec.convertStorageEntryToKV(line);
//                if (row.getId_word_key().equals(inputtedKeyForSearch)) {
//                    return Optional.of(row);
//                }
//            }
//        } catch (IOException | NoSuchElementException | IllegalStateException e) {
//            throw new DictionaryNotFoundException("findByKey");
//        }
//        return Optional.empty();
//    }

    /**
     * Delete row by key.
     *
     * @param inputtedKeyForDeletion by what parameter to search for a row that should be deleted.
     * @return boolean. true - if row was found and deleted. false - if not.
     * @throws DictionaryNotFoundException if no line was found (NoSuchElementException).
     *                                     if this scanner is closed(IllegalStateException).
     *                                     If a security manager exists and its SecurityManager.checkDelete method denies delete access to the file (SecurityException).
     *                                     If parameter <code>mainFile</code> is <code>null</code> (NullPointerException).
     */
//    public boolean deleteByKey(String inputtedKeyForDeletion, String fileName) {
//        boolean isExist = false;
//        if ((findByKey(inputtedKeyForDeletion, fileName).isPresent())) {
//            boolean isFirstRow = true;
//            File mainFile = createFile(fileName);
//            File tempFile = createFile(TEMPORARY_FILENAME);
//            try (FileWriter fileWriter = new FileWriter(tempFile, StandardCharsets.UTF_8, true);
//                 Scanner sc = new Scanner(mainFile)) {
//
//                while (sc.hasNextLine()) {
//                    String line = sc.nextLine();
//                    Row row = codec.convertStorageEntryToKV(line);
//                    if (row.id_word_key()..equals(inputtedKeyForDeletion)){
//                        isExist = true;
//                    } else{
//                        if (isFirstRow) {
//                            isFirstRow = false;
//                        } else {
//                            fileWriter.write(System.lineSeparator());
//                        }
//                        fileWriter.write(codec.convertKVToStorageEntry(row));
//                    }
//                }
//            } catch (IOException | NoSuchElementException | IllegalStateException | NullPointerException |
//                     SecurityException e) {
//                throw new DictionaryNotFoundException("deleteByKey");
//            }
//            try {
//                mainFile.delete();
//                tempFile.renameTo(mainFile);
//            } catch (SecurityException | NullPointerException e) {
//                throw new DictionaryNotFoundException("deleteByKey");
//            }
//
//        }
//        return isExist;
//    }

    /**
     * Encapsulates the format in which the line in the file is stored.
     */
    private static class Codec {

        private static final int NUMBER_FOR_SPLIT = Row.class.getDeclaredFields().length;
        private static final int UUID_ROW_SERIAL_NUMBER = 0;
        private static final int ID_WORD_KEY_SERIAL_NUMBER = 1;
        private static final int ID_WORD_VALUE_SERIAL_NUMBER = 2;

        /**
         * Convert key and value into a String to storage format.
         *
         * @return String consisting of a key and value with a given separator.
         */
        public String convertFromObjectFormatToStorageFormat(Row row) {
            return row.getIdRow() + ELEMENTS_SEPARATOR + row.getIdWordKey() + ELEMENTS_SEPARATOR + row.getIdWordValue();
        }

        /**
         * Convert String from file to separate variables.
         *
         * @param lineFromFile line from file.
         * @throws DictionaryNotFoundException <code>encode.length</code> is smaller than <code>NUMBER_FOR_SPLIT</code>(ArrayIndexOutOfBoundsException).
         *                                     if the regular expression's syntax is invalid(PatternSyntaxException).
         */
        public Row convertFromStorageFormatToObjectFormat(String lineFromFile) {
            try {
                String[] encode = lineFromFile.split(ELEMENTS_SEPARATOR, NUMBER_FOR_SPLIT);
                Row row = new Row();
                row.setIdRow(encode[UUID_ROW_SERIAL_NUMBER]);
                row.setIdWordKey(encode[ID_WORD_KEY_SERIAL_NUMBER]);
                row.setIdWordValue(encode[ID_WORD_VALUE_SERIAL_NUMBER]);
                return row;

            } catch (ArrayIndexOutOfBoundsException | PatternSyntaxException e) {
                throw new DictionaryNotFoundException("convertStorageEntryToKV");
            }
        }
    }
}