package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import lombok.Data;

import static j2html.TagCreator.*;

@Data
public class RuleActionCallDoc {

    public RuleActionCallDoc(String name, String action) {
        this.name = name;
        this.action = action;
    }

    public RuleActionCallDoc(RuleActionCallDoc fromIncludedRule, String inheritedFrom) {
        this.name = fromIncludedRule.getName();
        this.action = fromIncludedRule.getAction();
        this.inheritedFrom = inheritedFrom;
    }

    String name;
    String action;
    String inheritedFrom;

    Tag actionCallDocHtml(){
        return dd(join(
                String.format("%s:", name),
                a(action).withHref(String.format("#%s", action)),
                (inheritedFrom != null) ? i(String.format("(from included rule %s.)", inheritedFrom)) :null
        ));
    }

}
