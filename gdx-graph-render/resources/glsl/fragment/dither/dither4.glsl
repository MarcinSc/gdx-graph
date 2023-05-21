const int dither4Size = 4;
const float dither4Divisor = 17.0;

float getDither4(vec2 position, vec2 pixelSize, float value) {
    const float[16] dither4 = float[]
    (0.0, 8.0, 2.0, 10.0,
    12.0, 4.0, 14.0, 6.0,
    3.0, 11.0, 1.0, 9.0,
    15.0, 7.0, 13.0, 5.0);

    vec2 pixelCoordinate = position / pixelSize;
    int xInMatrix = int(mod(int(pixelCoordinate.x), dither4Size));
    int yInMatrix = int(mod(int(pixelCoordinate.y), dither4Size));
    return (value > (dither4[(yInMatrix * dither4Size + xInMatrix)] + 1.0) / dither4Divisor) ? 1.0 : 0.0;
}
