float unpackVec3ToFloat(vec3 packedValue, float minValue, float maxValue) {
    vec3 scaleVector = vec3(1.0, 256.0, 256.0 * 256.0) / (256.0 * 256.0 * 256.0 - 1.0) * 255.0;
    float zeroToOne = dot(packedValue, scaleVector);
    return zeroToOne * (maxValue - minValue) + minValue;
}
