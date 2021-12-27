package inspectit.ocelot.configuration.docs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import rocks.inspectit.ocelot.config.model.InspectitConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
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
public class DocConfigParser {

    /**
     * Predicate to check if a given file path ends with .yml or .yaml
     */
    private static final Predicate<String> HAS_YAML_ENDING = filePath -> filePath.toLowerCase()
            .endsWith(".yml") || filePath.toLowerCase().endsWith(".yaml");

    public InspectitConfig parseConfigFromYamls(List<String> allYamlFiles){

        String inputString = loadAndMergeMultiple(allYamlFiles);

        String cleanedInputString = replacePlaceholders(inputString);

        //Parse the InspectitConfig from the created YAML String
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //Add Module to deal with non-standard Duration values in the YAML
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new CustomDurationDeserializer());
        mapper.registerModule(module);
        //In the YAML property-names are kebab-case in the java objects CamelCase, Jackson can do that conversion
        //with the following line
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        ObjectReader reader = mapper.reader().withRootName("inspectit").forType(InspectitConfig.class);
        try {
            return reader.readValue(cleanedInputString);
        } catch (IOException e) {
            log.error("YAML String could not be parsed by Jackson. Probably an error in the configuration files.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates an InspectitConfig based on the information of all YAML Files found in the given directory and all its
     * subdirectories
     * @param absoluteDirectoryPath absolute path to the directory.
     * @throws IOException
     */
    public InspectitConfig parseConfigFromBaseDirectory(String absoluteDirectoryPath){

        Path absoluteBasePath = Paths.get(absoluteDirectoryPath);

        List<String> allYamlFiles = getAllYamlFiles(absoluteBasePath);

        return parseConfigFromYamls(allYamlFiles);
    }

    /**
     * Finds all YAML Files in the given directory and its subdirectories and returns their locations in a
     * List of Strings.
     * @param absoluteDirectoryPath
     * @return
     */
    private List<String> getAllYamlFiles(Path absoluteDirectoryPath){

        List<String> allYamlPathStrings = new ArrayList<>();

        try(Stream<Path> stream = Files.walk(absoluteDirectoryPath, Integer.MAX_VALUE)){
            allYamlPathStrings = stream
                    .map(String::valueOf)
                    .filter(HAS_YAML_ENDING)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("Error when accessing given absoluteDirectoryPath.");
            e.printStackTrace();
        }

        return allYamlPathStrings;
    }

    /**
     * Merges all provided YAML Files into one YAML String.
     * @param allYamlFiles Paths to all YAML Files in a List.
     * @return
     */
    public String loadAndMergeMultiple(List<String> allYamlFiles){

        Object result = null;
        for (String path : allYamlFiles) {
            result = loadAndMergeYaml(result, path);
        }

        return result == null ? "" : new Yaml().dump(result);
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

    /**
     * Checks if a File exists and is actually a file and not a directory and then reads it and returns the read content.
     * @param targetPath
     * @return
     * @throws IOException
     */
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
     * Replaces placeholders in the format ${placeholder} with their referenced values or just the keys inside as a
     * String if no value can be found.
     * @param yamlString String in YAML format with placeholders.
     * @return
     */
    private String replacePlaceholders(String yamlString){

        //deserialize YAML to Map to get the placeholders' values from
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try{
            Map yamlMap = mapper.readValue(yamlString, Map.class);

            //find the first occurence of the ${placeholder} syntax
            int index = yamlString.indexOf("${");
            while (index >= 0) {

                //get String within the curly braces of ${placeholder} expression
                String replacementSubstring = yamlString.substring(index + 2);
                replacementSubstring = replacementSubstring.substring(0, replacementSubstring.indexOf("}"));
                log.debug("Index: " + index +", Current replaced Placeholder:" + replacementSubstring);

                //get keys within the placeholder, e.g. "inspectit" and "service-name" from inspectit.service-name
                List<String> keys = Arrays.asList(replacementSubstring.split("\\."));

                //get the value the placeholder references from within the Map based on the YAML
                String newSubstring = getNestedValue(yamlMap, keys);

                //Replace the placeholder with the found value
                yamlString = yamlString.replace("${" + replacementSubstring + "}", newSubstring);

                //get index of next placeholder
                index = yamlString.indexOf("${");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return yamlString;
    }

    /**
     * Get value as String from within nested Maps within the given Map based on the given keys.
     * @param map Map to get value from.
     * @param keys List of keys.
     * @return
     */
    private String getNestedValue(Map map, List<String> keys) {
        Object value = map;
        String old_key = "";

        for (String key : keys) {

            //needs to be casted each time because Java does not know that within the Map there are more Maps
            Object new_value = ((Map) value).get(old_key + key);

            //Some keys themselves contain dots again, which previously were used as split points, e.g. there is a key
            //concurrent.phase.time which points to one boolean value, so if no value is found with one key, it is
            // concatenated with the next one on the next round of the loop.
            if (new_value != null) {
                //If that is not the case, simply replace the old Map with the newly found one.
                value = new_value;
            } else {
                if(keys.get(keys.size() - 1).equals(key)){
                    //if the corresponding value can not be found, return the full key.
                    // This is a workaround for, as of now, only environment variables, so it should be fine for the
                    // Documentation but would not be for any actually running agents.
                    return old_key + key;
                } else {
                    old_key = old_key + key + ".";
                }
            }
        }

        return value.toString();
    }

    /**
     * Returns a Duration corresponding to the given value in a String in humanly readable format, e.g. 2h3m20s40ms.
     * Needed for deserializing the YAML using Jackson.
     * @param text
     * @return
     */
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

    /**
     * The CustomDurationDeserializer is needed to parse the Duration values in the YAML files using Jackson.
     */
    public static class CustomDurationDeserializer extends JsonDeserializer<Duration> {

        @Override
        public Duration deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            //return parseHuman(parser.getText());\
            return Duration.ofMillis(50);
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
}
