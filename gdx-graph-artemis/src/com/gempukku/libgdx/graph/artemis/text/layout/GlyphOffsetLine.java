package com.gempukku.libgdx.graph.artemis.text.layout;

import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;

public interface GlyphOffsetLine {
    float getWidth();

    float getHeight();

    int getGlyphCount();

    int getStartIndex();

    boolean isEndingWithLineBreak();

    float getGlyphXAdvance(int glyphIndex);

    float getGlyphYAdvance(int glyphIndex);

    TextStyle getGlyphStyle(int glyphIndex);
}
