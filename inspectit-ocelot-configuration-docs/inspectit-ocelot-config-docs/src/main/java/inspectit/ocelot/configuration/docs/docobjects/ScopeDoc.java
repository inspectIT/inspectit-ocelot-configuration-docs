package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;

public class ScopeDoc extends BaseDoc {

    public ScopeDoc(String name, String description){
        super(name, description);
    }

    @Override
    Tag specificPartialHTML() {
        return null;
    }
}
