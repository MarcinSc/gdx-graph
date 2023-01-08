package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;

public class PaddingLeftTagHandler extends PushPopStyleTagHandler {
    @Override
    protected void modifyTextStyle(TextStyle textStyle, String tagParameters) {
        float paddingLeft = Float.parseFloat(tagParameters.trim());
        textStyle.setAttribute(TextStyleConstants.PaddingLeft, paddingLeft);
    }
}
