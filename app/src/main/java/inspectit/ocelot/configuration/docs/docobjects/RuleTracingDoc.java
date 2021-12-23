package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;

import java.util.Map;

import static j2html.TagCreator.*;
import static j2html.TagCreator.dd;

public class RuleTracingDoc {

    public RuleTracingDoc(Boolean startSpan, Map<String, String> startSpanConditions, Map<String, String> attributes) {
        this.startSpan = startSpan;
        this.startSpanConditions = startSpanConditions;
        this.attributes = attributes;
    }

    Boolean startSpan;
    Map<String, String> startSpanConditions;
    Map<String, String> attributes;

    Tag tracingDocHtml(){
        return div(
                dt(h4("Tracing:")),
                dd(String.format("start-span: %s", startSpan)),
                dd(BaseDoc.listKeyValueHtml("Start-Span-Condtions", startSpanConditions)),
                dd(BaseDoc.listKeyValueHtml("Attributes", attributes))
        );
    }

}
