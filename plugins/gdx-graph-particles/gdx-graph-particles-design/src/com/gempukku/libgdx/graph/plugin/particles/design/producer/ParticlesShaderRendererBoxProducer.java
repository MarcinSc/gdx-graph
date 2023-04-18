package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ParticlesShaderRendererBoxProducer extends GdxGraphNodeEditorProducer {
    public ParticlesShaderRendererBoxProducer() {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        ParticlesShadersBoxPart graphBoxPart = new ParticlesShadersBoxPart();
        graphNodeEditor.addGraphBoxPart(graphBoxPart);
    }
}
