package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.lighting3d.config.EndShadowShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.producer.ModelShaderPreviewEditorPart;
import com.gempukku.libgdx.graph.shader.setting.Culling;
import com.gempukku.libgdx.graph.shader.setting.DepthTesting;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class EndShadowShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public EndShadowShaderEditorProducer() {
        super(new EndShadowShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final ModelShaderPreviewEditorPart previewBoxPart = new ModelShaderPreviewEditorPart("preview.modelType");

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"Object space", "World space"});
        graphNodeEditor.addGraphEditorPart(positionType);

        EnumSelectEditorPart<Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling",
                Culling.back,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(Culling.values()));
        graphNodeEditor.addGraphEditorPart(cullingBox);

        EnumSelectEditorPart<DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", DepthTesting.less,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(DepthTesting.values()));
        graphNodeEditor.addGraphEditorPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(writeDepthBox);

        graphNodeEditor.addGraphEditorPart(previewBoxPart);
    }
}
