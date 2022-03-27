package com.gempukku.libgdx.graph.plugin.lighting3d.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.plugin.lighting3d.config.EndShadowShaderNodeConfiguration;
import com.gempukku.libgdx.graph.plugin.models.design.producer.ModelShaderPreviewBoxPart;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.part.EnumSelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.part.StringifyEnum;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;

public class EndShadowShaderBoxProducer extends GraphBoxProducerImpl {
    public EndShadowShaderBoxProducer() {
        super(new EndShadowShaderNodeConfiguration());
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        final ModelShaderPreviewBoxPart previewBoxPart = new ModelShaderPreviewBoxPart();
        previewBoxPart.initialize(data);

        GraphBoxImpl result = new GraphBoxImpl(id, getConfiguration()) {
            @Override
            public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode, ? extends GraphConnection, ? extends GraphProperty> graph) {
                if (event.isData() || event.isStructure()) {
                    previewBoxPart.graphChanged(hasErrors, graph);
                }
            }
        };

        SelectBoxPart positionType = new SelectBoxPart("Position", "positionType",
                "Object space", "World space");
        positionType.initialize(data);
        result.addGraphBoxPart(positionType);

        addConfigurationInputsAndOutputs(result);
        EnumSelectBoxPart cullingBox = new EnumSelectBoxPart("Culling", "culling", new StringifyEnum<BasicShader.Culling>(), BasicShader.Culling.values());
        cullingBox.initialize(data);
        result.addGraphBoxPart(cullingBox);

        EnumSelectBoxPart depthTestBox = new EnumSelectBoxPart("DepthTest", "depthTest", new StringifyEnum<BasicShader.DepthTesting>(), BasicShader.DepthTesting.values());
        depthTestBox.initialize(data);
        result.addGraphBoxPart(depthTestBox);

        CheckboxBoxPart writeDepthBox = new CheckboxBoxPart("Write depth", "depthWrite");
        writeDepthBox.initialize(data);
        result.addGraphBoxPart(writeDepthBox);

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }
}
