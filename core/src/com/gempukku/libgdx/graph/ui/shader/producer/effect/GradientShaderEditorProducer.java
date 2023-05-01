package com.gempukku.libgdx.graph.ui.shader.producer.effect;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.config.common.effect.GradientShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GradientEditorPart;

public class GradientShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public GradientShaderEditorProducer() {
        super(new GradientShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        graphNodeEditor.addGraphEditorPart(
                new GradientEditorPart("points", "gdx-graph"));
        graphNodeEditor.addGraphEditorPart(
                new EnumSelectEditorPart<>("Clamp method:", "clamp",
                        ClampMethod.Normal,
                        new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(ClampMethod.values())));
    }
}
