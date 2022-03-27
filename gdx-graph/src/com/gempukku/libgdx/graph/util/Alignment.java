package com.gempukku.libgdx.graph.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

public enum Alignment {
    center(1 << 0),
    top(1 << 1),
    bottom(1 << 2),
    left(1 << 3),
    right(1 << 4),

    topLeft(top.align | left.align),
    topRight(top.align | right.align),
    bottomLeft(bottom.align | left.align),
    bottomRight(bottom.align | right.align);

    private static final Vector2 temp = new Vector2();

    private final int align;

    Alignment(int align) {
        this.align = align;
    }

    public boolean isLeft() {
        return (align & left.align) != 0;
    }

    public boolean isRight() {
        return (align & right.align) != 0;
    }

    public boolean isTop() {
        return (align & top.align) != 0;
    }

    public boolean isBottom() {
        return (align & bottom.align) != 0;
    }

    public boolean isCenterVertical() {
        return (align & top.align) == 0 && (align & bottom.align) == 0;
    }

    public boolean isCenterHorizontal() {
        return (align & left.align) == 0 && (align & right.align) == 0;
    }

    public Vector2 apply(float width, float height, float availableWidth, float availableHeight) {
        float spaceX = Align.isLeft(align) ? 0 : (Align.isRight(align) ? (availableWidth - width) : ((availableWidth - width) / 2));
        float spaceY = Align.isTop(align) ? 0 : (Align.isBottom(align) ? (availableHeight - height) : ((availableHeight - height) / 2));
        return temp.set(spaceX, spaceY);
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(13);
        if ((align & top.align) != 0)
            buffer.append("top,");
        else if ((align & bottom.align) != 0)
            buffer.append("bottom,");
        else
            buffer.append("center,");
        if ((align & left.align) != 0)
            buffer.append("left");
        else if ((align & right.align) != 0)
            buffer.append("right");
        else
            buffer.append("center");
        return buffer.toString();
    }
}
