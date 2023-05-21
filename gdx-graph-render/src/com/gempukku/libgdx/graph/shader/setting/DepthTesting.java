package com.gempukku.libgdx.graph.shader.setting;

import com.badlogic.gdx.graphics.GL20;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;

public enum DepthTesting {
    less(GL20.GL_LESS, "less"), less_or_equal(GL20.GL_LEQUAL, "less or equal"),
    equal(GL20.GL_EQUAL, "equal"), not_equal(GL20.GL_NOTEQUAL, "not equal"), greater_or_equal(GL20.GL_GEQUAL, "greater or equal"),
    greater(GL20.GL_GREATER, "greater"), never(GL20.GL_NEVER, "never"), always(GL20.GL_ALWAYS, "always"),
    disabled(0, "disabled");

    private final int depthFunction;
    private final String text;

    DepthTesting(int depthFunction, String text) {
        this.depthFunction = depthFunction;
        this.text = text;
    }

    public void setDepthTest(OpenGLContext renderContext, float depthNear, float depthFar) {
        renderContext.setDepthTest(depthFunction, depthNear, depthFar);
    }

    public String toString() {
        return text;
    }
}
