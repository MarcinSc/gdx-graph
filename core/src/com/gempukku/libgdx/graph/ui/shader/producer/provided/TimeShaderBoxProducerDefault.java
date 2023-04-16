package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;

public class TimeShaderBoxProducerDefault extends DefaultMenuGraphNodeEditorProducer {
    public TimeShaderBoxProducerDefault() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        FloatEditorPart multiplierPart = new FloatEditorPart("Multiplier", "multiplier", 1f, null);
        graphNodeEditor.addGraphBoxPart(multiplierPart);
    }
}
