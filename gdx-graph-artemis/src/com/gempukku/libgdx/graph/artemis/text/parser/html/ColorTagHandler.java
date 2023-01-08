package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;

public class ColorTagHandler extends PushPopStyleTagHandler {
    @Override
    protected void modifyTextStyle(TextStyle textStyle, String tagParameters) {
        Color color = Color.valueOf(tagParameters.trim());
        textStyle.setAttribute(TextStyleConstants.FontColor, color);
    }
}
