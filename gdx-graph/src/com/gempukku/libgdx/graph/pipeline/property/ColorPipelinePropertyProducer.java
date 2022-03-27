package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;

import java.util.function.Supplier;

public class ColorPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public String getType() {
        return PipelineFieldType.Color;
    }

    @Override
    public WritablePipelineProperty createProperty(JsonValue data) {
        final Color color = Color.valueOf(data.getString("color"));
        return new WritablePipelineProperty(PipelineFieldType.Color,
                new Supplier<Color>() {
                    @Override
                    public Color get() {
                        return color;
                    }
                });
    }
}
