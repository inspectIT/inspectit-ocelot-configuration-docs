package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.configuration.docs.docobjects.BaseDoc;
import lombok.extern.slf4j.Slf4j;
import rocks.inspectit.ocelot.config.model.InspectitConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class DocGenerator {

    InspectitConfig config;
    List<BaseDoc> docObjects;
    String html;

    public void parseConfig(String baseDirectory){
        DocConfigParser configParser = new DocConfigParser();
        config = configParser.parseConfigFromBaseDirectory(baseDirectory);
    }

    public void generateDocObjects(){
        DocObjectGenerator docObjectGenerator = new DocObjectGenerator();
        docObjects = docObjectGenerator.generateDocObjects(config);
    }

    public void generateHTML(){
        DocHTMLGenerator docHTMLGenerator = new DocHTMLGenerator();
        html = docHTMLGenerator.generateHTMLDoc(docObjects);
    }

    public void saveHTML(String targetDirectory){
        try(PrintWriter writer = new PrintWriter(Paths.get(targetDirectory, "configDoc.html").toString(), StandardCharsets.UTF_8);){
            writer.println(html);
        } catch(IOException e){
            log.error("Error while writing HTML file.");
            e.printStackTrace();
        }
    }
}
