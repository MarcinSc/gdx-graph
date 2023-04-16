package com.gempukku.libgdx.graph.ui.shader.producer.math.value;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapValueShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.CurveEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class RemapValueShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public RemapValueShaderBoxProducer() {
        super(new RemapValueShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        graphNodeEditor.addGraphBoxPart(
                new CurveEditorPart("points", "gdx-graph"));
        graphNodeEditor.addGraphBoxPart(
                new EnumSelectEditorPart<>("Clamp method:", "clamp",
                        new StringifyEnum<>(), ClampMethod.values()));
    }
}
