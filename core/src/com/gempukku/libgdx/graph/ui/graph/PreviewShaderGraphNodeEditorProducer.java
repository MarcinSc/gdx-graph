package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.ui.part.GraphAwareCollapsibleSectionEditorPart;
import com.gempukku.libgdx.graph.ui.part.PreviewShaderEditorPart;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class PreviewShaderGraphNodeEditorProducer extends GdxGraphNodeEditorProducer {
    private final String output;
    private final int width;
    private final int height;

    public PreviewShaderGraphNodeEditorProducer(MenuNodeConfiguration configuration, String output, int width, int height) {
        super(configuration);
        this.output = output;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        PreviewShaderEditorPart previewShaderEditorPart = new PreviewShaderEditorPart(graphNodeEditor.getNodeId(), output, width, height);
        graphNodeEditor.addGraphEditorPart(
                new GraphAwareCollapsibleSectionEditorPart("preview.expanded", previewShaderEditorPart, "Preview",
                "default", "default"));
    }
}
