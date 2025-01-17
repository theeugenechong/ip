package duke.storage;

import duke.exception.InvalidFileDataException;
import duke.exception.InvalidFileTypeException;
import duke.task.Task;

import duke.task.TaskList;
import duke.ui.Ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

import static duke.constants.DukeDataStorageConstants.BACKSLASH;
import static duke.constants.DukeDataStorageConstants.BACKSLASH_REGEX;
import static duke.constants.DukeDataStorageConstants.DEFAULT_STORAGE_FILEPATH;
import static duke.constants.DukeDataStorageConstants.FILE_CREATION_ERROR_MESSAGE;
import static duke.constants.DukeDataStorageConstants.FILE_WRITE_ERROR_MESSAGE;
import static duke.constants.DukeDataStorageConstants.VALID_FILE_TYPE;

/**
 * Handles storage of data in Duke's task list.
 * This includes reading and writing to a {@code ".txt"} file in the user's device.
 */
public class DataStorage {

    private Path filePath;

    /**
     * Parameterless constructor will set the storage file path to the default, {@code "dukeData\tasks.txt"}.
     */
    public DataStorage() {
        this(DEFAULT_STORAGE_FILEPATH);
    }

    /**
     * Constructor which allows for a specific file path to be created for data storage.
     * Prints an error message if the file specified is not a {@code ".txt"} file.
     *
     * @param filePath string representing the file path
     */
    public DataStorage(String filePath) {
        try {
            checkFileType(filePath);
            this.filePath = Paths.get(filePath);
        } catch (InvalidFileTypeException ifte) {
            Ui.printInvalidFileTypeMessage();
            System.exit(1);
        }
    }

    /**
     * Returns storage file path in string format.
     *
     * @return string representing file path
     */
    public String getFilePath() {
        return filePath.toString();
    }

    /**
     * Loads data from storage file into {@code taskList} upon entering the program.
     *
     * @param taskList task list into which the tasks are to be loaded
     */
    public void loadData(TaskList taskList) {
        if(filePath == null) {
            return;
        }
        Scanner fileScanner = getScanner(filePath.toString());
        readData(fileScanner, taskList.getTaskList());
    }

    /**
     * Saves {@code taskList} into storage file upon any change to the tasks in the list.
     * Such changes include addition of a task, deletion of a task, and marking a task as done.
     * Prints an error message if there is an error writing to the file.
     *
     * @param taskList task list containing data of tasks to be saved
     */
    public void saveData(TaskList taskList) {
        try {
            writeData(taskList.getTaskList());
        } catch (IOException ioe) {
            System.out.println(FILE_WRITE_ERROR_MESSAGE + ioe.getMessage());
            System.exit(1);
        }
    }

    /**
     * Obtains a {@code Scanner} to read from the storage file specified by {@code filePath}.
     * If the file does not exist, the file is created. An error message is printed if there is an error creating
     * the file.
     *
     * @param filePath string representing the file path
     * @return scanner which is able to read lines from storage file
     */
    private static Scanner getScanner(String filePath) {
        File taskFile = new File(filePath);
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(taskFile);
        } catch (FileNotFoundException fileNotFoundException) {
            try {
                createFileInDirectory(filePath);
            } catch (IOException ioe) {
                System.out.println(FILE_CREATION_ERROR_MESSAGE + ioe.getMessage());
            }
        }
        return fileScanner;
    }

    /**
     * Reads lines representing tasks from {@code fileScanner} to be decoded and added to the task list. Prints an
     * error message if the data in the file is of an invalid format. An error message is also printed if the date and
     * time for {@code Event}/{@code Deadline} objects are of the wrong format.
     *
     * @param fileScanner scanner which reads lines from storage file
     * @param taskList task list into which the decoded tasks are added
     */
    private static void readData(Scanner fileScanner, ArrayList<Task> taskList) {
        if (fileScanner != null) {
            while (fileScanner.hasNext()) {
                String task = fileScanner.nextLine();
                try {
                    Task decodedTask = TaskDecoder.decodeTask(task);
                    taskList.add(decodedTask);
                } catch (InvalidFileDataException ifde) {
                    Ui.printFileTaskInvalidFormatMessage();
                } catch (DateTimeParseException dtpe) {
                    Ui.printFileInvalidDateTimeMessage();
                }
            }
        }
    }

    /**
     * Encodes each task in {@code taskList} into a string which is written to the storage file.
     *
     * @param taskList task list containing tasks to be encoded and written to storage file
     * @throws IOException if there is an error writing to the file
     */
    private static void writeData(ArrayList<Task> taskList) throws IOException {
        FileWriter fileWriter = new FileWriter(DEFAULT_STORAGE_FILEPATH, false);

        for (Task task : taskList) {
            StringBuilder formattedTask = new StringBuilder();
            TaskEncoder.encodeTask(task, fileWriter, formattedTask);
        }
        fileWriter.close();
    }

    /**
     * Creates a file with the path specified by {@code filePath}.
     *
     * @param filePath string representing the file path
     * @throws IOException if there is an error creating the file
     */
    private static void createFileInDirectory(String filePath) throws IOException {
        String directoryName = getDirectoryPath(filePath);
        Files.createDirectories(Paths.get(directoryName));
        Files.createFile(Paths.get(filePath));
    }

    /**
     * Helper function which returns the full directory path of {@code filePath}. The purpose of this method
     * is so that the directory for the storage file can be created first before the file is created in the
     * {@code createFileInDirectory} method.
     *
     * @param filePath string representing the file path
     * @return a string representing the full directory path of {@code filePath}
     */
    private static String getDirectoryPath(String filePath) {
        String[] directoryPathAsArray = filePath.split(BACKSLASH_REGEX);
        StringBuilder directoryPath = new StringBuilder();

        /* Iterate up to length - 1 because the last argument in a file path is usually the file type */
        for (int i = 0; i < (directoryPathAsArray.length - 1); i++) {
            directoryPath.append(directoryPathAsArray[i]).append(BACKSLASH);
        }
        return String.valueOf(directoryPath);
    }

    /**
     * Helper function to check if file specified is valid.
     * File is considered valid if it ends in {@code ".txt"}.
     *
     * @param filePath string representing the file path
     * @throws InvalidFileTypeException if file specified is not a {@code ".txt"} file
     */
    private static void checkFileType(String filePath) throws InvalidFileTypeException {
        if (!filePath.endsWith(VALID_FILE_TYPE)) {
            throw new InvalidFileTypeException();
        }
    }
}
