package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.SpotLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.IndexEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class SpotlightBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public SpotlightBoxProducer() {
        super(new SpotLightShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        StringEditorPart envId = new StringEditorPart("Env id: ", "id");
        graphNodeEditor.addGraphBoxPart(envId);

        IndexEditorPart indexPart = new IndexEditorPart("Index", "index");
        graphNodeEditor.addGraphBoxPart(indexPart);
    }
}