package com.gempukku.libgdx.graph.plugin.callback.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.callback.producer.RenderCallbackPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.StringEditorPart;

public class RenderCallbackBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public RenderCallbackBoxProducer() {
        super(new RenderCallbackPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        StringEditorPart callbackId = new StringEditorPart("Callback id: ", "callbackId");
        graphNodeEditor.addGraphBoxPart(callbackId);
    }
}