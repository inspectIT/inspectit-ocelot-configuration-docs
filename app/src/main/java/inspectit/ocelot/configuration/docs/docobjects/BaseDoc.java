package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import static j2html.TagCreator.*;

public abstract class BaseDoc {

    public BaseDoc(String description, String name, DocType docType) {
        this.description = description;
        this.name = name;
        this.docType = docType;
    }

    public enum DocType{
        SCOPE, ACTION, RULE
    }

    String description;
    String name;

    DocType docType;

    public Tag mainConfigDocPartial(){
        return
                div(
                        a(attrs("#" + this.name)),
                        h3(attrs(".element-heading")),
                        div(
                                attrs(".doc-element-content"),
                                specificPartialHTML()
                        )
        );
    }

    abstract Tag specificPartialHTML();

    abstract String toJSON();

}
