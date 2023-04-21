package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;

public abstract class ValueGraphBoxProducerDefault extends GdxGraphNodeEditorProducer {
    protected NodeConfiguration configuration;

    public ValueGraphBoxProducerDefault(MenuNodeConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        graphNodeEditor.addGraphBoxPart(createValuePart());
    }

    protected abstract DefaultGraphNodeEditorPart createValuePart();
}
