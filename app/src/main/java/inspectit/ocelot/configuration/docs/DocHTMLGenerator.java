package inspectit.ocelot.configuration.docs;

import com.fasterxml.jackson.databind.ser.Serializers;
import inspectit.ocelot.configuration.docs.docobjects.BaseDoc;
import j2html.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static j2html.TagCreator.*;

@Slf4j
public class DocHTMLGenerator {

    public String generateHTMLDoc(FullDoc fullDoc){
        String cssPath = "configDocStyle.css";
        return baseTemplate(cssPath, fullDoc).render();
    }

    /* gives Path somewhere in build directory, don't want that
    private String cssAbsolutePath(){
        URL resource = DocHTMLGenerator.class.getClassLoader().getResource("configDocStyle.css");
        try {
            String cssPath = Paths.get(resource.toURI()).toFile().getAbsolutePath();
            return cssPath;
        } catch(URISyntaxException e){
            log.error("CSS file not found.");
            e.printStackTrace();
            return "";
        }
    }*/

    private Tag baseTemplate(String cssPath, FullDoc fullDoc){
        return html(
                head(
                        title("inspectIT Ocelot ConfigDoc"),
                        link().withRel("stylesheet").withHref(cssPath)
                ),
                body(
                        h1("inspectIT Ocelot Configuration Documentation"),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Scopes"),
                                each(fullDoc.getScopes(), BaseDoc::mainConfigDocPartial)
                        ),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Rules"),
                                each(fullDoc.getRules(), BaseDoc::mainConfigDocPartial)
                        ),
                        div(
                                attrs(".doc-section"),
                                h2(attrs(".section-heading"), "Actions"),
                                each(fullDoc.getActions(), BaseDoc::mainConfigDocPartial)
                        )
                )
        );

    }

}
