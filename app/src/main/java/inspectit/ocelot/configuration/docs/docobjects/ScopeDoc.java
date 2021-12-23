package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import static j2html.TagCreator.*;

public class ScopeDoc extends BaseDoc {

    public ScopeDoc(String name, String description){
        super(name, description, DocType.SCOPE);
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
