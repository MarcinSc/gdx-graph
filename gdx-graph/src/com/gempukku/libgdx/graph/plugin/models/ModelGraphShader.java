package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;

public class ModelGraphShader extends GraphShader {
    public ModelGraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
    }

    public void render(ShaderContextImpl shaderContext, RenderableModel renderableModel) {
        renderableModel.prepareToRender(shaderContext);

        shaderContext.setRenderableModel(renderableModel);
        shaderContext.setLocalPropertyContainer(renderableModel.getPropertyContainer(getTag()));

        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        renderableModel.render(shaderContext.getCamera(), program, getPropertyToLocationMapping());
    }
}
