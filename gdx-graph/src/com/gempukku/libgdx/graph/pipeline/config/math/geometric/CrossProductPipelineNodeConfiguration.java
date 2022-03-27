package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.data.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.producer.node.GraphNodeOutputImpl;

public class CrossProductPipelineNodeConfiguration extends NodeConfigurationImpl {
    public CrossProductPipelineNodeConfiguration() {
        super("CrossProduct", "Cross product", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl("a", "A", true, PipelineFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl("b", "B", true, PipelineFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl("output", "Result", PipelineFieldType.Vector3));
    }
}
