package com.gempukku.libgdx.graph.shader.producer.effect;

import com.gempukku.libgdx.graph.shader.config.common.effect.DitherColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class DitherColorShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public DitherColorShaderEditorProducer() {
        super(new DitherColorShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        SelectEditorPart ditherSizeSelect = new SelectEditorPart("Dither size:", "ditherSize",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"2", "4", "8"});
        graphNodeEditor.addGraphEditorPart(ditherSizeSelect);
    }
}
