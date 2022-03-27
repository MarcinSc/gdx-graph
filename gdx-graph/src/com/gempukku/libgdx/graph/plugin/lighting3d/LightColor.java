package com.gempukku.libgdx.graph.plugin.lighting3d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class LightColor {
    private float red;
    private float green;
    private float blue;

    public LightColor(Color color) {
        this(color.r, color.g, color.b);
    }

    public LightColor(float red, float green, float blue) {
        this.red = MathUtils.clamp(red, 0, 1);
        this.green = MathUtils.clamp(green, 0, 1);
        this.blue = MathUtils.clamp(blue, 0, 1);
    }

    public void set(Color color) {
        set(color.r, color.g, color.b);
    }

    public void set(float red, float green, float blue) {
        this.red = MathUtils.clamp(red, 0, 1);
        this.green = MathUtils.clamp(green, 0, 1);
        this.blue = MathUtils.clamp(blue, 0, 1);
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }
}
