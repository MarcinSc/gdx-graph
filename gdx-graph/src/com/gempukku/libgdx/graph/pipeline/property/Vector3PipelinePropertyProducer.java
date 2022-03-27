package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class Vector3PipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public String getType() {
        return PipelineFieldType.Vector3;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        final float z = data.getFloat("z");
        return new WritablePipelineProperty(PipelineFieldType.Vector3,
                new Supplier<Vector3>() {
                    @Override
                    public Vector3 get() {
                        return new Vector3(x, y, z);
                    }
                });
    }
}
