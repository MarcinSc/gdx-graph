package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;

import java.util.function.Function;

public class FontTagHandler extends PushPopStyleTagHandler {
    private Function<String, BitmapFont> fontResolver;

    public FontTagHandler(Function<String, BitmapFont> fontResolver) {
        this.fontResolver = fontResolver;
    }

    @Override
    protected void modifyTextStyle(TextStyle textStyle, String tagParameters) {
        String fontName = tagParameters.trim();
        textStyle.setAttribute(TextStyleConstants.Font, fontResolver.apply(fontName));
    }
}
