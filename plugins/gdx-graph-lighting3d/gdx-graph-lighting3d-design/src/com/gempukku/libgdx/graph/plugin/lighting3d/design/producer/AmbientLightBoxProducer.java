package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.lighting3d.producer.AmbientLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class AmbientLightBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public AmbientLightBoxProducer() {
        super(new AmbientLightShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        StringEditorPart envId = new StringEditorPart("Env id: ", "id");
        graphNodeEditor.addGraphBoxPart(envId);
    }
}