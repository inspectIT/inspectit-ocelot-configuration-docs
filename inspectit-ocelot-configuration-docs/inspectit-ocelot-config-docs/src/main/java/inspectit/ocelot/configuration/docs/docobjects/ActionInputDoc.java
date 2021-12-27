package inspectit.ocelot.configuration.docs.docobjects;


import j2html.tags.Tag;
import static j2html.TagCreator.*;

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
