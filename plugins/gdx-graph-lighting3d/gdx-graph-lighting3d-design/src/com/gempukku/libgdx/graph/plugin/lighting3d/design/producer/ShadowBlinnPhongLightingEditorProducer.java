package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.gempukku.libgdx.graph.shader.lighting3d.producer.ShadowBlinnPhongLightingShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.preview.SpherePreviewShaderEditorPart;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.GraphAwareCollapsibleSectionEditorPart;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class ShadowBlinnPhongLightingEditorProducer extends GdxGraphNodeEditorProducer {
    public ShadowBlinnPhongLightingEditorProducer() {
        super(new ShadowBlinnPhongLightingShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        StringEditorPart envId = new StringEditorPart("Env id: ", "id", "", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphEditorPart(envId);

        int width = 150;
        int height = 150;
        SpherePreviewShaderEditorPart previewShaderEditorPart = new SpherePreviewShaderEditorPart(graphNodeEditor.getNodeId(), "output", width, height);
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