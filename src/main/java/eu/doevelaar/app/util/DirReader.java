package eu.doevelaar.app.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility-class om een gegeven directory in te lezen.
 */
public class DirReader {
    private final static int NO_SUBDIRS = 1;
    private Path dir;

    public DirReader(Path directoryToRead) {
        this.dir = directoryToRead;
    }

    /**
     * Geeft een Stream terug van de opgegeven directory, zonder subdirectories.
     * @return Stream van de opgegeven directory.
     * @throws IOException
     */
    private Stream<Path> getFileStream() throws IOException {
        return Files.walk(this.dir, NO_SUBDIRS).filter(path -> {
            try {
                return !(Files.readAttributes(path, BasicFileAttributes.class).isDirectory());
            } catch (IOException e) {
                e.printStackTrace();//TODO
                return false;
            }
        });
    }

    /**
     * Geeft een List terug van alle files in de gegeven directory.
     * @return List<Path>
     * @throws IOException
     */
    public List<Path> read() throws IOException {
        return getFileStream().collect(Collectors.toList());
    }

    /**
     * Geeft een List terug van alle files met de opgegeven extensie.
     * @param extension De extensie die de files moeten hebben.
     * @return
     * @throws IOException
     */
    public List<Path> read(String extension) throws IOException {
        return getFileStream().filter(path -> path.toString().toLowerCase().endsWith(extension.toLowerCase()))
                .collect(Collectors.toList());
    }
}
