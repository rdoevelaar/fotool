package eu.doevelaar.app;

import eu.doevelaar.app.util.DirReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Fotool {
    /**
     * Hernoemen van foto's in een directory op basis van EXIF data.
     *
     * arg0 Te scannen directory.
     * arg1 Nieuwe naam van het bestand
     * arg2 Extensie van de te hernoemen bestanden (optioneel, default '.jpg')
     *
     * Helpful ;-) : https://drewnoakes.com/code/exif/
     *
     * @param args Programma argumenten, zie beschrijving hierboven.
     */
    public static void main(String[] args) {
        String naamPrefix = "DefaultPrefix";
        String extension  = ".jpg";
        Integer counter = 1;

        if (args.length == 0) {
            throw new IllegalArgumentException("Geen directory opgegeven!");
        }
        String dir = args[0];
        if (args.length > 1) {
            naamPrefix = args[1];
        }

        List<Foto> sortedFotos = createSortedList(Paths.get(dir), extension);

        for (Foto f : sortedFotos) {
            f.setFileName(naamPrefix + counter++ + f.getExtension());
            f.showRename();
        }
    }

    /**
     * Leest bestanden in vanuit gegeven directory en maakt hiervan een gesorteerde lijst van
     * <code>Foto</code>'s. Optioneel kan een extensie opgegeven worden zodat alleen bestanden
     * met deze extensie worden ingelezen.
     *
     * @param pathToScan Path-object dat verwijst naar in te lezen directory.
     * @return Gesorteerde lijst van Foto-objecten. Dit kan ook een lege lijst zijn.
     */
    private static List<Foto> createSortedList(Path pathToScan, String extension) {
        List<Foto> fotolijst = new ArrayList<>();
        try {
            List<Path> bestandslijst = new DirReader(pathToScan).read(extension);

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
