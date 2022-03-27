package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class GdxCompatibilityUtils {
    public static String getShaderVersionCode() {
        // To support OpenGL, work with this:
        // config.useGL30 = true;
        // ShaderProgram.prependVertexCode = "#version 140\n#define varying out\n#define attribute in\n";
        // ShaderProgram.prependFragmentCode = "#version 140\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\n";
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            return "#version 120\n";
        else
            return "#version 100\n";
    }
}
