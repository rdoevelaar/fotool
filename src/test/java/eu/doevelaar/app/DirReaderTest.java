package eu.doevelaar.app;

import eu.doevelaar.app.fileutils.DirReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


/**
 * Created by doevel.r on 18-11-2016.
 */
public class DirReaderTest {

    //TODO: Dit slaat als test natuurlijk nergens op. Iets beters verzinnen...
//    @Test
    public void readerTest() throws IOException {
        Path path = Paths.get("E:/tmp");
        DirReader dr = new DirReader(path);
        List<Path> list = dr.read();
        System.out.println(list.size());
    }
}
