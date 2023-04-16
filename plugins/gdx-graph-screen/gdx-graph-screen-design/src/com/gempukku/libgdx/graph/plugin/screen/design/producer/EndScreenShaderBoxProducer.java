package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.plugin.screen.config.EndScreenShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenShaderPreviewBoxPart;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.graph.ui.part.BlendingBoxPart;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;

public class EndScreenShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public EndScreenShaderBoxProducer() {
        super(new EndScreenShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final ScreenShaderPreviewBoxPart previewBoxPart = new ScreenShaderPreviewBoxPart();

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Rendering config"));

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        graphNodeEditor.addGraphBoxPart(blendingBox);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Preview"));

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }


    private class GraphChangedAwareEditor extends DefaultGraphNodeEditor implements GraphChangedAware {
        private ScreenShaderPreviewBoxPart previewBoxPart;

        public GraphChangedAwareEditor(NodeConfiguration configuration, ScreenShaderPreviewBoxPart previewBoxPart) {
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
