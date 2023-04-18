package com.gempukku.libgdx.graph.plugin.maps.design.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.maps.producer.MapsLayerIdsRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;
import com.kotcrab.vis.ui.widget.VisLabel;

public class MapsLayerIdsRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public MapsLayerIdsRendererEditorProducer() {
        super(new MapsLayerIdsRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        graphNodeEditor.addGraphBoxPart(
                new StringEditorPart("Map Id:", "id"));
        graphNodeEditor.addGraphBoxPart(
                new StringEditorPart("Layer ids:", "layers"));

        VisLabel description = new VisLabel("Comma separated list of layer ids");
        description.setColor(Color.valueOf("7f7f7fff"));
        GraphNodeEditorPart commentPart = new DefaultGraphNodeEditorPart(description, null);
        graphNodeEditor.addGraphBoxPart(commentPart);
    }
}
