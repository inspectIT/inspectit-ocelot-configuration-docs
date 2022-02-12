package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.config.doc.generator.docobjects.ConfigDocumentation;
import inspectit.ocelot.config.doc.generator.docobjects.DocObjectGenerator;
import inspectit.ocelot.config.doc.generator.parsing.ConfigParser;
import inspectit.ocelot.configuration.docs.generators.DocHTMLGenerator;
import inspectit.ocelot.configuration.docs.generators.DocMarkdownGenerator;
import inspectit.ocelot.configuration.docs.yaml.utility.YamlProcessor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import rocks.inspectit.ocelot.config.model.InspectitConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Slf4j
@Data
public class DocGenerator {

    ConfigParser configParser = new ConfigParser();
    YamlProcessor yamlProcessor = new YamlProcessor();
    DocHTMLGenerator docHTMLGenerator = new DocHTMLGenerator();
    DocObjectGenerator docObjectGenerator = new DocObjectGenerator();
    DocMarkdownGenerator docMarkdownGenerator = new DocMarkdownGenerator();

    public InspectitConfig parseConfig(String baseDirectory){
        String configYaml = yamlProcessor.mergeConfigYamls(baseDirectory);
        return configParser.parseConfig(configYaml);
    }

    public ConfigDocumentation generateConfigDocumentation(InspectitConfig config){
        return docObjectGenerator.generateConfigDocumentation(config);
    }

    public String generateHTML(ConfigDocumentation configDocumentation){
        return docHTMLGenerator.generateHTMLDoc(configDocumentation);
    }

    public String generateMarkdown(ConfigDocumentation configDocumentation){
        return docMarkdownGenerator.generateMarkdown(configDocumentation);
    }

    public void saveToFile(String content, String targetDirectory, String fileName){
        try(PrintWriter writer = new PrintWriter(Paths.get(targetDirectory, fileName).toString(), StandardCharsets.UTF_8);){
            writer.println(content);
        } catch(IOException e){
            log.error("Error while writing HTML file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        DocGenerator docGenerator = new DocGenerator();
        InspectitConfig inspectitConfig = docGenerator.parseConfig(
                "C:\\Users\\awi\\Documents\\GitHub\\inspectit-ocelot-configuration-docs\\" +
                        "inspectit-ocelot\\inspectit-ocelot-config\\src\\main\\resources\\rocks\\inspectit\\" +
                        "ocelot\\config\\default");
        ConfigDocumentation configDocumentation = docGenerator.generateConfigDocumentation(inspectitConfig);

        String html = docGenerator.generateHTML(configDocumentation);
        docGenerator.saveToFile(html, "C:\\Users\\awi\\Documents\\GitHub\\" +
                "inspectit-ocelot-configuration-docs\\inspectit-ocelot-configuration-docs\\" +
                "inspectit-ocelot-config-docs\\src\\main\\resources\\", "configDoc.html");

        String markdown = docGenerator.generateMarkdown(configDocumentation);
        docGenerator.saveToFile(markdown, "C:\\Users\\awi\\Documents\\GitHub\\" +
                "inspectit-ocelot-configuration-docs\\inspectit-ocelot-configuration-docs\\" +
                "inspectit-ocelot-config-docs\\src\\main\\resources\\", "configDoc.md");

        int i = 1;
    }
}
