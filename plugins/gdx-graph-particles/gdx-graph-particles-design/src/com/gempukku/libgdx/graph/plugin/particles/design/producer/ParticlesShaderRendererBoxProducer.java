package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;

public class ParticlesShaderRendererBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public ParticlesShaderRendererBoxProducer() {
        super(new ParticlesShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        ParticlesShadersBoxPart graphBoxPart = new ParticlesShadersBoxPart();
        graphNodeEditor.addGraphBoxPart(graphBoxPart);
    }
}
