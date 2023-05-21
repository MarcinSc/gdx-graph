package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.shader.lighting3d.ShadowShaderGraphType;
import com.gempukku.libgdx.graph.shader.lighting3d.producer.ShadowShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.ShaderGraphEditorPart;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class ShadowShaderRendererEditorProducer extends GdxGraphNodeEditorProducer {
    public ShadowShaderRendererEditorProducer() {
        super(new ShadowShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        EnumSelectEditorPart<RenderOrder> renderOrderSelect = new EnumSelectEditorPart<>("Render order", "renderOrder", RenderOrder.Shader_Unordered,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(RenderOrder.values()));
        graphNodeEditor.addGraphEditorPart(renderOrderSelect);

        StringEditorPart envId = new StringEditorPart("Env id: ", "id", "", "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphEditorPart(envId);

        ShaderGraphEditorPart graphBoxPart = new ShaderGraphEditorPart((UIGraphType) GraphTypeRegistry.findGraphType(ShadowShaderGraphType.TYPE));
        graphNodeEditor.addGraphEditorPart(graphBoxPart);
    }
}
