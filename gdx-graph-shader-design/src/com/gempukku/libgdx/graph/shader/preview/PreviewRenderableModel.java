package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface PreviewRenderableModel extends RenderableModel {
    void updateModel(ObjectMap<String, BasicShader.Attribute> attributeMap, ObjectMap<String, ShaderPropertySource> propertySourceMap,
            PropertyContainer localPropertyContainer);

    Actor getCustomizationActor();
}
