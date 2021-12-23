package inspectit.ocelot.configuration.docs.docobjects;

import inspectit.ocelot.configuration.docs.docobjects.BaseDoc;
import j2html.tags.Tag;

import java.util.List;
import java.util.Map;

public class RuleDoc extends BaseDoc {

    public RuleDoc(String description, String name, List<String> include, List<String> scopes,
                   List<RuleMetricsDoc> metricsDocs, RuleTracingDoc tracingDoc,
                   Map<String, List<RuleActionCallDoc>> entryExits) {
        super(description, name, DocType.RULE);
        this.include = include;
        this.scopes = scopes;
        this.metricsDocs = metricsDocs;
        this.tracingDoc = tracingDoc;
        this.entryExits = entryExits;
    }

    List<String> include;
    List<String> scopes;
    List<RuleMetricsDoc> metricsDocs;
    RuleTracingDoc tracingDoc;
    Map<String, List<RuleActionCallDoc>> entryExits;

    @Override
    Tag specificPartialHTML() {
        return null;
    }

    @Override
    String toJSON() {
        return null;
    }

}
