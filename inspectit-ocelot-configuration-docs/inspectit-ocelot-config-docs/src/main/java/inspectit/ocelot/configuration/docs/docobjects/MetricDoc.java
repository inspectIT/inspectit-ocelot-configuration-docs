package inspectit.ocelot.configuration.docs.docobjects;

import j2html.tags.Tag;

import static j2html.TagCreator.p;

public class MetricDoc extends BaseDoc {

    public MetricDoc(String name, String description, String unit){
        super(name, description);
        this.unit = unit;
    }

    String unit;

    @Override
    Tag specificPartialHTML() {
        return p(String.format("Unit: %s", unit));
    }
}
