package io.github.kezzyhko.gameoflife;

import java.io.*;
import java.util.*;

public class FileSystem {
    private FileSystem() {}

    public static TreeSet<String> getSetOfFiles(String folderPath) {
        File folder = new File(folderPath);
        TreeSet<String> result = new TreeSet<>();
        File[] list = folder.listFiles();
        if (list == null || !folder.isDirectory()) {
            return result;
        }
        for (File file : list) {
            if (file.isFile()) {
                result.add(file.getName());
            }
        }
        return result;
    }
    public static String[] getFilesNames(String directory) {
        File[] filesAndDirs = new File(directory).listFiles();
        if (filesAndDirs == null) {
            return new String[]{};
        }
        ArrayList<String> fileNames = new ArrayList();
        for (File fileOrDir : filesAndDirs) {
            if (fileOrDir.isFile()) {
                fileNames.add(fileOrDir.getName());
            }
        }
        return fileNames.toArray(new String[fileNames.size()]);
    }

    public static boolean fileExists(String filePath) {
        return new File(filePath).isFile();
    }

    public static String getFileContents(String filePath) throws FileNotFoundException {
        return getFileContents(new File(filePath));
    }
    public static String getFileContents(File file) throws FileNotFoundException {
        return new Scanner(file).useDelimiter("\\Z").next();
    }

    public static void createNewFile(String filePath) throws IOException {
        File f = new File(filePath);
        f.getParentFile().mkdirs();
        f.createNewFile();
    }
    public static void createNewFile(String filePath, String content) throws IOException {
        createNewFile(filePath);
        new PrintStream(new FileOutputStream(filePath)).print(content);
    }
    public static void createNewFile(String filePath, Properties content) throws IOException {
        createNewFile(filePath);
        content.store(new FileOutputStream(filePath), null);
    }

    public static void changeProperty(String filePath, String property, String value) throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(filePath));
        prop.setProperty(property, value);
        prop.store(new FileOutputStream(filePath), null);
    }
}
