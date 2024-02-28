package com.gempukku.libgdx.graph.shader.producer.texture;

import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;

public class UVFlipbookShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public UVFlipbookShaderEditorProducer() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        CheckboxEditorPart invertX = new CheckboxEditorPart("Invert X", "invertX", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(invertX);
        CheckboxEditorPart invertY = new CheckboxEditorPart("Invert Y", "invertY", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(invertY);
    }
}
