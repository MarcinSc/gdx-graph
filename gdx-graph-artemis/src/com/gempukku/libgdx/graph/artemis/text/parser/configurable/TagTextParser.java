package com.gempukku.libgdx.graph.artemis.text.parser.configurable;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class TagTextParser {
    private char openTagCharacter;
    private char closeTagCharacter;
    private char escapeCharacter;

    public TagTextParser(char openTagCharacter, char closeTagCharacter, char escapeCharacter) {
        this.openTagCharacter = openTagCharacter;
        this.closeTagCharacter = closeTagCharacter;
        this.escapeCharacter = escapeCharacter;
    }

    public void parseTaggedText(String text, TagTextHandler handler) {
        StringBuilder resultBuilding = new StringBuilder();

        boolean insideTag = false;
        boolean escaping = false;
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (escaping) {
                resultBuilding.append(c);
                escaping = false;
            } else {
                if (c == openTagCharacter) {
                    if (insideTag) {
                        throw new GdxRuntimeException("Start of new tag inside a tag");
                    } else {
                        if (resultBuilding.length() > 0) {
                            handler.processText(resultBuilding.toString());
                            resultBuilding.setLength(0);
                        }
                        insideTag = true;
                    }
                } else if (c == closeTagCharacter) {
                    if (!insideTag) {
                        throw new GdxRuntimeException("End of tag outside of a tag");
                    } else {
                        handler.processTag(resultBuilding.toString());
                        resultBuilding.setLength(0);
                        insideTag = false;
                    }
                } else if (c == escapeCharacter) {
                    escaping = true;
                } else {
                    resultBuilding.append(c);
                }
            }
        }
        if (insideTag)
            throw new GdxRuntimeException("Tag not closed");
        if (escaping)
            throw new GdxRuntimeException("Escape sequence not completed");

        if (resultBuilding.length() > 0)
            handler.processText(resultBuilding.toString());
    }
}
