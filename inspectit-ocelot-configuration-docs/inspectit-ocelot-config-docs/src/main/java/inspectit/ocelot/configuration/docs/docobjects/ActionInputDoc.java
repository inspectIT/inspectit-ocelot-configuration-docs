package inspectit.ocelot.configuration.docs.docobjects;


import j2html.tags.Tag;
import lombok.Getter;

import static j2html.TagCreator.*;

@Getter
public class ActionInputDoc{

    public ActionInputDoc(String name, String type, String description){
        this.name = name;
        this.type = type;
        this.description = description;
    }

    String name;
    String type;
    String description;

    Tag actionInputDocHtml(){
        return dd(
                join(
                        span(attrs(".actionInputType"), type),
                        span(attrs(".actionInputName"), name), ":",
                        span(attrs(".actionInputDescription"), description)
                )
        );
    }
}
