package com.gempukku.libgdx.graph.artemis.text.parser.html;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.artemis.text.parser.CharacterParsedText;
import com.gempukku.libgdx.graph.artemis.text.parser.CharacterTextParser;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.configurable.ConfigurableTagTextParser;
import com.gempukku.libgdx.graph.artemis.text.parser.configurable.TagHandler;

import java.util.function.Function;

public class HtmlTextParser implements CharacterTextParser {
    private ConfigurableTagTextParser tagTextParser;

    public HtmlTextParser(Function<String, BitmapFont> fontResolver, Function<String, TextureRegion> textureRegionResolver) {
        this(fontResolver, textureRegionResolver, '<', '>', '\\', '/');
    }

    public HtmlTextParser(Function<String, BitmapFont> fontResolver, Function<String, TextureRegion> textureRegionResolver,
                          char openTagCharacter, char closeTagCharacter, char escapeCharacter, char endTagCharacter) {
        tagTextParser = new ConfigurableTagTextParser(openTagCharacter, closeTagCharacter, escapeCharacter, endTagCharacter);
        // Font
        tagTextParser.addTagHandler("font", new FontTagHandler(fontResolver));
        // Color
        tagTextParser.addTagHandler("color", new ColorTagHandler());
        // Width
        tagTextParser.addTagHandler("width", new WidthTagHandler());
        // Scale
        tagTextParser.addTagHandler("scale", new ScaleTagHandler());
        // Letter spacing
        tagTextParser.addTagHandler("letterSpacing", new LetterSpacingTagHandler());
        // Padding
        tagTextParser.addTagHandler("paddingLeft", new PaddingLeftTagHandler());
        tagTextParser.addTagHandler("paddingRight", new PaddingRightTagHandler());
        // Alignment
        tagTextParser.addTagHandler("horAlign", new HorizontalAlignmentTagHandler());
        // Image
        tagTextParser.addTagHandler("img", new ImageTagHandler(textureRegionResolver));
    }

    public void addTagHandler(String tag, TagHandler tagHandler) {
        tagTextParser.addTagHandler(tag, tagHandler);
    }

    @Override
    public CharacterParsedText parseText(TextStyle defaultTextStyle, String text) {
        return tagTextParser.parseText(defaultTextStyle, text);
    }
}
