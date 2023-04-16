package com.gempukku.libgdx.graph.ui.shader.producer.effect;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.effect.DitherShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;

public class DitherShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public DitherShaderBoxProducer() {
        super(new DitherShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        SelectEditorPart ditherSizeSelect = new SelectEditorPart("Dither size:", "ditherSize", "2", "4", "8");
        graphNodeEditor.addGraphBoxPart(ditherSizeSelect);
    }
}
