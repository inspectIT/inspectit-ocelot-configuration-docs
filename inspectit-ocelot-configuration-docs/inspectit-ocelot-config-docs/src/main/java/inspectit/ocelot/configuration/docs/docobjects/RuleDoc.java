package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;

import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import static j2html.TagCreator.dl;

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

    Tag includeListHtml(){
        if(include.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Include:")),
                    each(include, inc ->
                            dd(
                                    a(inc).withHref(String.format("#%s", inc))
                            )
                    )
            );
        }
    }

    Tag scopeListHtml(){
        if(scopes.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Scopes:")),
                    each(scopes, scope ->
                            dd(
                                    a(scope).withHref(String.format("#%s", scope))
                            )
                    )
            );
        }
    }

    Tag tracingHtml(){
        if(tracingDoc!=null){
            return tracingDoc.tracingDocHtml();
        } else {
            return null;
        }
    }

    Tag entryExitHtml(){
        if(entryExits.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Exit/Entry:")),
                    dd(each(entryExits.keySet(), entryExitKey ->
                            dl(
                                    dt(strong(String.format("%s:", entryExitKey))),
                                    dd(dl(
                                            dt("Attributes:"),
                                            each(entryExits.get(entryExitKey), RuleActionCallDoc::actionCallDocHtml)
                                            )
                                    )
                            ))
                    )
            );
        }
    }

    Tag metricsHtml(){
        if (metricsDocs.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Metrics:")),
                    each(metricsDocs, RuleMetricsDoc::metricHtml)
            );
        }
    }

    @Override
    Tag specificPartialHTML() {
        return div(
                attrs(".doc-element-content"),
                p(description),
                dl(
                        includeListHtml(),
                        scopeListHtml(),
                        tracingHtml(),
                        entryExitHtml(),
                        metricsHtml()
                )
        );
    }

    @Override
    String toJSON() {
        return null;
    }

}
