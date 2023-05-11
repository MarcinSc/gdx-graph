package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderGraphType;
import com.gempukku.libgdx.graph.plugin.models.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class ModelShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ModelShaderRendererEditorProducer() {
        super(new ModelShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        EnumSelectEditorPart<RenderOrder> renderOrderSelect = new EnumSelectEditorPart<>("Render order", "renderOrder", RenderOrder.Shader_Unordered,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(RenderOrder.values()));
        graphNodeEditor.addGraphEditorPart(renderOrderSelect);

        ShaderGraphEditorPart graphBoxPart = new ShaderGraphEditorPart((UIGraphType) GraphTypeRegistry.findGraphType(ModelShaderGraphType.TYPE));
        graphNodeEditor.addGraphEditorPart(graphBoxPart);
    }
}
