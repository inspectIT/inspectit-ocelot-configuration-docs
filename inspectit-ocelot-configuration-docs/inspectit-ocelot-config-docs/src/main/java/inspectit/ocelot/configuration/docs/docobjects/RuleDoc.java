package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static j2html.TagCreator.*;
import static j2html.TagCreator.dl;

@Data
public class RuleDoc extends BaseDoc {

    public RuleDoc(String name, String description, List<String> include, List<String> scopes, List<RuleMetricsDoc> metricsDocs, RuleTracingDoc tracingDoc, Map<String, List<RuleActionCallDoc>> entryExits) {
        super(name, description);
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

    public void addEntryExitFromIncludedRules(Map<String, RuleDoc> allRuleDocs){

        for(String includedRuleName: include){

            RuleDoc includedRule = allRuleDocs.get(includedRuleName);
            Map<String, List<RuleActionCallDoc>> includedRuleEntryExits = includedRule.getEntryExits();

            for(String includedRuleEntryExitKey: includedRuleEntryExits.keySet()){

                if(!entryExits.containsKey(includedRuleEntryExitKey)){
                    entryExits.put(includedRuleEntryExitKey, new ArrayList<>());
                }
                for(RuleActionCallDoc entryExitActionCall: includedRuleEntryExits.get(includedRuleEntryExitKey)){
                    entryExits.get(includedRuleEntryExitKey).add(
                            new RuleActionCallDoc(entryExitActionCall, includedRule.getName())
                    );
                }
            }
        }
    }

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
                                    each(entryExits.get(entryExitKey), RuleActionCallDoc::actionCallDocHtml)
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
        return dl(
                includeListHtml(),
                scopeListHtml(),
                tracingHtml(),
                entryExitHtml(),
                metricsHtml()
        );
    }

}
