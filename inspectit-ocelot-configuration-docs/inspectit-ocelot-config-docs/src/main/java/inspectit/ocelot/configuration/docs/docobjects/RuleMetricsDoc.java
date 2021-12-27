package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;

import java.util.Map;

import static j2html.TagCreator.*;
import static j2html.TagCreator.dd;

public class RuleMetricsDoc {

    public RuleMetricsDoc(String name, String value, Map<String, String> dataTags, Map<String, String> constantTags) {
        this.name = name;
        this.value = value;
        this.dataTags = dataTags;
        this.constantTags = constantTags;
    }

    Tag metricHtml(){
        return dd(
                attrs(".metric"),
                strong(name),
                dl(
                        dt("Value:"),
                        dd(value),
                        BaseDoc.listKeyValueHtml("Constant Tags", constantTags),
                        BaseDoc.listKeyValueHtml("Data Tags", dataTags)
                )
        );
    }

    String name;
    String value;
    Map<String, String> dataTags;
    Map<String, String> constantTags;

}
