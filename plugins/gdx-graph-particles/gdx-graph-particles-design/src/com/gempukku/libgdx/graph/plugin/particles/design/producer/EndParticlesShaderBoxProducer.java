package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.particles.config.EndParticlesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.graph.ui.part.BlendingBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;

public class EndParticlesShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public EndParticlesShaderBoxProducer() {
        super(new EndParticlesShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final ParticlesShaderPreviewBoxPart previewBoxPart = new ParticlesShaderPreviewBoxPart();

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Rendering config"));

        EnumSelectEditorPart<BasicShader.Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", new StringifyEnum<BasicShader.Culling>(), BasicShader.Culling.values());
        graphNodeEditor.addGraphBoxPart(cullingBox);

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        graphNodeEditor.addGraphBoxPart(blendingBox);

        EnumSelectEditorPart<BasicShader.DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", new StringifyEnum<BasicShader.DepthTesting>(), BasicShader.DepthTesting.values());
        graphNodeEditor.addGraphBoxPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite");
        graphNodeEditor.addGraphBoxPart(writeDepthBox);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Preview"));

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }

    private class GraphChangedAwareEditor extends DefaultGraphNodeEditor implements GraphChangedAware {
        private ParticlesShaderPreviewBoxPart previewBoxPart;

        public GraphChangedAwareEditor(NodeConfiguration configuration, ParticlesShaderPreviewBoxPart previewBoxPart) {
            super(configuration);
            this.previewBoxPart = previewBoxPart;
        }

        @Override
        public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
            if (event.isData() || event.isStructure()) {
                previewBoxPart.graphChanged(hasErrors, graph);
            }
        }
    }
}
