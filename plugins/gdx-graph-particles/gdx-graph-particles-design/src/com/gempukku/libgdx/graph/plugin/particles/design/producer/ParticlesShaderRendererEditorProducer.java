package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.gempukku.libgdx.graph.plugin.particles.ParticlesShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ParticlesShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ParticlesShaderRendererEditorProducer() {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        ParticlesShadersEditorPart graphBoxPart = new ParticlesShadersEditorPart();
        graphNodeEditor.addGraphEditorPart(graphBoxPart);
    }
}
