const int dither2Size = 2;
const float dither2Divisor = 5.0;

float getDither2(vec2 position, vec2 pixelSize, float value) {
    const float[4] dither2 = float[]
    (0.0, 2.0,
    3.0, 1.0);

    vec2 pixelCoordinate = position / pixelSize;
    int xInMatrix = int(mod(int(pixelCoordinate.x), dither2Size));
    int yInMatrix = int(mod(int(pixelCoordinate.y), dither2Size));
    return (value > (dither2[(yInMatrix * dither2Size + xInMatrix)] + 1.0) / dither2Divisor) ? 1.0 : 0.0;
}
