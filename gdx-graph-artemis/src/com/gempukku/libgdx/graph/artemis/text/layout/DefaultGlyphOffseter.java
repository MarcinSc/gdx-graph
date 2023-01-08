package com.gempukku.libgdx.graph.artemis.text.layout;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.artemis.text.TextHorizontalAlignment;
import com.gempukku.libgdx.graph.artemis.text.parser.ParsedText;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyle;
import com.gempukku.libgdx.graph.artemis.text.parser.TextStyleConstants;

public class DefaultGlyphOffseter implements GlyphOffseter {
    private float defaultLineSpacing = 0f;
    private float defaultParagraphSpacing = 0f;
    private float defaultGlyphSpacing = 0f;
    private float defaultGlyphScale = 1f;
    private TextHorizontalAlignment defaultHorizontalAlignment = TextHorizontalAlignment.left;

    public void setDefaultGlyphSpacing(float defaultGlyphSpacing) {
        this.defaultGlyphSpacing = defaultGlyphSpacing;
    }

    public void setDefaultLineSpacing(float defaultLineSpacing) {
        this.defaultLineSpacing = defaultLineSpacing;
    }

    public void setDefaultGlyphScale(float defaultGlyphScale) {
        this.defaultGlyphScale = defaultGlyphScale;
    }

    public void setDefaultHorizontalAlignment(TextHorizontalAlignment defaultHorizontalAlignment) {
        this.defaultHorizontalAlignment = defaultHorizontalAlignment;
    }

    @Override
    public GlyphOffsetText offsetText(ParsedText parsedText, float availableWidth, boolean wrap) {
        float width = 0;
        float height = 0;

        int nextCharacterIndex = 0;

        DefaultGlyphOffsetText text = Pools.obtain(DefaultGlyphOffsetText.class);

        DefaultGlyphOffsetLine lastLine = null;

        do {
            DefaultGlyphOffsetLine line = layoutLine(parsedText, availableWidth, nextCharacterIndex, wrap);
            if (line == null)
                break;

            if (lastLine != null) {
                if (lastLine.isEndingWithLineBreak()) {
                    float lastLineSpacing = getParagraphSpacing(lastLine.getGlyphStyle(0));
                    height += lastLineSpacing;
                    lastLine.lineHeight += lastLineSpacing;
                } else {
                    float lastLineSpacing = getLineSpacing(lastLine.getGlyphStyle(0));
                    height += lastLineSpacing;
                    lastLine.lineHeight += lastLineSpacing;
                }
            }

            text.lines.add(line);

            width = Math.max(width, line.getWidth());
            height += line.getHeight();
            nextCharacterIndex = nextCharacterIndex + line.getGlyphCount();
            lastLine = line;
        } while (wrap);

        text.textWidth = width;
        text.textHeight = height;

        return text;
    }

    private DefaultGlyphOffsetLine layoutLine(ParsedText parsedText, float availableWidth, int startIndex, boolean wrap) {
        int lineGlyphLength = determineLineGlyphLength(parsedText, availableWidth, startIndex, wrap);
        // Check if no more lines available
        if (lineGlyphLength == 0)
            return null;

        // Calculate max ascent and descent of the line
        float maxAscent = 0f;
        float maxDescent = 0f;
        for (int i = startIndex; i < startIndex + lineGlyphLength; i++) {
            TextStyle textStyle = parsedText.getTextStyle(i);

            // If it's not the last character, or not whitespace - use to calculate ascent/descent
            if (i != startIndex + lineGlyphLength - 1 || !parsedText.isWhitespace(i)) {
                float fontScale = getGlyphScale(textStyle);
                maxDescent = Math.max(maxDescent, parsedText.getDescent(textStyle) * fontScale);
                maxAscent = Math.max(maxAscent, parsedText.getAscent(textStyle) * fontScale);
            }
        }

        DefaultGlyphOffsetLine line = Pools.obtain(DefaultGlyphOffsetLine.class);

        TextStyle lineStyle = parsedText.getTextStyle(startIndex);

        float linePaddingLeft = getLinePaddingLeft(lineStyle);

        float usedWidth = linePaddingLeft;
        float justifiedSpace = 0;
        TextHorizontalAlignment horizontalAlignment = getHorizontalAlignment(lineStyle);
        if (horizontalAlignment == TextHorizontalAlignment.justifiedFragment
                || (horizontalAlignment == TextHorizontalAlignment.justified
                && !isEndOfLine(parsedText, startIndex, lineGlyphLength))) {
            int spaceCount = countJustifiableSpaces(parsedText, startIndex, lineGlyphLength);
            if (spaceCount > 0) {
                float linePaddingRight = getLinePaddingRight(lineStyle);
                justifiedSpace = availableWidth - linePaddingLeft - linePaddingRight;
                justifiedSpace -= getTextWidthExcludingLastSkippable(parsedText, startIndex, lineGlyphLength);
                justifiedSpace /= spaceCount;
            }
        }

        for (int i = startIndex; i < startIndex + lineGlyphLength; i++) {
            TextStyle textStyle = parsedText.getTextStyle(i);

            // If it's not the last character, or not whitespace - layout the char
            if (i != startIndex + lineGlyphLength - 1 || !parsedText.isWhitespace(i)) {
                float fontScale = getGlyphScale(textStyle);
                float ascent = parsedText.getAscent(textStyle) * fontScale;
                float descent = parsedText.getDescent(textStyle) * fontScale;
                float kerning = 0f;
                if (i > startIndex) {
                    kerning = parsedText.getKerning(i);
                }
                line.xAdvances.add(usedWidth + kerning * fontScale);
                line.yAdvances.add(maxAscent - ascent);
                float glyphWidth = parsedText.getWidth(i);
                line.widths.add(glyphWidth);
                line.heights.add(ascent + descent);

                float glyphAdvance = glyphWidth + kerning + getLetterSpacing(textStyle);
                boolean addJustified = parsedText.isWhitespace(i);

                usedWidth += glyphAdvance * fontScale + (addJustified ? justifiedSpace : 0);
            }
        }
        line.xAdvances.add(usedWidth);
        line.yAdvances.add(0f);
        line.glyphStartIndex = startIndex;
        line.glyphCount = lineGlyphLength;
        line.parsedText = parsedText;
        line.endingWithLineBreak = parsedText.isLineBreak(startIndex + lineGlyphLength - 1);
        line.lineWidth = usedWidth;
        line.lineHeight = maxAscent + maxDescent;

        return line;
    }

    private boolean isEndOfLine(ParsedText parsedText, int startIndex, int lineGlyphLength) {
        // Ends with line break, or no further text after
        if (parsedText.isLineBreak(startIndex + lineGlyphLength - 1)
                || parsedText.getNextUnbreakableChunkLength(startIndex + lineGlyphLength) == -1)
            return true;
        return false;
    }

    private int countJustifiableSpaces(ParsedText parsedText, int startIndex, int glyphCount) {
        int result = 0;
        for (int i = startIndex; i < startIndex + glyphCount; i++) {
            if (i != startIndex + glyphCount - 1 && parsedText.isWhitespace(i))
                result++;
        }
        return result;
    }

    private int determineLineGlyphLength(ParsedText parsedText, float availableWidth, int startIndex, boolean wrap) {
        float usedWidth = 0f;
        int consumedGlyphIndex = startIndex;

        boolean firstChunk = true;
        while (true) {
            int chunkLength = parsedText.getNextUnbreakableChunkLength(consumedGlyphIndex);
            // Check if there is no more chunks left
            if (chunkLength < 0)
                return consumedGlyphIndex - startIndex;
            // Add the trailing whitespace width (if any) of a previous chunk (if not first chunk)
            if (!firstChunk) {
                usedWidth += getCharacterWidthIfSkippable(parsedText, consumedGlyphIndex - 1);
            } else {
                TextStyle lineStyle = parsedText.getTextStyle(startIndex);
                usedWidth += getLinePaddingLeft(lineStyle);
                availableWidth -= getLinePaddingRight(lineStyle);
            }

            float chunkWidth = getTextWidthExcludingLastSkippable(parsedText, consumedGlyphIndex, chunkLength);
            if (wrap && !firstChunk && usedWidth + chunkWidth > availableWidth)
                return consumedGlyphIndex - startIndex;
            usedWidth += chunkWidth;

            consumedGlyphIndex += chunkLength;

            if (parsedText.isLineBreak(consumedGlyphIndex - 1))
                return consumedGlyphIndex - startIndex;

            firstChunk = false;
        }
    }

    private float getCharacterWidthIfSkippable(ParsedText parsedText, int glyphIndex) {
        if (parsedText.isWhitespace(glyphIndex)) {
            TextStyle textStyle = parsedText.getTextStyle(glyphIndex);
            float fontScale = getGlyphScale(textStyle);
            return (parsedText.getWidth(glyphIndex) + getLetterSpacing(textStyle)) * fontScale;
        } else {
            return 0f;
        }
    }

    private float getTextWidthExcludingLastSkippable(ParsedText parsedText, int startIndex, int length) {
        float width = 0;

        for (int i = startIndex; i < startIndex + length; i++) {
            TextStyle textStyle = parsedText.getTextStyle(i);

            // If it's not the last character, or not whitespace - add the width
            if (i != startIndex + length - 1 || !parsedText.isWhitespace(i)) {

                float fontScale = getGlyphScale(textStyle);

                float glyphAdvance = parsedText.getWidth(i);
                if (i > startIndex) {
                    glyphAdvance += parsedText.getKerning(i);
                }

                width += (glyphAdvance + getLetterSpacing(textStyle)) * fontScale;
            }
        }
        return width;
    }

    private float getLetterSpacing(TextStyle textStyle) {
        Float letterSpacing = (Float) textStyle.getAttribute(TextStyleConstants.GlyphSpacing);
        return letterSpacing != null ? letterSpacing : defaultGlyphSpacing;
    }

    private float getLineSpacing(TextStyle textStyle) {
        Float lineSpacing = (Float) textStyle.getAttribute(TextStyleConstants.LineSpacing);
        return lineSpacing != null ? lineSpacing : defaultLineSpacing;
    }

    private float getParagraphSpacing(TextStyle textStyle) {
        Float paragraphSpacing = (Float) textStyle.getAttribute(TextStyleConstants.ParagraphSpacing);
        return paragraphSpacing != null ? paragraphSpacing : defaultParagraphSpacing;
    }

    private float getGlyphScale(TextStyle textStyle) {
        Float fontScale = (Float) textStyle.getAttribute(TextStyleConstants.GlyphScale);
        return fontScale != null ? fontScale : defaultGlyphScale;
    }

    private TextHorizontalAlignment getHorizontalAlignment(TextStyle lineStyle) {
        TextHorizontalAlignment alignment = (TextHorizontalAlignment) lineStyle.getAttribute(TextStyleConstants.AlignmentHorizontal);
        return alignment != null ? alignment : defaultHorizontalAlignment;
    }

    private float getLinePaddingLeft(TextStyle lineStyle) {
        Float linePaddingLeft = (Float) lineStyle.getAttribute(TextStyleConstants.PaddingLeft);
        return linePaddingLeft != null ? linePaddingLeft : 0f;
    }

    private float getLinePaddingRight(TextStyle lineStyle) {
        Float linePaddingRight = (Float) lineStyle.getAttribute(TextStyleConstants.PaddingRight);
        return linePaddingRight != null ? linePaddingRight : 0f;
    }

    public static class DefaultGlyphOffsetText implements GlyphOffsetText, Pool.Poolable {
        private float textWidth;
        private float textHeight;
        private Array<DefaultGlyphOffsetLine> lines = new Array<>();

        @Override
        public float getTextWidth() {
            return textWidth;
        }

        @Override
        public float getTextHeight() {
            return textHeight;
        }

        @Override
        public TextStyle getTextStyle() {
            return getLineStyle(0);
        }

        @Override
        public int getLineCount() {
            return lines.size;
        }

        @Override
        public GlyphOffsetLine getLine(int index) {
            return lines.get(index);
        }

        @Override
        public TextStyle getLineStyle(int index) {
            return getLine(index).getGlyphStyle(0);
        }

        @Override
        public void dispose() {
            Pools.free(this);
        }

        @Override
        public void reset() {
            textWidth = 0f;
            textHeight = 0;
            Pools.freeAll(lines);
            lines.clear();
        }
    }

    private static class DefaultGlyphOffsetLine implements GlyphOffsetLine, Pool.Poolable {
        private ParsedText parsedText;
        private float lineWidth;
        private float lineHeight;
        private int glyphStartIndex;
        private int glyphCount;
        private boolean endingWithLineBreak;
        private FloatArray xAdvances = new FloatArray();
        private FloatArray yAdvances = new FloatArray();
        private FloatArray widths = new FloatArray();
        private FloatArray heights = new FloatArray();

        @Override
        public float getWidth() {
            return lineWidth;
        }

        @Override
        public float getHeight() {
            return lineHeight;
        }

        @Override
        public int getGlyphCount() {
            return glyphCount;
        }

        @Override
        public int getStartIndex() {
            return glyphStartIndex;
        }

        @Override
        public boolean isEndingWithLineBreak() {
            return endingWithLineBreak;
        }

        @Override
        public float getGlyphXAdvance(int glyphIndex) {
            return xAdvances.get(glyphIndex);
        }

        @Override
        public float getGlyphYAdvance(int glyphIndex) {
            return yAdvances.get(glyphIndex);
        }

        @Override
        public TextStyle getGlyphStyle(int glyphIndex) {
            return parsedText.getTextStyle(glyphStartIndex + glyphIndex);
        }

        @Override
        public void reset() {
            parsedText = null;
            lineWidth = 0f;
            lineHeight = 0f;
            glyphStartIndex = 0;
            glyphCount = 0;
            endingWithLineBreak = false;
            xAdvances.clear();
            yAdvances.clear();
            widths.clear();
            heights.clear();
        }
    }
}
