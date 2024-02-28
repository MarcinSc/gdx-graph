package com.gempukku.libgdx.graph.shader.producer.effect;

import com.gempukku.libgdx.graph.shader.config.common.effect.DitherShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class DitherShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public DitherShaderEditorProducer() {
        super(new DitherShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        SelectEditorPart ditherSizeSelect = new SelectEditorPart("Dither size:", "ditherSize",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[]{"2", "4", "8"});
        graphNodeEditor.addGraphEditorPart(ditherSizeSelect);
    }
}
