package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface PreviewModelProducer {
    PreviewRenderableModel create(ObjectMap<String, BasicShader.Attribute> attributeMap, ObjectMap<String, ShaderPropertySource> propertySourceMap,
                            PropertyContainer localPropertyContainer);
}
