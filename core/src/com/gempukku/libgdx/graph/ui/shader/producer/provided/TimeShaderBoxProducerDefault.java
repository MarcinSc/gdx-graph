package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;

public class TimeShaderBoxProducerDefault extends GdxGraphNodeEditorProducer {
    public TimeShaderBoxProducerDefault() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        FloatEditorPart multiplierPart = new FloatEditorPart("Multiplier", "multiplier", 1f, null);
        graphNodeEditor.addGraphBoxPart(multiplierPart);
    }
}
