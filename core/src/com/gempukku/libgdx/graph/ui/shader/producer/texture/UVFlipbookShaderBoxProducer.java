package com.gempukku.libgdx.graph.ui.shader.producer.texture;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVFlipbookShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;

public class UVFlipbookShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public UVFlipbookShaderBoxProducer() {
        super(new UVFlipbookShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        CheckboxEditorPart invertX = new CheckboxEditorPart("Invert X", "invertX");
        graphNodeEditor.addGraphBoxPart(invertX);
        CheckboxEditorPart invertY = new CheckboxEditorPart("Invert Y", "invertY");
        graphNodeEditor.addGraphBoxPart(invertY);
    }
}
