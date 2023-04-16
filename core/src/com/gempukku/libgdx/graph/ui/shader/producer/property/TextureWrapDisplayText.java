package com.gempukku.libgdx.graph.ui.shader.producer.property;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.common.Function;

public class TextureWrapDisplayText implements Function<Texture.TextureWrap, String> {
    @Override
    public String evaluate(Texture.TextureWrap textureWrap) {
        switch (textureWrap) {
            case ClampToEdge:
                return "Clamp to edge";
            case Repeat:
                return "Repeat";
            case MirroredRepeat:
                return "Mirrored repeat";
        }
        return "Unknown";
    }
}
