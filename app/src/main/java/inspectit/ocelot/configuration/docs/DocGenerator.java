package inspectit.ocelot.configuration.docs;


import ch.qos.logback.core.util.FileUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import rocks.inspectit.ocelot.config.model.InspectitConfig;
import rocks.inspectit.ocelot.config.model.instrumentation.InstrumentationSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.actions.GenericActionSettings;

import javax.swing.text.StyledEditorKit;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class DocGenerator {


    public DocGenerator(String absoluteBasePath) {
        if (absoluteBasePath.startsWith("/")) {
            this.absoluteBasePath = Paths.get(absoluteBasePath.substring(1));
        } else {
            this.absoluteBasePath = Paths.get(absoluteBasePath);
        }
    }

    Path absoluteBasePath;

    List<InspectitConfig> intermediateConfigs = new ArrayList<>();

    private void readSingleYaml(String path){

    }

    private void readAllYaml(String startFolder){

    }

    public void fileAccessTest(){


        /*BufferedReader reader = new BufferedReader(streamReader);

        String line;
        while(true) {
            try {
                if (((line = reader.readLine()) != null)){
                    System.out.println(line);
                };
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }

    /*private InputStream getInputStreamWithoutFirstLine() throws IOException {
        InputStream inputStream = new FileInputStream(Paths.(absoluteBasePath, "default",
                "instrumentation", "actions", "_shared", "assignment.yml").toString());

        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        String temp = reader.readLine();
        //log.
        return inputStream;
    }*/

    private List<String> getAllYamlFiles(){

        List<String> allYamlPathStrings = new ArrayList<>();

        try(Stream<Path> stream = Files.walk(absoluteBasePath, Integer.MAX_VALUE)){
            allYamlPathStrings = stream
                    .map(String::valueOf)
                    .filter(HAS_YAML_ENDING)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allYamlPathStrings;
    }

    /**
     * Predicate to check if a given file path ends with .yml or .yaml
     */
    private static final Predicate<String> HAS_YAML_ENDING = filePath -> filePath.toLowerCase()
            .endsWith(".yml") || filePath.toLowerCase().endsWith(".yaml");

    public String loadAndMergeAllYamls(){
        List<String> allYamlFiles = getAllYamlFiles();

        Object result = null;
        for (String path : allYamlFiles) {
            result = loadAndMergeYaml(result, path);
        }

        //Map onlyInstrumentation = new LinkedHashMap();
        //onlyInstrumentation.put("instrumentation", result.get("inspectit").get("instrumentation"));

        return result == null ? "" : new Yaml().dump(result);
    }

    /**
     * Reads the file content of the specified configuration file. In case the file does not exist or cannot be read,
     * the resulting {@link Optional} will be empty.
     *
     * @param file the configuration file to read
     *
     * @return the content of the specified file
     */
    public Optional<String> readConfigurationFile(String file) {
        log.debug("Reading configuration file: {}", file);

        Path targetPath = Paths.get(file);

        try {
            byte[] rawFileContent = readFile(targetPath);
            String fileContent = new String(rawFileContent, StandardCharsets.UTF_8);
            return Optional.of(fileContent);
        } catch (Exception ex) {
            log.error("File '{}' could not been loaded.", ex);
            return Optional.empty();
        }
    }

    protected byte[] readFile(Path targetPath) throws IOException {
        if (!Files.exists(targetPath)) {
            throw new FileNotFoundException("File '" + targetPath + "' does not exist.");
        }

        if (Files.isDirectory(targetPath)) {
            throw new IllegalArgumentException("The specified '" + targetPath + "' is not a file but directory.");
        }

        return Files.readAllBytes(targetPath);
    }

    /**
     * Loads a yaml file as a Map/List structure and merges it with an existing map/list structure
     *
     * @param toMerge the existing structure of nested maps / lists with which the loaded yaml will be merged.
     * @param path    the path of the yaml file to load
     *
     * @return the merged structure
     */
    private Object loadAndMergeYaml(Object toMerge, String path) {
        Yaml yaml = new Yaml();
        String src = readConfigurationFile(path).orElse("");

        try {
            Object loadedYaml = yaml.load(src);
            if (toMerge == null) {
                return loadedYaml;
            } else {
                return ObjectStructureMerger.merge(toMerge, loadedYaml);
            }
        } catch (Exception e) {
            throw new InvalidConfigurationFileException(path, e);
        }
    }

    /**
     * This exception will be thrown if a configuration file cannot be parsed, e.g. it contains invalid characters.
     */
    static class InvalidConfigurationFileException extends RuntimeException {

        public InvalidConfigurationFileException(String path, Exception e) {
            super(String.format("The configuration file '%s' is invalid and cannot be parsed.", path), e);
        }
    }

    public void jacksonTest() throws IOException {

        String inputString = loadAndMergeAllYamls();
        String cleanedInputString = replacePlaceholders(inputString);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new CustomDurationDeserializer());
        mapper.registerModule(module);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

        ObjectReader reader = mapper.reader().withRootName("inspectit").forType(InspectitConfig.class);
        InspectitConfig config = reader.readValue(cleanedInputString);

        int a = 1;
    }

    private String replacePlaceholders(String yamlString) throws JsonProcessingException {

        //deserialize to Map to get the placeholders' values from
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map yamlMap = mapper.readValue(yamlString, Map.class);

        int index = yamlString.indexOf("${");
        while (index >= 0) {

            //get String within the curly braces of ${placeholder} expression
            String replacementSubstring = yamlString.substring(index + 2);
            replacementSubstring = replacementSubstring.substring(0, replacementSubstring.indexOf("}"));
            log.debug("Index: " + index +", Current replaced Placeholder:" + replacementSubstring);

            //get keys within the placeholder, e.g. "inspectit" and "service-name" from inspectit.service-name
            List<String> keys = Arrays.asList(replacementSubstring.split("\\."));

            String newSubstring = getNestedValue(yamlMap, keys);

            yamlString = yamlString.replace("${" + replacementSubstring + "}", newSubstring);
            index = yamlString.indexOf("${");
        }

        return yamlString;
    }

    private String getNestedValue(Map map, List<String> keys) {
        Object value = map;
        String old_key = "";

        for (String key : keys) {
            Object new_value = ((Map) value).get(old_key + key);
            if (new_value != null) {
                value = new_value;
            } else {
                if(keys.get(keys.size()-1)==key){
                    //if the corresponding value can not be found, return empty String
                    // TODO: 17.12.2021 Not ideal, because it breaks some entries for environmental(?) variables
                    //  in inspectit.config.http, but it should be fine for the documentation purpose despite this flaw
                    return "";
                } else {
                    old_key = old_key + key + ".";
                }
            }
        }

        return value.toString();
    }

    public static Duration parseHuman(String text) {
        Matcher m = Pattern.compile("\\s*(?:(\\d+)\\s*(?:hours?|hrs?|h))?" +
                        "\\s*(?:(\\d+)\\s*(?:minutes?|mins?|m))?" +
                        "\\s*(?:(\\d+)\\s*(?:seconds?|secs?|s))?" +
                        "\\s*(?:(\\d+)\\s*(?:milliseconds?|ms))?" +
                        "\\s*", Pattern.CASE_INSENSITIVE)
                .matcher(text);
        if (! m.matches())
            throw new IllegalArgumentException("Not valid duration: " + text);
        int hours = (m.start(1) == -1 ? 0 : Integer.parseInt(m.group(1)));
        int mins  = (m.start(2) == -1 ? 0 : Integer.parseInt(m.group(2)));
        int secs  = (m.start(3) == -1 ? 0 : Integer.parseInt(m.group(3)));
        int ms  = (m.start(4) == -1 ? 0 : Integer.parseInt(m.group(4)));
        return Duration.ofMillis(((hours * 60L + mins) * 60L + secs) * 1000L + ms);
    }

    public class CustomDurationDeserializer extends JsonDeserializer<Duration> {

        @Override
        public Duration deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            //return parseHuman(parser.getText());\
            return Duration.ofMillis(50);
        }
    }
}
