package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class Vector2PipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public String getType() {
        return PipelineFieldType.Vector2;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        final float x = data.getFloat("x");
        final float y = data.getFloat("y");
        return new WritablePipelineProperty(PipelineFieldType.Vector2,
                new Supplier<Vector2>() {
                    @Override
                    public Vector2 get() {
                        return new Vector2(x, y);
                    }
                });
    }
}
