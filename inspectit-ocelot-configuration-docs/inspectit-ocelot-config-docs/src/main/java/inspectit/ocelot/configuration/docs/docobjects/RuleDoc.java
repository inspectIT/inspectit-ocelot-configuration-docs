package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import lombok.Getter;

import java.util.*;

import static j2html.TagCreator.*;
import static j2html.TagCreator.dl;

@Getter
public class RuleDoc extends BaseDoc {

    public RuleDoc(String name, String description, List<String> include, List<String> scopes,
                   List<RuleMetricsDoc> metricsDocs, RuleTracingDoc tracingDoc, Map<String, Map<String,
            RuleActionCallDoc>> entryExits) {
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
    Map<String, Map<String, RuleActionCallDoc>> entryExits;

    public void addEntryExitFromIncludedRules(Map<String, RuleDoc> allRuleDocs, List<String> includedRules){

        for(String includedRuleName: includedRules){

            RuleDoc includedRule = allRuleDocs.get(includedRuleName);
            Map<String, Map<String, RuleActionCallDoc>> includedRuleEntryExits = includedRule.getEntryExits();

            for(String includedRuleEntryExitKey: includedRuleEntryExits.keySet()){

                if(!entryExits.containsKey(includedRuleEntryExitKey)){
                    entryExits.put(includedRuleEntryExitKey, new TreeMap<>());
                }
                for(RuleActionCallDoc entryExitActionCall: includedRuleEntryExits.get(includedRuleEntryExitKey).values()){

                    Map<String, RuleActionCallDoc> actionCallDocs = entryExits.get(includedRuleEntryExitKey);

                    if(!actionCallDocs.containsKey(entryExitActionCall.getName())) {
                        actionCallDocs.put(entryExitActionCall.getName(), new RuleActionCallDoc(entryExitActionCall,
                                includedRule.getName()));
                    }
                }
            }
            addEntryExitFromIncludedRules(allRuleDocs, includedRule.getInclude());
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

        for(String key: entryExits.keySet()){
            System.out.println(key);
            System.out.println(entryExits.get(key).getClass());
            for(RuleActionCallDoc doc: entryExits.get(key).values()){
                System.out.println(doc.getName());
            }
        }

        if(entryExits.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Exit/Entry:")),
                    dd(each(entryExits.keySet(), entryExitKey ->
                            dl(
                                    dt(strong(String.format("%s:", entryExitKey))),
                                    each(entryExits.get(entryExitKey).values(), RuleActionCallDoc::actionCallDocHtml)
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
