package com.gempukku.libgdx.graph.shader.setting;

import static com.badlogic.gdx.graphics.GL20.*;

public enum BlendingFactor {
    zero(GL_ZERO, "zero"), one(GL_ONE, "one"),
    source_alpha(GL_SRC_ALPHA, "src alpha"), one_minus_source_alpha(GL_ONE_MINUS_SRC_ALPHA, "1-src alpha"),
    destination_alpha(GL_DST_ALPHA, "dst alpha"), one_minus_destination_alpha(GL_ONE_MINUS_DST_ALPHA, "1-dst alpha"),
    source_color(GL_SRC_COLOR, "src color"), one_mius_source_color(GL_ONE_MINUS_SRC_COLOR, "1-src color"),
    destination_color(GL_DST_COLOR, "dst color"), one_minus_destination_color(GL_ONE_MINUS_DST_COLOR, "1-dst color");

    private final int factor;
    private final String text;

    BlendingFactor(int factor, String text) {
        this.factor = factor;
        this.text = text;
    }

    public int getFactor() {
        return factor;
    }

    public String toString() {
        return text;
    }
}
