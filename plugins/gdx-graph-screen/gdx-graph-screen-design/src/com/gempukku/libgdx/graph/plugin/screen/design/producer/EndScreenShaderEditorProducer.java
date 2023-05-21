package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.gempukku.libgdx.graph.plugin.screen.design.ScreenShaderPreviewEditorPart;
import com.gempukku.libgdx.graph.shader.screen.config.EndScreenShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.BlendingEditorPart;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;

public class EndScreenShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public EndScreenShaderEditorProducer() {
        super(new EndScreenShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final ScreenShaderPreviewEditorPart previewBoxPart = new ScreenShaderPreviewEditorPart();

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Rendering config", "gdx-graph-section-label", "default"));

        BlendingEditorPart blendingBox = new BlendingEditorPart();
        graphNodeEditor.addGraphEditorPart(blendingBox);

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Preview", "gdx-graph-section-label", "default"));

        graphNodeEditor.addGraphEditorPart(previewBoxPart);
    }
}
