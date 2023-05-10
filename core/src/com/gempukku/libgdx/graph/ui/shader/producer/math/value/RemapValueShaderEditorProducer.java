package com.gempukku.libgdx.graph.ui.shader.producer.math.value;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapValueShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.setting.ClampMethod;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CurveEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class RemapValueShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public RemapValueShaderEditorProducer() {
        super(new RemapValueShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        graphNodeEditor.addGraphEditorPart(
                new CurveEditorPart("points", "gdx-graph"));
        graphNodeEditor.addGraphEditorPart(
                new EnumSelectEditorPart<>("Clamp method:", "clamp",
                        ClampMethod.Normal,
                        new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(ClampMethod.values())));
    }
}
