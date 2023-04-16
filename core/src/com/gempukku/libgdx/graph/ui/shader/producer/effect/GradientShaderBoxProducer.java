package com.gempukku.libgdx.graph.ui.shader.producer.effect;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.ClampMethod;
import com.gempukku.libgdx.graph.shader.config.common.effect.GradientShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GradientEditorPart;

public class GradientShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public GradientShaderBoxProducer() {
        super(new GradientShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        graphNodeEditor.addGraphBoxPart(
                new GradientEditorPart("points", "gdx-graph"));
        graphNodeEditor.addGraphBoxPart(
                new EnumSelectEditorPart<>("Clamp method:", "clamp",
                        new StringifyEnum<>(), ClampMethod.values()));
    }
}
