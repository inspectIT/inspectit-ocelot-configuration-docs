package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.configuration.docs.docobjects.*;
import io.opencensus.metrics.export.Metric;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import rocks.inspectit.ocelot.config.model.InspectitConfig;
import rocks.inspectit.ocelot.config.model.instrumentation.InstrumentationSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.actions.ActionCallSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.actions.ConditionalActionSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.actions.GenericActionSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.documentation.ActionDocSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.documentation.BaseDocSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.rules.InstrumentationRuleSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.rules.MetricRecordingSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.rules.RuleTracingSettings;
import rocks.inspectit.ocelot.config.model.instrumentation.scope.InstrumentationScopeSettings;
import rocks.inspectit.ocelot.config.model.metrics.MetricsSettings;
import rocks.inspectit.ocelot.config.model.metrics.definition.MetricDefinitionSettings;

import java.lang.reflect.Field;
import java.util.*;

public class DocObjectGenerator {
    
    public FullDoc generateFullDocObject(InspectitConfig config){
        
        InstrumentationSettings instrumentation = config.getInstrumentation();
        Map<String, InstrumentationScopeSettings> scopes = instrumentation.getScopes();
        Map<String, GenericActionSettings> actions = instrumentation.getActions();
        Map<String, InstrumentationRuleSettings> rules = instrumentation.getRules();

        MetricsSettings metricsSettings = config.getMetrics();
        
        List<ScopeDoc> scopesDocs = generateScopeDocs(scopes);
        scopesDocs.sort(Comparator.comparing(BaseDoc::getName));
        List<ActionDoc> actionsDocs = generateActionDocs(actions);
        actionsDocs.sort(Comparator.comparing(BaseDoc::getName));
        List<RuleDoc> rulesDocs = generateRuleDocs(rules);
        rulesDocs.sort(Comparator.comparing(BaseDoc::getName));
        List<MetricDoc> metricDocs = generateMetricDocs(metricsSettings);
        metricDocs.sort(Comparator.comparing(BaseDoc::getName));

        return new FullDoc(scopesDocs, actionsDocs, rulesDocs, metricDocs);
    }
    
    private List<ScopeDoc> generateScopeDocs(Map<String, InstrumentationScopeSettings> scopes){
        List<ScopeDoc> scopeDocs = new ArrayList<>();
        for(String scopeName: scopes.keySet()){
            InstrumentationScopeSettings scopeSettings = scopes.get(scopeName);
            BaseDocSettings doc = scopeSettings.get_doc();

            String description = "";
            if(doc != null) {
                description = doc.get_description();
            }
            scopeDocs.add(new ScopeDoc(scopeName, description));
        }
        return scopeDocs;
    }

    private List<MetricDoc> generateMetricDocs(MetricsSettings metricsSettings){
        Map<String, MetricDefinitionSettings> metrics = metricsSettings.getDefinitions();
        List<MetricDoc> metricDocs = new ArrayList<>();
        for(String metricName: metrics.keySet()){

            MetricDefinitionSettings metricDefinitionSettings = metrics.get(metricName);

            String description = metricDefinitionSettings.getDescription();
            if(description==null){
                description = "";
            }
            String unit = metricDefinitionSettings.getUnit();

            metricDocs.add(new MetricDoc(metricName, description, unit));

        }
        return metricDocs;
    }

    private List<ActionDoc> generateActionDocs(Map<String, GenericActionSettings> actions){
        List<ActionDoc> actionDocs = new ArrayList<>();
        for(String actionName: actions.keySet()){
            GenericActionSettings actionSettings = actions.get(actionName);
            ActionDocSettings doc = actionSettings.get_doc();

            String description = "";
            String returnDesc = null;

            Map<String, String> inputDescriptions = Collections.emptyMap();

            if(doc != null) {
                description = doc.get_description();
                returnDesc = doc.get_returnDesc();
                inputDescriptions = doc.get_inputDesc();
            }

            Boolean isVoid = actionSettings.getIsVoid();

            List<ActionInputDoc> inputs = new ArrayList<>();
            Map<String, String> inputTypes = actionSettings.getInput();

            for (String inputName : inputTypes.keySet()) {
                inputs.add(new ActionInputDoc(inputName, inputTypes.get(inputName),
                        inputDescriptions.getOrDefault(inputName, "")));
            }

            actionDocs.add(new ActionDoc(actionName, description, inputs, returnDesc, isVoid));
        }
        return actionDocs;
    }

    private List<RuleDoc> generateRuleDocs(Map<String, InstrumentationRuleSettings> rules){
        List<RuleDoc> ruleDocs = new ArrayList<>();
        for(String ruleName: rules.keySet()){

            InstrumentationRuleSettings ruleSettings = rules.get(ruleName);
            BaseDocSettings doc = ruleSettings.get_doc();

            String description = "";
            if(doc != null) {
                description = doc.get_description();
            }

            List<String> include = new ArrayList<>(ruleSettings.getInclude().keySet());

            List<String> scopes = new ArrayList<>(ruleSettings.getScopes().keySet());

            List<RuleMetricsDoc> metricsDocs = new ArrayList<>();
            for (String metricKey : ruleSettings.getMetrics().keySet()) {
                MetricRecordingSettings currentMetric = ruleSettings.getMetrics().get(metricKey);

                String metricName;
                if (currentMetric.getMetric() != null) {
                    metricName = currentMetric.getMetric();
                } else {
                    metricName = metricKey;
                }
                String value = currentMetric.getValue();
                Map<String, String> dataTags = currentMetric.getDataTags();
                Map<String, String> constantTags = currentMetric.getConstantTags();

                metricsDocs.add(new RuleMetricsDoc(metricName, value, dataTags, constantTags));
            }

            RuleTracingSettings tracingSettings = ruleSettings.getTracing();
            RuleTracingDoc ruleTracingDoc = null;
            if (tracingSettings!=null) {
                Boolean startSpan = tracingSettings.getStartSpan();

                Map<String, String> startSpanConditions = new HashMap<>();
                ConditionalActionSettings conditionalActionSettings = tracingSettings.getStartSpanConditions();

                for (Field field : conditionalActionSettings.getClass().getFields()) {

                    String fieldName = field.getName();
                    try {
                        String fieldValue = BeanUtils.getProperty(conditionalActionSettings, fieldName);
                        startSpanConditions.put(fieldName, fieldValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Map<String, String> attributes = tracingSettings.getAttributes();
                ruleTracingDoc = new RuleTracingDoc(startSpan, startSpanConditions, attributes);
            }


            Map<String, List<RuleActionCallDoc>> entryExits = new HashMap<>();
            String[] fieldNames = {"preEntry", "entry", "postEntry", "preExit", "exit", "postExit"};
            for (String fieldName : fieldNames) {
                try {
                    Map<String, ActionCallSettings> entryExit =
                            (Map) PropertyUtils.getProperty(ruleSettings, fieldName);
                    if(!entryExit.isEmpty()){
                        List<RuleActionCallDoc> actionCallDocs = new ArrayList<>();
                        for (String actionCallKey : entryExit.keySet()) {
                            actionCallDocs.add(new RuleActionCallDoc(
                                    actionCallKey, entryExit.get(actionCallKey).getAction()));
                        }
                        entryExits.put(fieldName, actionCallDocs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ruleDocs.add(
                    new RuleDoc(ruleName, description, include, scopes,
                            metricsDocs, ruleTracingDoc, entryExits)
            );
        }
        return ruleDocs;
    }
    
}
