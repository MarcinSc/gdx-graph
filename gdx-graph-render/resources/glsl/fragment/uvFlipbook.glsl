vec2 uvFlipbook(vec2 uv, int width, int height, int index, bool looping, bvec2 invert) {
    int spriteCount = width * height;
    int indexAdjusted = looping ? index : ((index > spriteCount - 1) ? spriteCount - 1 : index);

    int spriteIndex = indexAdjusted - (indexAdjusted / spriteCount) * spriteCount;
    int column = spriteIndex - (spriteIndex / width) * width;
    int row = spriteIndex / width;

    column = invert.x ? width - column - 1 : column;
    row = invert.y ? height - row - 1 : row;

    vec2 spriteSize = vec2(1.0) / vec2(float(width), float(height));
    float tileX = spriteSize.x * float(column);
    float tileY = spriteSize.y * float(row);

    return uv * spriteSize + vec2(tileX, tileY);
}
