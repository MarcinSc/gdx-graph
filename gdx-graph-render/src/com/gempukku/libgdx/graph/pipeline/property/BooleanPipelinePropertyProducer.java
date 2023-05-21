package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.DefaultPipelineProperty;

import java.util.function.Supplier;

public class BooleanPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public String getType() {
        return PipelineFieldType.Boolean;
    }

    @Override
    public DefaultPipelineProperty createProperty(JsonValue data) {
        final boolean x = data.getBoolean("value");
        return new DefaultPipelineProperty("Boolean",
                new Supplier<Boolean>() {
                    @Override
                    public Boolean get() {
                        return x;
                    }
                });
    }
}
