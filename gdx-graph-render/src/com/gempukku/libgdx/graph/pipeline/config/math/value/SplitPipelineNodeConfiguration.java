package com.gempukku.libgdx.graph.pipeline.config.math.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class SplitPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public SplitPipelineNodeConfiguration() {
        super("Split", "Split", "Math/Value");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, Color, Vector3, Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("x", "X", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("y", "Y", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("z", "Z", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("w", "W", Float));
    }
}
