package com.gempukku.libgdx.graph.artemis.text;

public enum TextVerticalAlignment {
    top, center, bottom;


    public float apply(float height, float availableHeight) {
        switch (this) {
            case bottom:
                return availableHeight - height;
            case center:
                return (availableHeight - height) / 2;
            default:
                return 0;
        }
    }
}
