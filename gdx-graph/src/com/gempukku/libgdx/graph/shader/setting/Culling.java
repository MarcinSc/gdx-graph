package com.gempukku.libgdx.graph.shader.setting;

import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;

import static com.badlogic.gdx.graphics.GL20.*;

public enum Culling {
    back(GL_BACK, "back"), front(GL_FRONT, "front"), none(GL_NONE, "none");

    private final int cullFace;
    private final String text;

    Culling(int cullFace, String text) {
        this.cullFace = cullFace;
        this.text = text;
    }

    public void setCullFace(OpenGLContext renderContext) {
        renderContext.setCullFace(cullFace);
    }

    @Override
    public String toString() {
        return text;
    }
}
