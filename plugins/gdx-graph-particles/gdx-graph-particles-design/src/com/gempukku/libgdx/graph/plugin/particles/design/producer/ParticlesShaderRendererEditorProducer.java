package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.shader.particles.ParticleEffectGraphType;
import com.gempukku.libgdx.graph.shader.particles.ParticlesShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ParticlesShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ParticlesShaderRendererEditorProducer() {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        ShaderGraphEditorPart graphBoxPart = new ShaderGraphEditorPart((UIGraphType) GraphTypeRegistry.findGraphType(ParticleEffectGraphType.TYPE));
        graphNodeEditor.addGraphEditorPart(graphBoxPart);
    }
}
