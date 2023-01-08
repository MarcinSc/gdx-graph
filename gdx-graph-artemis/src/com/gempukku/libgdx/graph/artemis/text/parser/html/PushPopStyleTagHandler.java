package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.configurable.TagHandler;
import com.gempukku.libgdx.graph.artemis.text.parser.configurable.TagParsedText;

public abstract class PushPopStyleTagHandler implements TagHandler {
    @Override
    public final void processStartTag(String tagParameters, Array<TextStyle> textStyleStack, TagParsedText tagParsedText, StringBuilder resultText) {
        TextStyle lastTextStyle = textStyleStack.peek();
        TextStyle duplicated = lastTextStyle.duplicate();
        modifyTextStyle(duplicated, tagParameters);

        textStyleStack.add(duplicated);
    }

    protected abstract void modifyTextStyle(TextStyle textStyle, String tagParameters);

    @Override
    public final void processEndTag(Array<TextStyle> textStyleStack, TagParsedText tagParsedText, StringBuilder resultText) {
        textStyleStack.pop();
    }
}
