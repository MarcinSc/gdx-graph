package com.gempukku.libgdx.graph.shader.property;

public enum PropertyLocation {
    Attribute("Attribute"), Uniform("Uniform"), Global_Uniform("Global uniform");

    private String text;

    PropertyLocation(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
