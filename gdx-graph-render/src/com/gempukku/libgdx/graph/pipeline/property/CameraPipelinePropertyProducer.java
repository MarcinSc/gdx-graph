package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.DefaultPipelineProperty;

import java.util.function.Supplier;

public class CameraPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public String getType() {
        return PipelineFieldType.Camera;
    }

    @Override
    public DefaultPipelineProperty createProperty(JsonValue data) {
        return new DefaultPipelineProperty(PipelineFieldType.Camera,
                new Supplier<Camera>() {
                    @Override
                    public Camera get() {
                        return null;
                    }
                });
    }
}
