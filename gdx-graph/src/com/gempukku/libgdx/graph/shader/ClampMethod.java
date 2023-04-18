package com.gempukku.libgdx.graph.shader;

public enum ClampMethod {
    Normal("Normal"), Repeat("Repeat"), PingPong("Ping-pong");

    private String text;

    ClampMethod(String text) {
        this.text = text;
    }

    public String getShaderCode(String input) {
        if (this == Normal)
            return "clamp(" + input + ", 0.0, 1.0)";
        if (this == Repeat)
            return "fract(" + input + ")";
        if (this == PingPong)
            return "(fract(" + input + " / 2.0) < 0.5) ? fract(" + input + ") : (1.0 - fract(" + input + "))";
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
