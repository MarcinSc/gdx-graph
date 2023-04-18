package com.gempukku.libgdx.graph.plugin.ui.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.ui.UIRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class UIRendererPipelineEditorProducer extends GdxGraphNodeEditorProducer {
    public UIRendererPipelineEditorProducer() {
        super(new UIRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        graphNodeEditor.addGraphBoxPart(
                new StringEditorPart("Stage Id:", "id"));
    }
}
