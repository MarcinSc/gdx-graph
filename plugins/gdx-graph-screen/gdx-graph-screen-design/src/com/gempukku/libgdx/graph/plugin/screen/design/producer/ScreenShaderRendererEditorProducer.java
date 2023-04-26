package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.plugin.screen.config.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ScreenShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ScreenShaderRendererEditorProducer() {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        ScreenShaderEditorPart screenShaderBoxPart = new ScreenShaderEditorPart();
        graphNodeEditor.addGraphEditorPart(screenShaderBoxPart);
    }
}
