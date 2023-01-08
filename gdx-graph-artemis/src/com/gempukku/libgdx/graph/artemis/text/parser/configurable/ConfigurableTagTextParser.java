package com.gempukku.libgdx.graph.artemis.text.parser.configurable;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.artemis.text.parser.CharacterParsedText;
import com.gempukku.libgdx.graph.artemis.text.parser.CharacterTextParser;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;

public class ConfigurableTagTextParser implements CharacterTextParser {
    private char endTagCharacter;
    private TagTextParser tagTextParser;

    private ObjectMap<String, TagHandler> tagHandlers = new ObjectMap<>();

    public ConfigurableTagTextParser(char openTagCharacter, char closeTagCharacter, char escapeCharacter, char endTagCharacter) {
        tagTextParser = new TagTextParser(openTagCharacter, closeTagCharacter, escapeCharacter);
        this.endTagCharacter = endTagCharacter;
    }

    public void addTagHandler(String tag, TagHandler tagHandler) {
        tagHandlers.put(tag, tagHandler);
    }

    @Override
    public CharacterParsedText parseText(TextStyle defaultTextStyle, String text) {
        final Array<TextStyle> textStyleStack = new Array<>();
        textStyleStack.add(defaultTextStyle);

        final TagParsedText result = Pools.obtain(TagParsedText.class);

        final StringBuilder resultText = new StringBuilder();
        TagTextHandler handler = new TagTextHandler() {
            @Override
            public void processTag(String tag) {
                int tagNameEnd = tag.indexOf(" ");
                if (tagNameEnd < 0)
                    tagNameEnd = tag.length();
                String tagName = tag.substring(0, tagNameEnd);
                boolean startTag = true;
                if (tagName.charAt(0) == endTagCharacter) {
                    startTag = false;
                    tagName = tagName.substring(1);
                }
                TagHandler tagHandler = tagHandlers.get(tagName);
                if (tagHandler == null)
                    throw new GdxRuntimeException("Unable to find tag handler for tag: " + tagName);
                if (startTag) {
                    tagHandler.processStartTag(tag.substring(tagNameEnd), textStyleStack, result, resultText);
                } else {
                    tagHandler.processEndTag(textStyleStack, result, resultText);
                }
            }

            @Override
            public void processText(String text) {
                result.addStyleIndex(resultText.length(), textStyleStack.peek());
                resultText.append(text);
            }
        };
        tagTextParser.parseTaggedText(text, handler);
        result.setText(resultText.toString());
        return result;
    }
}
