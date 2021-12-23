/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package inspectit.ocelot.configuration.docs;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import rocks.inspectit.ocelot.config.model.instrumentation.actions.GenericActionSettings;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) throws IOException {
        DocGenerator docGenerator = new DocGenerator();
        docGenerator.parseConfig(
                "C:\\Users\\awi\\Documents\\GitHub\\inspectit-ocelot-configuration-docs\\" +
                        "inspectit_ocelot_repo\\inspectit-ocelot-config\\src\\main\\resources\\rocks\\inspectit\\" +
                        "ocelot\\config\\default");

        docGenerator.generateDocObjects();
        docGenerator.generateHTML();
        docGenerator.saveHTML("C:\\Users\\awi\\Documents\\GitHub\\" +
                "inspectit-ocelot-configuration-docs\\app\\src\\main\\resources\\");

        int i = 1;
    }
}
