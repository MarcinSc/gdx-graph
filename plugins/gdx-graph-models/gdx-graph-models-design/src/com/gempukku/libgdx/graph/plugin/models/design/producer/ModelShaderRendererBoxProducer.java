package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.pipeline.RenderOrder;
import com.gempukku.libgdx.graph.plugin.models.config.ModelShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class ModelShaderRendererBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public ModelShaderRendererBoxProducer() {
        super(new ModelShaderRendererPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        EnumSelectEditorPart<RenderOrder> renderOrderSelect = new EnumSelectEditorPart<>("Render order", "renderOrder",
                new ToStringEnum<>(), RenderOrder.values());
        graphNodeEditor.addGraphBoxPart(renderOrderSelect);

        ModelShadersBoxPart graphBoxPart = new ModelShadersBoxPart();
        graphNodeEditor.addGraphBoxPart(graphBoxPart);
    }
}
