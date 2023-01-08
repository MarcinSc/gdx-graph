package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;

public class WidthTagHandler extends PushPopStyleTagHandler {
    @Override
    protected void modifyTextStyle(TextStyle textStyle, String tagParameters) {
        Float width = Float.parseFloat(tagParameters.trim());
        textStyle.setAttribute(TextStyleConstants.FontWidth, width);
    }
}
