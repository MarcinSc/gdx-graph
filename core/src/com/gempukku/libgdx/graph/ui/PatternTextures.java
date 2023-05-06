package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PatternTextures {
    public static PatternTextures sharedInstance;

    public static void initializeShared(FileHandleResolver assetResolver) {
        sharedInstance = new PatternTextures(assetResolver);
    }

    public static void disposeShared() {
        sharedInstance.dispose();
    }

    public final Texture texture;
    public final TextureRegion textureRegion;

    private PatternTextures(FileHandleResolver assetResolver) {
        texture = new Texture(assetResolver.resolve("image/checkerboard.png"));
        textureRegion = new TextureRegion(texture);
    }

    public void dispose() {
        texture.dispose();
    }
}
