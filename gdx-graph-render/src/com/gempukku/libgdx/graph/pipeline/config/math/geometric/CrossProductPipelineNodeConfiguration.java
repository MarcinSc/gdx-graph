package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class CrossProductPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public CrossProductPipelineNodeConfiguration() {
        super("CrossProduct", "Cross product", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("a", "A", true, PipelineFieldType.Vector3));
        addNodeInput(
                new DefaultGraphNodeInput("b", "B", true, PipelineFieldType.Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", PipelineFieldType.Vector3));
    }
}
