package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.part.IndexBoxPart;


public class IndexedBoxProducer extends GraphBoxProducerImpl {
    public IndexedBoxProducer(NodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        GraphBoxImpl result = createGraphBox(id);
        addConfigurationInputsAndOutputs(result);
        IndexBoxPart indexPart = new IndexBoxPart("Index", "index");
        indexPart.initialize(data);
        result.addGraphBoxPart(indexPart);
        return result;
    }
}
