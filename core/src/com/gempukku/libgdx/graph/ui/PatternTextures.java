package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PatternTextures {
    public static PatternTextures sharedInstance;

    public static void initializeShared() {
        sharedInstance = new PatternTextures();
    }

    public static void disposeShared() {
        sharedInstance.dispose();
    }

    public final Texture texture;
    public final TextureRegion textureRegion;

    private PatternTextures() {
        texture = new Texture(Gdx.files.internal("image/checkerboard.png"));
        textureRegion = new TextureRegion(texture);
    }

    public void dispose() {
        texture.dispose();
    }
}
