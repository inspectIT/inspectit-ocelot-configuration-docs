package inspectit.ocelot.configuration.docs.docobjects;

import java.util.Map;

public class RuleMetricsDoc {

    public RuleMetricsDoc(String name, Map<String, String> dataTags, Map<String, String> constantTags) {
        this.name = name;
        this.dataTags = dataTags;
        this.constantTags = constantTags;
    }

    String name;
    Map<String, String> dataTags;
    Map<String, String> constantTags;

}
