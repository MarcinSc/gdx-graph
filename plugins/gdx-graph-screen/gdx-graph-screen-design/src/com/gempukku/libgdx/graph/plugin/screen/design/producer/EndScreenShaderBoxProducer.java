package com.gempukku.libgdx.graph.plugin.screen.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.plugin.screen.config.EndScreenShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.screen.design.ScreenShaderPreviewBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.BlendingBoxPart;
import com.gempukku.libgdx.graph.ui.part.SectionBoxPart;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class EndScreenShaderBoxProducer extends GraphBoxProducerImpl {
    public EndScreenShaderBoxProducer() {
        super(new EndScreenShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final ScreenShaderPreviewBoxPart previewBoxPart = new ScreenShaderPreviewBoxPart();
        previewBoxPart.initialize(data);

        GraphBoxImpl result = new GraphBoxImpl(id, getConfiguration()) {
            @Override
            public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
                if (event.isData() || event.isStructure()) {
                    previewBoxPart.graphChanged(hasErrors, graph);
                }
            }
        };

        addConfigurationInputsAndOutputs(result);

        result.addGraphBoxPart(new SectionBoxPart("Rendering config"));

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        blendingBox.initialize(data);
        result.addGraphBoxPart(blendingBox);

        result.addGraphBoxPart(new SectionBoxPart("Preview"));

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }
}
