package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.shader.screen.ScreenShaderGraphType;
import com.gempukku.libgdx.graph.shader.screen.config.ScreenShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ScreenShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ScreenShaderRendererEditorProducer() {
        super(new ScreenShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        ShaderGraphEditorPart screenShaderBoxPart = new ShaderGraphEditorPart((UIGraphType) GraphTypeRegistry.findGraphType(ScreenShaderGraphType.TYPE));
        graphNodeEditor.addGraphEditorPart(screenShaderBoxPart);
    }
}
