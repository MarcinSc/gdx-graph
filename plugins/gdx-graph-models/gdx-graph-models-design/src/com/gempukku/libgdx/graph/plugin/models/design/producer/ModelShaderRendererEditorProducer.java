package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.plugin.models.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class ModelShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ModelShaderRendererEditorProducer() {
        super(new ModelShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        EnumSelectEditorPart<RenderOrder> renderOrderSelect = new EnumSelectEditorPart<>("Render order", "renderOrder",
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", RenderOrder.values());
        graphNodeEditor.addGraphEditorPart(renderOrderSelect);

        ModelShadersEditorPart graphBoxPart = new ModelShadersEditorPart();
        graphNodeEditor.addGraphEditorPart(graphBoxPart);
    }
}