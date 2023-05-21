package com.gempukku.libgdx.graph.shader.producer.math.common;

import com.gempukku.libgdx.graph.shader.config.common.math.common.ConditionalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class ConditionalShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public ConditionalShaderEditorProducer() {
        super(new ConditionalShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        SelectEditorPart operationType = new SelectEditorPart("Operation", "operation",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {">", ">=", "==", "<=", "<", "!="});
        graphNodeEditor.addGraphEditorPart(operationType);
        SelectEditorPart aggregationType = new SelectEditorPart("Aggregate", "aggregate",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"any", "all"});
        graphNodeEditor.addGraphEditorPart(aggregationType);
    }
}
