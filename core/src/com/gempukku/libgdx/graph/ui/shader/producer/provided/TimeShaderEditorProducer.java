package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.gempukku.libgdx.graph.shader.config.common.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;

public class TimeShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public TimeShaderEditorProducer() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        FloatEditorPart multiplierPart = new FloatEditorPart("Multiplier", "multiplier", 1f, null, "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphEditorPart(multiplierPart);
    }
}
