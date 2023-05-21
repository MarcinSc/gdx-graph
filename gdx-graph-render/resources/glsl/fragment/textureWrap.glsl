float clampToEdge(float value) {
    return clamp(value, 0.0, 1.0);
}

float repeat(float value) {
    return fract(value);
}

float mirroredRepeat(float value) {
    return 1.0 - abs(mod(value, 2.0) - 1.0);
}