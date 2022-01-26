package inspectit.ocelot.configuration.docs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rocks.inspectit.ocelot.config.model.InspectitConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Slf4j
@Data
public class DocGenerator {

    InspectitConfig config;
    FullDoc fullDoc;
    String html;
    String markdown;

    public void parseConfig(String baseDirectory){
        DocConfigParser configParser = new DocConfigParser();
        config = configParser.parseConfigFromBaseDirectory(baseDirectory);
    }

    public void generateFullDoc(){
        DocObjectGenerator docObjectGenerator = new DocObjectGenerator();
        fullDoc = docObjectGenerator.generateFullDocObject(config);
    }

    public void generateHTML(){
        DocHTMLGenerator docHTMLGenerator = new DocHTMLGenerator();
        html = docHTMLGenerator.generateHTMLDoc(fullDoc);
    }

    public void generateMardown(){
        DocMarkdownGenerator docMarkdownGenerator = new DocMarkdownGenerator();
        markdown = docMarkdownGenerator.generateMarkdown(fullDoc);
    }

    public void saveHTML(String targetDirectory){
        try(PrintWriter writer = new PrintWriter(Paths.get(targetDirectory, "configDoc.html").toString(), StandardCharsets.UTF_8);){
            writer.println(html);
        } catch(IOException e){
            log.error("Error while writing HTML file.");
            e.printStackTrace();
        }
    }

    public void saveMD(String targetDirectory){
        try(PrintWriter writer = new PrintWriter(Paths.get(targetDirectory, "configDoc.md").toString(), StandardCharsets.UTF_8);){
            writer.println(markdown);
        } catch(IOException e){
            log.error("Error while writing markdown file.");
            e.printStackTrace();
        }
    }
}
