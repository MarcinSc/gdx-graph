const int dither8Size = 8;
const float dither8Divisor = 65.0;

float getDither8(vec2 position, vec2 pixelSize, float value) {
    const float[64] dither8 = float[]
    (0.0, 32.0, 8.0, 40.0, 2.0, 34.0, 10.0, 42.0,
    48.0, 16.0, 56.0, 24.0, 50.0, 18.0, 58.0, 26.0,
    12.0, 44.0, 4.0, 36.0, 14.0, 46.0, 6.0, 38.0,
    60.0, 28.0, 52.0, 20.0, 62.0, 30.0, 54.0, 22.0,
    3.0, 35.0, 11.0, 43.0, 1.0, 33.0, 9.0, 41.0,
    51.0, 19.0, 59.0, 27.0, 49.0, 17.0, 57.0, 25.0,
    15.0, 47.0, 7.0, 39.0, 13.0, 45.0, 5.0, 37.0,
    63.0, 31.0, 55.0, 23.0, 61.0, 29.0, 53.0, 21.0);

    vec2 pixelCoordinate = position / pixelSize;
    int xInMatrix = int(mod(int(pixelCoordinate.x), dither8Size));
    int yInMatrix = int(mod(int(pixelCoordinate.y), dither8Size));
    return (value > (dither8[(yInMatrix * dither8Size + xInMatrix)] + 1.0) / dither8Divisor) ? 1.0 : 0.0;
}
