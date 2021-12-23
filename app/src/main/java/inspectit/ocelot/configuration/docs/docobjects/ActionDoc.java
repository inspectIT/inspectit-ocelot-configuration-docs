package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import static j2html.TagCreator.*;

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

    Tag inputListHtml(){
        if(inputs.isEmpty()){
            return null;
        } else {
            return div(
                    dt(h4("Inputs:")),
                    each(inputs, ActionInputDoc::actionInputDocHtml
                    )
            );
        }
    }

    Tag returnDescHtml(){
        if(returnDescription!=null){
            return div(
                    dt(h4("Return value:")),
                    dd(
                            p(returnDescription)
                    )
            );
        } else {
            return null;
        }
    }

    @Override
    Tag specificPartialHTML() {
        return div(
                attrs(".doc-element-content"),
                p(description),
                dl(
                        inputListHtml(),
                        returnDescHtml()
                )
        );
    }

    @Override
    String toJSON() {
        return null;
    }
}
