package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import static j2html.TagCreator.*;

public class ScopeDoc extends BaseDoc {

    public ScopeDoc(String description, String name){
        super(description, name);
    }

    @Override
    Tag specificPartialHTML() {
        return null;
    }

    @Override
    String toJSON() {
        return null;
    }
}
