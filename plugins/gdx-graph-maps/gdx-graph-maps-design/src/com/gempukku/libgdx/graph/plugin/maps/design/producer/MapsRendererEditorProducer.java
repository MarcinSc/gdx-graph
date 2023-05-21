package com.gempukku.libgdx.graph.plugin.maps.design.producer;

import com.gempukku.libgdx.graph.render.maps.producer.MapsRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class MapsRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public MapsRendererEditorProducer() {
        super(new MapsRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        graphNodeEditor.addGraphEditorPart(
                new StringEditorPart("Map Id:", "id", "", "gdx-graph-property-label", "gdx-graph-property"));
    }
}
