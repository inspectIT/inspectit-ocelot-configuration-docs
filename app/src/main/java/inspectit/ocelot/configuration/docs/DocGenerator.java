package inspectit.ocelot.configuration.docs;

import lombok.extern.slf4j.Slf4j;
import rocks.inspectit.ocelot.config.model.InspectitConfig;
import java.io.*;

@Slf4j
public class DocGenerator {

    InspectitConfig config;

    public void parseConfig(String baseDirectory){
        DocConfigParser configParser = new DocConfigParser();
        config = configParser.parseConfigFromBaseDirectory(baseDirectory);
    }

}
