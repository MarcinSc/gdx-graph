package com.gempukku.libgdx.graph.plugin.callback.design.producer;

import com.gempukku.libgdx.graph.render.screenshot.producer.ScreenshotPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class ScreenshotEditorProducer extends GdxGraphNodeEditorProducer {
    public ScreenshotEditorProducer() {
        super(new ScreenshotPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        StringEditorPart path = new StringEditorPart("Path: ", "path", "", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphEditorPart(path);
    }
}