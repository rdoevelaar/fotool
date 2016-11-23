package eu.doevelaar.app;

import eu.doevelaar.app.util.DirReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Uitvragen van EXIF data van een directory met foto's.
 *
 * arg0 Te scannen directory.
 * arg1 Nieuwe naam van het bestand
 *
 * Helpful ;-) : https://drewnoakes.com/code/exif/
 *
 */
public class Fotool {

    public static void main(String[] args) {
        String naamPrefix = "DefaultPrefix";
        Integer counter = 1;

        if (args.length == 0) {
            throw new IllegalArgumentException("Geen directory opgegeven!");
        }
        String dir = args[0];
        if (args.length > 1) {
            naamPrefix = args[1];
        }
        List<Foto> sortedFotos = createSortedList(Paths.get(dir));
        for (Foto f : sortedFotos) {
            f.setFileName(naamPrefix + counter++ + f.getExtension());
            f.showRename();
        }
    }

    private static List<Foto> createSortedList(Path pathToScan) {
        DirReader dr = new DirReader(pathToScan);
        List<Foto> fotolijst = new ArrayList<>();
        try {
            List<Path> bestandslijst = dr.read(".jpg");

            // Nu hebben we een lijst van alle bestanden. Deze willen we sorteren op basis van hun
            // aanmaak-datumtijd. Oplopend.
            for (Path b : bestandslijst) {
                fotolijst.add(new Foto(b));
            }

            Comparator<Foto> creationDateComparator = (f1, f2) -> f1.getCreationDatetime().compareTo(f2.getCreationDatetime());
            fotolijst.sort(creationDateComparator);
//            for (Foto f : fotolijst) {
//                System.out.println(f.getCreationDatetime() + " " + f.getFileName());
//                f.printMetadata();
//            }

        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }
        return fotolijst;
    }
}
