package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Texture;

public interface TextureShaderPropertySource extends ShaderPropertySource {
    Texture.TextureFilter getMinFilter();

    Texture.TextureFilter getMagFilter();
}
