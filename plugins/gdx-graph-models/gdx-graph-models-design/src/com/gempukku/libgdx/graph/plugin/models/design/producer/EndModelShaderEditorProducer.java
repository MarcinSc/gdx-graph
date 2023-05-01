package com.gempukku.libgdx.graph.plugin.models.design.producer;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.models.config.EndModelShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.BlendingEditorPart;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class EndModelShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public EndModelShaderEditorProducer() {
        super(new EndModelShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final ModelShaderPreviewEditorPart previewBoxPart = new ModelShaderPreviewEditorPart("preview.modelType");

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"Object space", "World space"});
        graphNodeEditor.addGraphEditorPart(positionType);

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Rendering config", "gdx-graph-section-label", "default"));

        EnumSelectEditorPart<BasicShader.Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", BasicShader.Culling.back,
                new ToStringEnum<>(),
                "gdx-graph-property-label", "gdx-graph-property",
                new Array<>(BasicShader.Culling.values()));
        graphNodeEditor.addGraphEditorPart(cullingBox);

        BlendingEditorPart blendingBox = new BlendingEditorPart();
        graphNodeEditor.addGraphEditorPart(blendingBox);

        EnumSelectEditorPart<BasicShader.DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", BasicShader.DepthTesting.less,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(BasicShader.DepthTesting.values()));
        graphNodeEditor.addGraphEditorPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(writeDepthBox);

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Preview", "gdx-graph-section-label", "default"));

        graphNodeEditor.addGraphEditorPart(previewBoxPart);
    }
}
