package eu.doevelaar.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class DirReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(DirReader.class);
    private static final int NO_SUBDIRS = 1;

    /**
     * Geeft een Stream terug van de opgegeven directory, zonder subdirectories.
     * @param dir Directory waarvan de Stream moet worden geopend.
     * @return Stream van de opgegeven directory.
     * @throws IOException
     */
    private static Stream<Path> getFileStream(Path dir) throws IOException {
        return Files.walk(dir, NO_SUBDIRS).filter(path -> {
            try {
                return !(Files.readAttributes(path, BasicFileAttributes.class).isDirectory());
            } catch (IOException e) {
                LOGGER.error("Fout bij lezen BasicFileAttributes van Path {}", path);
                return false;
            }
        });
    }

    /**
     * Geeft een List terug van alle files in de gegeven directory.
     * @param dir Directory waarvan de bestanden moeten worden gelezen.
     * @return List<Path>
     * @throws IOException
     */
    public static List<Path> read(Path dir) throws IOException {
        List<Path> list = getFileStream(dir).collect(Collectors.toList());
        LOGGER.debug("Lezen directory '{}'. Aantal gelezen bestanden : {}", dir,  list.size());
        return list;
    }

    /**
     * Geeft een List terug van alle files met de opgegeven extensie.
     * @param dir Directory waarvan de bestanden moeten worden gelezen.
     * @param extension De extensie die de files moeten hebben.
     * @return
     * @throws IOException
     */
    public static List<Path> read(Path dir, String extension) throws IOException {
        List<Path> list = getFileStream(dir).filter(path -> path.toString().toLowerCase().endsWith(extension.toLowerCase()))
                .collect(Collectors.toList());
        LOGGER.debug("Lezen directory '{}'. Aantal bestanden met extensie '{}' ingelezen: {}", dir, extension, list.size());
        return list;
    }
}
