package inspectit.ocelot.configuration.docs.docobjects;

import java.util.Map;

public class RuleTracingDoc {

    public RuleTracingDoc(Boolean startSpan, Map<String, String> startSpanConditions, Map<String, String> attributes) {
        this.startSpan = startSpan;
        this.startSpanConditions = startSpanConditions;
        this.attributes = attributes;
    }

    Boolean startSpan;
    Map<String, String> startSpanConditions;
    Map<String, String> attributes;

}
