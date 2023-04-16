package com.gempukku.libgdx.graph.pipeline.config.math.common;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;

public class SmoothstepPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SmoothstepPipelineNodeConfiguration() {
        super("Smoothstep", "Smoothstep", "Math/Common");
        addNodeInput(
                new DefaultGraphNodeInput("edge0", "Edge 0", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("edge1", "Edge 1", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new MathCommonOutputTypeFunction(PipelineFieldType.Float, new String[]{"input"}, new String[]{"edge0", "edge1"}),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3, PipelineFieldType.Color));
    }
}
