package org.emp3r0r7.filesystem;

import org.emp3r0r7.configuration.ConfigReader;

import java.io.File;

public class FileSystemEngine {

    public static final String TEMP_PATH = ConfigReader.getTempPath();

    /**
     * Cerca un file che contiene una stringa specificata nel suo contenuto.
     *
     * @param directoryPath Il percorso della directory.
     * @param searchString  La stringa da cercare nei file.
     * @return Il file che contiene la stringa specificata, o null se non viene trovato.
     */
    public static File searchFileContainingString(String directoryPath, String searchString) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Il percorso specificato non è una directory valida.");
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().contains(searchString)) {
                    return file;
                }
            }
        }

        return null; // Nessun file trovato che contenga la stringa
    }
}
