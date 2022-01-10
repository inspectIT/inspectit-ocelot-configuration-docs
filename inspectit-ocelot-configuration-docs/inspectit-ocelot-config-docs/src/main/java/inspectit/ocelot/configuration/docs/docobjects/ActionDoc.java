package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;
import static j2html.TagCreator.*;

import java.util.List;

public class ActionDoc extends BaseDoc {

    public ActionDoc(String name, String description, List<ActionInputDoc> inputs, String returnDescription, Boolean isVoid) {
        super(name, description);
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
                dl(
                        inputListHtml(),
                        returnDescHtml()
                )
        );
    }
}
