package com.gempukku.libgdx.graph.artemis.text.layout;

import com.gempukku.libgdx.graph.artemis.text.parser.ParsedText;

public interface GlyphOffseter {
    GlyphOffsetText offsetText(ParsedText parsedText, float availableWidth, boolean wrap);
}
