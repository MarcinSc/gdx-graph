package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.gempukku.libgdx.graph.plugin.models.config.EndModelShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.BlendingBoxPart;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class EndModelShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public EndModelShaderBoxProducer() {
        super(new EndModelShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final ModelShaderPreviewBoxPart previewBoxPart = new ModelShaderPreviewBoxPart("preview.modelType");

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"Object space", "World space"});
        graphNodeEditor.addGraphBoxPart(positionType);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Rendering config", "gdx-graph-section-label", "default"));

        EnumSelectEditorPart<BasicShader.Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", new ToStringEnum<>(),
                "gdx-graph-property-label", "gdx-graph-property",
                BasicShader.Culling.values());
        graphNodeEditor.addGraphBoxPart(cullingBox);

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        graphNodeEditor.addGraphBoxPart(blendingBox);

        EnumSelectEditorPart<BasicShader.DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", BasicShader.DepthTesting.values());
        graphNodeEditor.addGraphBoxPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphBoxPart(writeDepthBox);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Preview", "gdx-graph-section-label", "default"));

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }
}
