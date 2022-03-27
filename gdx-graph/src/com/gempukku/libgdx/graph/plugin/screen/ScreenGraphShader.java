package com.gempukku.libgdx.graph.plugin.screen;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.pipeline.producer.FullScreenRender;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;

public class ScreenGraphShader extends GraphShader {
    private final MapWritablePropertyContainer propertyContainer = new MapWritablePropertyContainer();

    public ScreenGraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
        setCulling(BasicShader.Culling.back);
        setDepthTesting(BasicShader.DepthTesting.disabled);
        setDepthWriting(false);
    }

    public void render(ShaderContext shaderContext, FullScreenRender fullScreenRender) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        fullScreenRender.renderFullScreen(program);
    }

    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }
}
