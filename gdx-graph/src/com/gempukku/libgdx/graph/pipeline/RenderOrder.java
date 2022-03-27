package com.gempukku.libgdx.graph.pipeline;

public enum RenderOrder {
    Shader_Unordered("Shader, objects unordered"),
    Shader_Back_To_Front("Shader, then objects back to front"),
    Shader_Front_To_Back("Shader, then objects front to back"),
    Back_To_Front("Objects back to front"),
    Front_To_Back("Objects front to back");

    private final String text;

    RenderOrder(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
