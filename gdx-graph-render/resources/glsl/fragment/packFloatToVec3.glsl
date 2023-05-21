vec3 packFloatToVec3(float value, float minValue, float maxValue) {
    float zeroToOne = (value - minValue) / (maxValue - minValue);
    float zeroTo24Bit = zeroToOne * (256.0 * 256.0 * 256.0 - 1.0);
    return floor(vec3(mod(zeroTo24Bit, 256.0), mod(zeroTo24Bit / 256.0, 256.0), zeroTo24Bit / 256.0 / 256.0)) / 255.0;
}
