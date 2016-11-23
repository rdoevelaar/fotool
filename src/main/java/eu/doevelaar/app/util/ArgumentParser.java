package eu.doevelaar.app.util;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility-klasse om programma-argumenten van de commandline te parsen.
 */
public class ArgumentParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentParser.class);
    private Options options;

    public ArgumentParser() {
        this.options = new Options();
        Option directory = new Option("d","directory", true, "Input directory (volledige pad incl root)");
        directory.setRequired(true);
        options.addOption(directory);

        Option extension = new Option("e","extensie", true, "Extensie van de te verwerken bestanden");
        options.addOption(extension);

        Option naamPrefix = new Option("n","naamprefix", true, "Nieuwe naam (vaste deel) van de bestanden");
        options.addOption(naamPrefix);

        Option showhelp = new Option("?", "Toon help voor gebruik");
        options.addOption(showhelp);
    }

    public CommandLine parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.error("Fout bij parsen commandline args: {}", e);
            formatter.printHelp("Fotool", options, true);
            return cmd;
        }
        return cmd;
    }

    /**
     * Geef Options. Nodig voor tonen Help.
     * @return Options
     */
    public Options getOptions() {
        return options;
    }

}
