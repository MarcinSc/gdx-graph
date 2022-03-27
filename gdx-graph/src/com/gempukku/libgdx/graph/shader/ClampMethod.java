package com.gempukku.libgdx.graph.shader;

public enum ClampMethod {
    Normal, Repeat, PingPong;

    public String getShaderCode(String input) {
        if (this == Normal)
            return "clamp(" + input + ", 0.0, 1.0)";
        if (this == Repeat)
            return "fract(" + input + ")";
        if (this == PingPong)
            return "(fract(" + input + " / 2.0) < 0.5) ? fract(" + input + ") : (1.0 - fract(" + input + "))";
        return null;
    }
}
