package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.plugin.particles.config.EndParticlesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.*;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class EndParticlesShaderBoxProducer extends GraphBoxProducerImpl {
    public EndParticlesShaderBoxProducer() {
        super(new EndParticlesShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final ParticlesShaderPreviewBoxPart previewBoxPart = new ParticlesShaderPreviewBoxPart();
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

        EnumSelectBoxPart cullingBox = new EnumSelectBoxPart("Culling", "culling", new StringifyEnum<BasicShader.Culling>(), BasicShader.Culling.values());
        cullingBox.initialize(data);
        result.addGraphBoxPart(cullingBox);

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        blendingBox.initialize(data);
        result.addGraphBoxPart(blendingBox);

        EnumSelectBoxPart depthTestBox = new EnumSelectBoxPart("DepthTest", "depthTest", new StringifyEnum<BasicShader.DepthTesting>(), BasicShader.DepthTesting.values());
        depthTestBox.initialize(data);
        result.addGraphBoxPart(depthTestBox);

        CheckboxBoxPart writeDepthBox = new CheckboxBoxPart("Write depth", "depthWrite");
        writeDepthBox.initialize(data);
        result.addGraphBoxPart(writeDepthBox);

        result.addGraphBoxPart(new SectionBoxPart("Preview"));

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }
}
