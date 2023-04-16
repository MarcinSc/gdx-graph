package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;

public abstract class ValueGraphBoxProducerDefault extends DefaultMenuGraphNodeEditorProducer {
    protected NodeConfiguration configuration;

    public ValueGraphBoxProducerDefault(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        graphNodeEditor.addGraphBoxPart(createValuePart(skin));
    }

    protected abstract DefaultGraphNodeEditorPart createValuePart(Skin skin);
}
