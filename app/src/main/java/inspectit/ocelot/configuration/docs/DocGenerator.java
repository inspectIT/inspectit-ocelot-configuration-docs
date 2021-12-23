package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.configuration.docs.docobjects.BaseDoc;
import lombok.extern.slf4j.Slf4j;
import rocks.inspectit.ocelot.config.model.InspectitConfig;

import java.io.*;
import java.util.List;

@Slf4j
public class DocGenerator {

    InspectitConfig config;
    List<BaseDoc> docObjects;

    public void parseConfig(String baseDirectory){
        DocConfigParser configParser = new DocConfigParser();
        config = configParser.parseConfigFromBaseDirectory(baseDirectory);
    }

    public void generateDocObjects(){
        DocObjectGenerator docObjectGenerator = new DocObjectGenerator();
        docObjects = docObjectGenerator.generateDocObjects(config);
    }

}
