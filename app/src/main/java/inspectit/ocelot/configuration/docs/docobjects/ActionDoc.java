package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;

import java.util.List;

public class ActionDoc extends BaseDoc {

    public ActionDoc(String description, String name, List<ActionInputDoc> inputs,
                     String returnDescription, Boolean isVoid) {
        super(description, name, DocType.ACTION);
        this.inputs = inputs;
        this.returnDescription = returnDescription;
        this.isVoid = isVoid;
    }

    List<ActionInputDoc> inputs;
    String returnDescription;
    Boolean isVoid;

    @Override
    Tag specificPartialHTML() {
        return null;
    }

    @Override
    String toJSON() {
        return null;
    }
}
