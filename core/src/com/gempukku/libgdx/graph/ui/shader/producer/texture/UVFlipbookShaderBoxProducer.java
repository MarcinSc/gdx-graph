package com.gempukku.libgdx.graph.ui.shader.producer.texture;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;

public class UVFlipbookShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public UVFlipbookShaderBoxProducer() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        CheckboxEditorPart invertX = new CheckboxEditorPart("Invert X", "invertX");
        graphNodeEditor.addGraphBoxPart(invertX);
        CheckboxEditorPart invertY = new CheckboxEditorPart("Invert Y", "invertY");
        graphNodeEditor.addGraphBoxPart(invertY);
    }
}
