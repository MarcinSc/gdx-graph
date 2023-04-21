package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

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
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final ModelShaderPreviewBoxPart previewBoxPart = new ModelShaderPreviewBoxPart("preview.modelType");

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"Object space", "World space"});
        graphNodeEditor.addGraphBoxPart(positionType);

        EnumSelectEditorPart<BasicShader.Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", BasicShader.Culling.values());
        graphNodeEditor.addGraphBoxPart(cullingBox);

        EnumSelectEditorPart<BasicShader.DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", BasicShader.DepthTesting.values());
        graphNodeEditor.addGraphBoxPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphBoxPart(writeDepthBox);

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }
}
