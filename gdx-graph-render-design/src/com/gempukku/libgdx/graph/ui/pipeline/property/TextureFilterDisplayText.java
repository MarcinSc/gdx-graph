package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.common.Function;

public class TextureFilterDisplayText implements Function<Texture.TextureFilter, String> {
    @Override
    public String evaluate(Texture.TextureFilter textureFilter) {
        switch (textureFilter) {
            case Linear:
                return "Linear";
            case Nearest:
                return "Nearest";
            case MipMap:
                return "Mipmap";
            case MipMapLinearLinear:
                return "Mipmap Linear Linear";
            case MipMapLinearNearest:
                return "Mipmap Linear Nearest";
            case MipMapNearestLinear:
                return "Mipmap Nearest Linear";
            case MipMapNearestNearest:
                return "Mipmap Nearest Nearest";
        }
        return "Unknown";
    }
}
