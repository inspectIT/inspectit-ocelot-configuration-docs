package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.configuration.docs.docobjects.*;
import net.steppschuh.markdowngenerator.Markdown;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.list.UnorderedList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.steppschuh.markdowngenerator.Markdown.*;

public class DocMardownGenerator {

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
            sb.append(heading(ruleDoc.getName(), 3)).append("\n")
                    .append(text(ruleDoc.getDescription())).append("\n\n");

            // Includes Markdown
            List<Link> includesMarkdown = ruleDoc.getInclude().stream()
                    .map(include -> {
                        return link(include, include);
                    }).collect(Collectors.toList());
            sb.append(heading("Include:", 4))
                    .append("\n\n").append(new UnorderedList<>(includesMarkdown)).append("\n");
        }
        return sb.toString();
    }

    /*public String rulesIncludesMarkdown(){

    }

    public String rulesAttributesMarkdown(){

    }

    public String actionCallsMarkdown(){

    }

    public String ruleMetricsMarkdown(){

    }*/

    public String actionsMarkdown(List<ActionDoc> actions){
        StringBuilder sb = new StringBuilder();
        for(ActionDoc actionDoc: actions){
            sb.append(heading(actionDoc.getName(), 3)).append("\n")
                    .append(text(actionDoc.getDescription())).append("\n\n");
                    //.append(actionInputsMarkdown(actionDoc.getInputs())).append("\n");

            // Inputs Markdown
            List<String> inputsMarkdown = actionDoc.getInputs().stream()
                    .map(inputDoc -> {
                        return String.format("%s %s: %s",
                                inputDoc.getType(), inputDoc.getName(), inputDoc.getDescription());
                    }).collect(Collectors.toList());
            sb.append(heading("Inputs:", 4)).append("\n\n")
                    .append(new UnorderedList<>(inputsMarkdown)).append("\n");

        }

        return sb.toString();
    }

    /*
    public String actionInputsMarkdown(List<ActionInputDoc> inputs){
        StringBuilder sb = new StringBuilder();
        for(ActionInputDoc inputDoc: inputs){
            sb.append(text(String.format("%s %s: %s",
                    inputDoc.getType(), inputDoc.getName(), inputDoc.getDescription())));
        }
        return sb.toString();
    }*/

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
