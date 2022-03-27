package com.gempukku.libgdx.graph.sprite;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteStateData {
    public final TextureRegion sprites;
    public final int spriteWidth;
    public final int spriteHeight;
    public final float speed;
    public final boolean looping;

    public SpriteStateData(TextureRegion sprites, int spriteWidth, int spriteHeight, float speed, boolean looping) {
        this.sprites = sprites;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.speed = speed;
        this.looping = looping;
    }
}