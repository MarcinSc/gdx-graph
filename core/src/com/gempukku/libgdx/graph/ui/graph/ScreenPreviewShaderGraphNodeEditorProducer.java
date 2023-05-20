package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.ui.part.GraphAwareCollapsibleSectionEditorPart;
import com.gempukku.libgdx.graph.ui.part.ScreenPreviewShaderEditorPart;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public class ScreenPreviewShaderGraphNodeEditorProducer extends GdxGraphNodeEditorProducer {
    private final String output;
    private final int width;
    private final int height;

    public ScreenPreviewShaderGraphNodeEditorProducer(MenuNodeConfiguration configuration, String output, int width, int height) {
        super(configuration);
        this.output = output;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        ScreenPreviewShaderEditorPart previewShaderEditorPart = new ScreenPreviewShaderEditorPart(graphNodeEditor.getNodeId(), output, width, height);
        GraphAwareCollapsibleSectionEditorPart sectionEditorPart = new GraphAwareCollapsibleSectionEditorPart("preview.expanded", previewShaderEditorPart, "Preview",
                "gdx-graph-preview", "default") {
            @Override
            public float getPrefWidth() {
                return width;
            }
        };
        graphNodeEditor.addGraphEditorPart(sectionEditorPart);
    }
}
