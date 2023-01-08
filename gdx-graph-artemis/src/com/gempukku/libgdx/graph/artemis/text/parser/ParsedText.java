package com.gempukku.libgdx.graph.artemis.text.parser;

import com.badlogic.gdx.utils.Disposable;

/**
 * Parsed text that provides information about the unbreakable chunks, as well as
 * a style of each glyph to render.
 */
public interface ParsedText extends Disposable {
    /**
     * Returns length of next unbreakable run of glyphs, starting at startIndex.
     * If this method returns -1, it means that there is no more glyphs left.
     *
     * @param startIndex
     * @return
     */
    int getNextUnbreakableChunkLength(int startIndex);

    TextStyle getTextStyle(int glyphIndex);

    float getKerning(int glyphIndex);

    float getDescent(TextStyle style);

    float getAscent(TextStyle style);

    float getWidth(int glyphIndex);

    boolean isWhitespace(int glyphIndex);

    boolean isLineBreak(int glyphIndex);
}
