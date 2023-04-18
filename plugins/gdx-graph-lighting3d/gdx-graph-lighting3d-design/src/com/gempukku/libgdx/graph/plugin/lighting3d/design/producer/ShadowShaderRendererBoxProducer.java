package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.ShadowShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class ShadowShaderRendererBoxProducer extends GdxGraphNodeEditorProducer {
    public ShadowShaderRendererBoxProducer() {
        super(new ShadowShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        EnumSelectEditorPart<RenderOrder> renderOrderSelect = new EnumSelectEditorPart("Render order", "renderOrder",
                new ToStringEnum<RenderOrder>(), RenderOrder.values());
        graphNodeEditor.addGraphBoxPart(renderOrderSelect);

        StringEditorPart envId = new StringEditorPart("Env id: ", "id");
        graphNodeEditor.addGraphBoxPart(envId);

        ShadowShadersBoxPart graphBoxPart = new ShadowShadersBoxPart();
        graphNodeEditor.addGraphBoxPart(graphBoxPart);
    }
}
