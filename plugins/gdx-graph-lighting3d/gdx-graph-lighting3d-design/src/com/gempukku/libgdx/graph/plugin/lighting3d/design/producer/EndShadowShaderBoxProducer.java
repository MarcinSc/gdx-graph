package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.lighting3d.config.EndShadowShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderPreviewBoxPart;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class EndShadowShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public EndShadowShaderBoxProducer() {
        super(new EndShadowShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final ModelShaderPreviewBoxPart previewBoxPart = new ModelShaderPreviewBoxPart();

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "Object space", "World space");
        graphNodeEditor.addGraphBoxPart(positionType);

        EnumSelectEditorPart cullingBox = new EnumSelectEditorPart("Culling", "culling", new ToStringEnum<>(), BasicShader.Culling.values());
        graphNodeEditor.addGraphBoxPart(cullingBox);

        EnumSelectEditorPart depthTestBox = new EnumSelectEditorPart("DepthTest", "depthTest", new ToStringEnum<>(), BasicShader.DepthTesting.values());
        graphNodeEditor.addGraphBoxPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite");
        graphNodeEditor.addGraphBoxPart(writeDepthBox);

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }
}
