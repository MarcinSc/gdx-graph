package com.gempukku.libgdx.graph.ui.shader.producer.math.common;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.math.common.ConditionalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class ConditionalShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public ConditionalShaderBoxProducer() {
        super(new ConditionalShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        SelectEditorPart operationType = new SelectEditorPart("Operation", "operation",
                ">", ">=", "==", "<=", "<", "!=");
        graphNodeEditor.addGraphBoxPart(operationType);
        SelectEditorPart aggregationType = new SelectEditorPart("Aggregate", "aggregate",
                "any", "all");
        graphNodeEditor.addGraphBoxPart(aggregationType);
    }
}
