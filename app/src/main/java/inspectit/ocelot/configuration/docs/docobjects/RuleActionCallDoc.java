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

    String name;
    String action;

    Tag actionCallDocHtml(){
        return dd(join(
                String.format("%s:", name),
                " ",
                a(action).withHref(String.format("#%s", action))
        ));
    }

}
