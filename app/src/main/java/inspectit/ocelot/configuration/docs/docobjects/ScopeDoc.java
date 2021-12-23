package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import static j2html.TagCreator.*;

public class ScopeDoc extends BaseDoc {

    public ScopeDoc(String description, String name){
        super(description, name, DocType.SCOPE);
    }

    @Override
    Tag specificPartialHTML() {
        return p(this.description);
    }

    @Override
    String toJSON() {
        return null;
    }
}
