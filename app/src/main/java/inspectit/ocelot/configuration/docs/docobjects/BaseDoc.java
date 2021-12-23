package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.DomContent;
import j2html.tags.Tag;
import lombok.Data;

import java.util.Map;

import static j2html.TagCreator.*;

@Data
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

    public static Tag listKeyValueHtml(String heading, Map<String, String> map){
        if(map.isEmpty()){
            return null;
        } else {
            return dl(
                    dt(String.format("%s:", heading)),
                    each(map.keySet(), key ->
                            dd(String.format("%s: %s", key,
                                    map.get(key)))
                    )
            );
        }
    }

    public Tag mainConfigDocPartial(){
        return
                div(
                        attrs(".doc-element"),
                        a(attrs("#" + this.name)),
                        h3(attrs(".element-heading"), this.name),
                        specificPartialHTML()
        );
    }

    abstract Tag specificPartialHTML();

    abstract String toJSON();

}
