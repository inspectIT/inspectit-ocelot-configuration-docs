package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.configuration.docs.docobjects.*;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.text.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.steppschuh.markdowngenerator.Markdown.*;

public class DocMarkdownGenerator {

    private String anchor(String name){
        return String.format("<a id=\"%s\"></a>\n", name);
    }

    public String scopesMarkdown(List<ScopeDoc> scopes){
        StringBuilder sb = new StringBuilder();
        for(ScopeDoc scopeDoc: scopes){
            sb.append(heading(scopeDoc.getName(), 3)).append("\n")
                    .append(text(scopeDoc.getDescription())).append("\n");
        }
        return sb.toString();
    }

    public String rulesMarkdown(List<RuleDoc> rules){
        StringBuilder sb = new StringBuilder();
        for(RuleDoc ruleDoc: rules){
            sb.append(anchor(ruleDoc.getName()));
            sb.append(heading(ruleDoc.getName(), 3)).append("\n")
                    .append(text(ruleDoc.getDescription())).append("\n\n");

            // Includes Markdown
            if(!ruleDoc.getInclude().isEmpty()) {
                List<Link> includesMarkdown = ruleDoc.getInclude().stream().map(include -> {
                    return link(include, String.format("#%s", include));
                }).collect(Collectors.toList());
                sb.append(heading("Include:", 4)).append("\n\n")
                        .append(new UnorderedList<>(includesMarkdown)).append("\n");
            }

            // Attributes Markdown
            if(!ruleDoc.getEntryExits().isEmpty()) {
                sb.append(heading("Attributes:", 4)).append("\n\n");
                for (String entryExitKey : ruleDoc.getEntryExits().keySet()) {
                    sb.append(heading(entryExitKey, 5)).append("\n");
                    List<Text> attributesMarkdown = ruleDoc.getEntryExits().get(entryExitKey).values().stream().map(
                            actionCallDoc -> {
                                return text(String.format("%s: %s", actionCallDoc.getName(),
                                        link(actionCallDoc.getAction(), String.format("#%s", actionCallDoc.getAction()))));
                    }).collect(Collectors.toList());
                    sb.append(new UnorderedList<>(attributesMarkdown)).append("\n");
                }
            }

            // Metrics Markdown
            if(!ruleDoc.getMetricsDocs().isEmpty()) {
                sb.append(heading("Metrics:", 4)).append("\n\n");
                for (RuleMetricsDoc metricDoc : ruleDoc.getMetricsDocs()) {

                    sb.append(heading(metricDoc.getName(), 5)).append("\n\n");
                    sb.append(heading("Value: ", 6)).append(text(metricDoc.getValue())).append("\n\n");

                    sb.append(tagsMarkdown(metricDoc.getDataTags()));
                    sb.append(tagsMarkdown(metricDoc.getConstantTags()));
                }
            }
        }
        return sb.toString();
    }

    String tagsMarkdown(Map<String, String> tagsMap){
        StringBuilder sb = new StringBuilder();
        if(!tagsMap.isEmpty()) {
            sb.append(heading("Data Tags: ", 6)).append("\n\n");
            List<Text> tagsMd = tagsMap.keySet().stream().map(tagKey ->
                    text(String.format("%s: %s", tagKey, tagsMap.get(tagKey))))
                    .collect(Collectors.toList());
            sb.append(new UnorderedList<>(tagsMd)).append("\n");
        }
        return sb.toString();
    }

    public String actionsMarkdown(List<ActionDoc> actions){
        StringBuilder sb = new StringBuilder();
        for(ActionDoc actionDoc: actions){
            sb.append(anchor(actionDoc.getName()));
            sb.append(heading(actionDoc.getName(), 3)).append("\n")
                    .append(text(actionDoc.getDescription())).append("\n\n");

            // Inputs Markdown
            if(!actionDoc.getInputs().isEmpty()) {
                List<String> inputsMarkdown = actionDoc.getInputs().stream().map(inputDoc -> {
                    return String.format("%s %s: %s", inputDoc.getType(), inputDoc.getName(), inputDoc.getDescription());
                }).collect(Collectors.toList());
                sb.append(heading("Inputs:", 4)).append("\n\n").append(new UnorderedList<>(inputsMarkdown)).append("\n");
            }
        }

        return sb.toString();
    }

    public String metricsMarkdown(List<MetricDoc> metrics){
        StringBuilder sb = new StringBuilder();
        for(MetricDoc metricDoc: metrics){
            sb.append(heading(metricDoc.getName(), 3)).append("\n")
                    .append(text(metricDoc.getDescription())).append("\n\n")
                    .append(text(String.format("Unit: %s", metricDoc.getUnit()))).append("\n");
        }
        return sb.toString();
    }

    public String generateMarkdown(FullDoc fullDoc){
        StringBuilder sb = new StringBuilder()
                .append(heading("inspectIT Ocelot Configuration Documentation", 1)).append("\n")
                .append(heading("Scopes", 2)).append("\n")
                .append(scopesMarkdown(fullDoc.getScopes()))
                .append(heading("Rules", 2)).append("\n")
                .append(rulesMarkdown(fullDoc.getRules()))
                .append(heading("Actions", 2)).append("\n")
                .append(actionsMarkdown(fullDoc.getActions()))
                .append(heading("Metrics", 2)).append("\n")
                .append(metricsMarkdown(fullDoc.getMetrics()));
        return sb.toString();
    }

}
