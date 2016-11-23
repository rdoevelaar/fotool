package eu.doevelaar.app;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * Klasse voor een foto-bestand.
 * De klasse bevat diverse methoden om metadata op te halen of in te stellen.
 */
public class Foto {
    private static final Logger LOGGER = LoggerFactory.getLogger(Foto.class);

    private Path path, newPath;
    private final String extension;
    private Metadata metadata;

    public Foto(Path path) {
        this.path = path;
        String filenaam = path.getFileName().toString();
        this.extension = filenaam.substring(filenaam.lastIndexOf('.'));

        try {
            this.metadata = ImageMetadataReader.readMetadata(path.toFile());
        } catch (ImageProcessingException | IOException e) {
            LOGGER.error("Fout bij lezen Foto: {}", e);
        }

    }

    /**
     * Geef de bestandsnaam van de Foto terug.
     * @return Bestandsnaam van de Foto.
     */
    public String getFileName() {
        return String.valueOf(path.getFileName());
    }

    /**
     * Stel nieuwe bestandsnaam samen, behoud originele extensie.
     * @param newName De nieuw in te stellen naam, exclusief extensie.
     */
    public void setFileName(String newName) {
        if (newName == null || newName.equals("")) {
            throw new IllegalArgumentException("Geen geldige nieuwe naam opgegeven!");
        }
        if (!Files.isWritable(this.path)) {
            LOGGER.error("Kan niet schrijven naar bestand '{}'", this.path.getFileName());
            throw new RuntimeException("Kan niet schrijven naar bestand " + this.path.getFileName());
        }

        newPath = this.path.resolveSibling(newName);
//        try {
//            Files.move(this.path, newPath);
//        } catch (IOException e) {
//            System.out.println("Fout bij hernoemen van Foto " + this.path + ": " + e);
//        }
    }

    public void showRename() {
        LOGGER.info(String.format("%30s ==> %30s",
                path.getFileName().toString(),
                newPath.getFileName().toString()));
    }

    public String getExtension() {
        return this.extension;
    }

    /**
     * Geef de datumtijd waarop de foto gemaakt is volgens de EXIF-tags.
     * @return Waarde van EXIF-tag met creatie datumtijd.
     */
    public Date getCreationDatetime() {
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            return directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
    }

    /**
     * Print the metadata to System.out
     */
    public void printMetadata() {
        System.out.println("-------------------------------------");
        System.out.println(this.getFileName());
        System.out.println("-------------------------------------");

        // A Metadata object contains multiple Directory objects
        //
        for (Directory directory : this.metadata.getDirectories()) {

            // Each Directory stores values in Tag objects
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }

            // Each Directory may also contain error messages
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.println("ERROR: " + error);
                }
            }
        }
    }

}
