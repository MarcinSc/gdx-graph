package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.screen.config.EndScreenShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenShaderPreviewBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.BlendingBoxPart;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;

public class EndScreenShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public EndScreenShaderBoxProducer() {
        super(new EndScreenShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final ScreenShaderPreviewBoxPart previewBoxPart = new ScreenShaderPreviewBoxPart();

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Rendering config"));

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        graphNodeEditor.addGraphBoxPart(blendingBox);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Preview"));

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }
}
