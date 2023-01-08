package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.gempukku.libgdx.graph.artemis.text.TextHorizontalAlignment;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;

public class HorizontalAlignmentTagHandler extends PushPopStyleTagHandler {
    @Override
    protected void modifyTextStyle(TextStyle textStyle, String tagParameters) {
        TextHorizontalAlignment horizontalAlignment = TextHorizontalAlignment.valueOf(tagParameters.trim());
        textStyle.setAttribute(TextStyleConstants.AlignmentHorizontal, horizontalAlignment);
    }
}
