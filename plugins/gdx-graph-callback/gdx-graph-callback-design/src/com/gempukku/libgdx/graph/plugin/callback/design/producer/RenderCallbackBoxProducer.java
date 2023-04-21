package com.gempukku.libgdx.graph.plugin.callback.design.producer;

import com.gempukku.libgdx.graph.plugin.callback.producer.RenderCallbackPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class RenderCallbackBoxProducer extends GdxGraphNodeEditorProducer {
    public RenderCallbackBoxProducer() {
        super(new RenderCallbackPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        StringEditorPart callbackId = new StringEditorPart("Callback id: ", "callbackId", "", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphBoxPart(callbackId);
    }
}