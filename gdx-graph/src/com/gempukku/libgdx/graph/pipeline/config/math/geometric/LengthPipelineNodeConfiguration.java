package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class LengthPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public LengthPipelineNodeConfiguration() {
        super("Length", "Length", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result", PipelineFieldType.Float));
    }
}
