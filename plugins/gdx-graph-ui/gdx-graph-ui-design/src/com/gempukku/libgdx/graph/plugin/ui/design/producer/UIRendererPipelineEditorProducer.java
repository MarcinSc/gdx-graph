package com.gempukku.libgdx.graph.plugin.ui.design.producer;

import com.gempukku.libgdx.graph.render.ui.producer.UIRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class UIRendererPipelineEditorProducer extends GdxGraphNodeEditorProducer {
    public UIRendererPipelineEditorProducer() {
        super(new UIRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        graphNodeEditor.addGraphEditorPart(
                new StringEditorPart("Stage Id:", "id", "", "gdx-graph-property-label", "gdx-graph-property"));
    }
}
