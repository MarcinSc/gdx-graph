package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.gempukku.libgdx.graph.plugin.lighting3d.producer.PointLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.IndexEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class PointLightBoxProducer extends GdxGraphNodeEditorProducer {
    public PointLightBoxProducer() {
        super(new PointLightShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        StringEditorPart envId = new StringEditorPart("Env id: ", "id", "", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphBoxPart(envId);

        IndexEditorPart indexPart = new IndexEditorPart("Index", "index", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphBoxPart(indexPart);
    }
}