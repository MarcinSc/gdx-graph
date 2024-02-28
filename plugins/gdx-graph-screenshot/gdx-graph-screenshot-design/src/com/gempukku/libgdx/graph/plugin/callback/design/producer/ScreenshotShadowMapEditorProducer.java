package com.gempukku.libgdx.graph.plugin.callback.design.producer;

import com.gempukku.libgdx.graph.render.screenshot.producer.ScreenshotShadowMapPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.IndexEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class ScreenshotShadowMapEditorProducer extends GdxGraphNodeEditorProducer {
    public ScreenshotShadowMapEditorProducer() {
        super(new ScreenshotShadowMapPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        StringEditorPart envId = new StringEditorPart("Env id: ", "environmentId", "", "gdx-graph-property-label", "gdx-graph-property");
        IndexEditorPart indexEditorPart = new IndexEditorPart("Light ind: ", "lightIndex", "gdx-graph-property-label", "gdx-graph-property");
        StringEditorPart path = new StringEditorPart("Path: ", "path", "", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphEditorPart(envId);
        graphNodeEditor.addGraphEditorPart(indexEditorPart);
        graphNodeEditor.addGraphEditorPart(path);
    }
}