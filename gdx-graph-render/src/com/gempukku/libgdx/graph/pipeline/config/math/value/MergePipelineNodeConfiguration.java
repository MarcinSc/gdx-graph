package com.gempukku.libgdx.graph.pipeline.config.math.value;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.*;

public class MergePipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public MergePipelineNodeConfiguration() {
        super("Merge", "Merge", "Math/Value");
        addNodeInput(
                new DefaultGraphNodeInput("x", "X", Float));
        addNodeInput(
                new DefaultGraphNodeInput("y", "Y", Float));
        addNodeInput(
                new DefaultGraphNodeInput("z", "Z", Float));
        addNodeInput(
                new DefaultGraphNodeInput("w", "W", Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("v2", "Vector2", Vector2));
        addNodeOutput(
                new DefaultGraphNodeOutput("v3", "Vector3", Vector3));
        addNodeOutput(
                new DefaultGraphNodeOutput("color", "Color", Color));
    }
}
