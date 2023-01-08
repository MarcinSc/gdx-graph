package com.gempukku.libgdx.graph.artemis.text.layout;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;

public interface GlyphOffsetText extends Disposable {
    float getTextHeight();

    float getTextWidth();

    TextStyle getTextStyle();

    int getLineCount();

    GlyphOffsetLine getLine(int index);

    TextStyle getLineStyle(int index);
}
