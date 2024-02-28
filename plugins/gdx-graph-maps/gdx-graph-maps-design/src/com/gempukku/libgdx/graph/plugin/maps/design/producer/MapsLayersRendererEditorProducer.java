package com.gempukku.libgdx.graph.plugin.maps.design.producer;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.render.maps.producer.MapsLayerIdsRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MapsLayersRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public MapsLayersRendererEditorProducer() {
        super(new MapsLayerIdsRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        graphNodeEditor.addGraphEditorPart(
                new StringEditorPart("Map Id:", "id", "", "gdx-graph-property-label", "gdx-graph-property"));
        graphNodeEditor.addGraphEditorPart(
                new StringEditorPart("Layers:", "layers", "", "gdx-graph-property-label", "gdx-graph-property"));

        VisLabel description = new VisLabel("Comma separated list of layer names", "gdx-graph-property-label");
        description.setColor(Color.valueOf("7f7f7fff"));
        GraphNodeEditorPart commentPart = new DefaultGraphNodeEditorPart(description, null);
        graphNodeEditor.addGraphEditorPart(commentPart);
    }
}
