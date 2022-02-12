package inspectit.ocelot.configuration.docs.generators;

import inspectit.ocelot.config.doc.generator.docobjects.*;
import j2html.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

@Slf4j
public class DocHTMLGenerator {

    public String generateHTMLDoc(ConfigDocumentation configDocumentation){
        String cssPath = "configDocStyle.css";
        return baseTemplate(cssPath, configDocumentation).render();
    }

    /* gives Path somewhere in build directory, don't want that
    private String cssAbsolutePath(){
        URL resource = DocHTMLGenerator.class.getClassLoader().getResource("configDocStyle.css");
        try {
            String cssPath = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            return cssPath;
        } catch(URISyntaxException e){
            log.error("CSS file not found.");
            e.printStackTrace();
            return "";
        }
    }*/

    private Tag baseTemplate(String cssPath, ConfigDocumentation configDocumentation){
        return html(
                head(
                        title("inspectIT Ocelot ConfigDoc"),
                        link().withRel("stylesheet").withHref(cssPath)
                ),
                body(
                        h1("inspectIT Ocelot Configuration Documentation"),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Scopes"),
                                each(configDocumentation.getScopes(), this::mainConfigDocPartial)
                        ),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Rules"),
                                each(configDocumentation.getRules(), this::mainConfigDocPartial)
                        ),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Actions"),
                                each(configDocumentation.getActions(), this::mainConfigDocPartial)
                        ),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Metrics"),
                                each(configDocumentation.getMetrics(), this::mainConfigDocPartial)
                        )
                )
        );

    }

    public Tag mainConfigDocPartial(BaseDoc baseDoc){
        return
                div(
                        attrs(".doc-element"),
                        a(attrs("#" + baseDoc.getName())),
                        h3(attrs(".element-heading"), baseDoc.getName()),
                        div(
                                attrs(".doc-element-content"),
                                p(baseDoc.getDescription()),
                                specificPartialHTML(baseDoc)
                        )
                );
    }

    Tag specificPartialHTML(BaseDoc baseDoc) {
        if (baseDoc instanceof ActionDoc){
            return actionHtml((ActionDoc) baseDoc);
        } else if (baseDoc instanceof MetricDoc) {
            return metricHtml((MetricDoc) baseDoc);
        } else if (baseDoc instanceof RuleDoc) {
            return ruleHtml((RuleDoc) baseDoc);
        } else {
            return null;
        }
    }

    public static Tag listKeyValueHtml(String heading, Map<String, String> map){
        if(map.isEmpty()){
            return null;
        } else {
            return dl(
                    dt(String.format("%s:", heading)),
                    each(map.keySet(), key ->
                            dd(String.format("%s: %s", key,
                                    map.get(key)))
                    )
            );
        }
    }

    private Tag actionHtml(ActionDoc actionDoc) {
        return div(
                dl(
                        iff(!actionDoc.getInputs().isEmpty(), div(
                                dt(h4("Inputs:")),
                                each(actionDoc.getInputs(), this::actionInputDocHtml)
                        )),
                        iff(actionDoc.getReturnDescription() != null, div(
                                dt(h4("Return value:")),
                                dd(
                                        p(actionDoc.getReturnDescription())
                                ))
                        )
                )
        );
    }

    Tag actionInputDocHtml(ActionInputDoc inputDoc){
        return dd(
                join(
                        span(attrs(".actionInputType"), inputDoc.getType()),
                        span(attrs(".actionInputName"), inputDoc.getName()), ":",
                        span(attrs(".actionInputDescription"), inputDoc.getDescription())
                )
        );
    }

    Tag metricHtml(MetricDoc metricDoc) {
        return p(String.format("Unit: %s", metricDoc.getUnit()));
    }

    Tag ruleHtml(RuleDoc ruleDoc) {
        return dl(
                listHtml("Includes:", ruleDoc.getInclude()),
                listHtml("Scopes:", ruleDoc.getScopes()),
                ruleTracingDocHtml(ruleDoc.getTracingDoc()),
                entryExitHtml(ruleDoc.getEntryExits()),
                iff(!ruleDoc.getMetricsDocs().isEmpty(),div(
                        dt(h4("Metrics:")),
                        each(ruleDoc.getMetricsDocs(), this::ruleMetricHtml)
                ))
        );
    }

    Tag listHtml(String title, List<String> entries){
        if(entries.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4(title)),
                    each(entries, entry ->
                            dd(
                                    a(entry).withHref(String.format("#%s", entry))
                            )
                    )
            );
        }
    }

    Tag ruleTracingDocHtml(RuleTracingDoc ruleTracingDoc){
        if(ruleTracingDoc == null) {
            return null;
        } else {
            return div(
                    dt(h4("Tracing:")),
                    dd(String.format("start-span: %s", ruleTracingDoc.getStartSpan())),
                    dd(listKeyValueHtml("Start-Span-Condtions", ruleTracingDoc.getStartSpanConditions())),
                    dd(listKeyValueHtml("Attributes", ruleTracingDoc.getAttributes()))
            );
        }
    }

    Tag entryExitHtml(Map<String, Map<String, RuleActionCallDoc>> entryExits){
        if(entryExits.keySet().stream().filter(key -> (entryExits.get(key).size() > 0)).collect(Collectors.toSet()).isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Attributes:")),
                    dd(each(entryExits.keySet(), entryExitKey ->
                            iff(!entryExits.get(entryExitKey).isEmpty(), dl(
                                    dt(strong(String.format("%s:", entryExitKey))),
                                    each(entryExits.get(entryExitKey).values(), this::actionCallDocHtml)
                            ))
                    ))
            );
        }
    }

    Tag actionCallDocHtml(RuleActionCallDoc ruleActionCallDoc){
        return dd(join(
                String.format("%s:", ruleActionCallDoc.getName()),
                a(ruleActionCallDoc.getAction()).withHref(String.format("#%s", ruleActionCallDoc.getAction())),
                (ruleActionCallDoc.getInheritedFrom() != null) ? join(
                        i("( from included rule"),
                        a(ruleActionCallDoc.getInheritedFrom()).withHref(String.format("#%s", ruleActionCallDoc.getInheritedFrom())),
                        i(")")
                ) : null
        ));
    }

    Tag ruleMetricHtml(RuleMetricsDoc ruleMetricsDoc){
        return dd(
                attrs(".metric"),
                a(strong(ruleMetricsDoc.getName())).withHref(String.format("#%s", ruleMetricsDoc.getName())),
                dl(
                        dt("Value:"),
                        dd(ruleMetricsDoc.getValue()),
                        listKeyValueHtml("Constant Tags", ruleMetricsDoc.getConstantTags()),
                        listKeyValueHtml("Data Tags", ruleMetricsDoc.getDataTags())
                )
        );
    }

}
