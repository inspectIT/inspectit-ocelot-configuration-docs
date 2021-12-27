package inspectit.ocelot.configuration.docs;

import inspectit.ocelot.configuration.docs.docobjects.ActionDoc;
import inspectit.ocelot.configuration.docs.docobjects.RuleDoc;
import inspectit.ocelot.configuration.docs.docobjects.ScopeDoc;
import lombok.Data;

import java.util.List;

@Data
public class FullDoc {

    public FullDoc(List<ScopeDoc> scopes, List<ActionDoc> actions, List<RuleDoc> rules) {
        this.scopes = scopes;
        this.actions = actions;
        this.rules = rules;
    }

    private List<ScopeDoc> scopes;
    private List<ActionDoc> actions;
    private List<RuleDoc> rules;

}
