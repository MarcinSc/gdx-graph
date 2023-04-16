package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.screen.config.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;

public class ScreenShaderRendererBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public ScreenShaderRendererBoxProducer() {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        ScreenShaderBoxPart screenShaderBoxPart = new ScreenShaderBoxPart();
        graphNodeEditor.addGraphBoxPart(screenShaderBoxPart);
    }
}
