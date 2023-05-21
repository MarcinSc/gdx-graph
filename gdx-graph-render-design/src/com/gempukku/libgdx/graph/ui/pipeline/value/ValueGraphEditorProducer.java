package com.gempukku.libgdx.graph.ui.pipeline.value;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.*;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;

public abstract class ValueGraphEditorProducer implements MenuGraphNodeEditorProducer {
    protected MenuNodeConfiguration configuration;

    public ValueGraphEditorProducer(MenuNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public NodeConfiguration getConfiguration(JsonValue data) {
        return configuration;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public GraphNodeEditor createNodeEditor(String nodeId, JsonValue data) {
        GdxGraphNodeEditor graphNodeEditor = new GdxGraphNodeEditor(nodeId, configuration);
        graphNodeEditor.addGraphEditorPart(createValuePart());

        if (data != null)
            graphNodeEditor.initialize(data);

        return graphNodeEditor;
    }

    protected Drawable getInputDrawable(GraphNodeInput input, boolean valid) {
        boolean required = input.isRequired();
        String side = (input.getSide() == GraphNodeInputSide.Left) ? "left" : "top";
        String drawable = "connector-" + side + (required ? "-required" : "") + (valid ? "" : "-invalid");
        return VisUI.getSkin().getDrawable(drawable);
    }

    protected Drawable getOutputDrawable(GraphNodeOutput output, boolean valid) {
        String side = (output.getSide() == GraphNodeOutputSide.Right) ? "right" : "bottom";
        String drawable = "connector-" + side + (valid ? "" : "-invalid");
        return VisUI.getSkin().getDrawable(drawable);
    }

    protected abstract GraphNodeEditorPart createValuePart();
}
