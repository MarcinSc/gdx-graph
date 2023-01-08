package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;
import com.gempukku.libgdx.graph.artemis.text.parser.configurable.TagHandler;
import com.gempukku.libgdx.graph.artemis.text.parser.configurable.TagParsedText;

import java.util.function.Function;

public class ImageTagHandler implements TagHandler {
    private Function<String, TextureRegion> textureRegionResolver;

    public ImageTagHandler(Function<String, TextureRegion> textureRegionResolver) {
        this.textureRegionResolver = textureRegionResolver;
    }

    @Override
    public void processStartTag(String tagParameters, Array<TextStyle> textStyleStack, TagParsedText tagParsedText, StringBuilder resultText) {
        tagParameters = tagParameters.trim();
        String[] params = tagParameters.split(" ", 2);
        String spriteSystemName = params[0];
        TextureRegion texture = textureRegionResolver.apply(params[1]);

        TextStyle lastStyle = textStyleStack.peek();
        TextStyle duplicate = lastStyle.duplicate();
        duplicate.setAttribute(TextStyleConstants.ImageSpriteSystemName, spriteSystemName);
        duplicate.setAttribute(TextStyleConstants.ImageTextureRegion, texture);

        textStyleStack.add(duplicate);

        tagParsedText.addStyleIndex(resultText.length(), textStyleStack.peek());
        resultText.append("_");

        textStyleStack.pop();
    }

    @Override
    public void processEndTag(Array<TextStyle> textStyleStack, TagParsedText tagParsedText, StringBuilder resultText) {

    }
}
