package com.gempukku.libgdx.graph.artemis.text.parser;

/**
 * Entry class used to parse text (either plain, or in any rich text format)
 * into a set of unbreakable chunks, that can be later used to layout and render.
 */
public interface TextParser {
    /**
     * Parses the provided text with the provided TextStyle as the default.
     *
     * @param defaultTextStyle
     * @param text
     * @return
     */
    ParsedText parseText(TextStyle defaultTextStyle, String text);
}
