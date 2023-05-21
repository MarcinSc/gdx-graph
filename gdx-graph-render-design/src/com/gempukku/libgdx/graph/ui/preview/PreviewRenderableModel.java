package com.gempukku.libgdx.graph.ui.preview;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface PreviewRenderableModel extends RenderableModel {
    void updateModel(ObjectMap<String, BasicShader.Attribute> attributeMap, ObjectMap<String, ShaderPropertySource> propertySourceMap,
            PropertyContainer localPropertyContainer);
}
