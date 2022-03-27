package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WhitePixel {
    public static Texture texture;
    public static TextureRegion textureRegion;

    public static void initialize() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        texture = new Texture(pixmap);
        textureRegion = new TextureRegion(texture);

        pixmap.dispose();
    }

    public static void dispose() {
        texture.dispose();
    }

    private WhitePixel() {

    }
}
