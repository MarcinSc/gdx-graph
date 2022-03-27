package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class SectionBoxPart extends VisTable implements GraphBoxPart {
    private Separator separator = new Separator();

    public SectionBoxPart(String sectionLabel) {
        add(separator).growX().row();
        VisLabel label = new VisLabel(sectionLabel);
        label.setColor(Color.GRAY);
        label.setAlignment(Align.center);
        add(label).growX().row();
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public void initialize(JsonValue object) {

    }

    @Override
    public void serializePart(JsonValue object) {

    }

    @Override
    public void dispose() {

    }
}
