package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.screen.config.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ScreenShaderRendererBoxProducer extends GdxGraphNodeEditorProducer {
    public ScreenShaderRendererBoxProducer() {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        ScreenShaderBoxPart screenShaderBoxPart = new ScreenShaderBoxPart();
        graphNodeEditor.addGraphBoxPart(screenShaderBoxPart);
    }
}
