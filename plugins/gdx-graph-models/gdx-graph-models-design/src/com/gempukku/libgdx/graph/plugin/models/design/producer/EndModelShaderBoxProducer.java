package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.models.config.EndModelShaderNodeConfiguration;
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
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class EndModelShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public EndModelShaderBoxProducer() {
        super(new EndModelShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final ModelShaderPreviewBoxPart previewBoxPart = new ModelShaderPreviewBoxPart();

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "Object space", "World space");
        graphNodeEditor.addGraphBoxPart(positionType);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Rendering config"));

        EnumSelectEditorPart cullingBox = new EnumSelectEditorPart("Culling", "culling", new StringifyEnum<BasicShader.Culling>(), BasicShader.Culling.values());
        graphNodeEditor.addGraphBoxPart(cullingBox);

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        graphNodeEditor.addGraphBoxPart(blendingBox);

        EnumSelectEditorPart depthTestBox = new EnumSelectEditorPart("DepthTest", "depthTest", new StringifyEnum<BasicShader.DepthTesting>(), BasicShader.DepthTesting.values());
        graphNodeEditor.addGraphBoxPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite");
        graphNodeEditor.addGraphBoxPart(writeDepthBox);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Preview"));

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }

    private class GraphChangedAwareEditor extends DefaultGraphNodeEditor implements GraphChangedAware {
        private ModelShaderPreviewBoxPart previewBoxPart;

        public GraphChangedAwareEditor(NodeConfiguration configuration, ModelShaderPreviewBoxPart previewBoxPart) {
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
