float getStarValue(vec2 uv, float arms, float min, float max, float power) {
    vec2 vectorFromMiddle = normalize(uv - vec2(0.5));
    float topPartAngle = acos(vectorFromMiddle.x);
    float isBottomMultiplier = step(0.0, vectorFromMiddle.y);
    float bottomAngle = acos(-vectorFromMiddle.x);
    float angle = topPartAngle + isBottomMultiplier * bottomAngle * 2.0;
    float anglePerArm = 2.0 * 3.14159265358979323846 / arms;
    float inArm = mod(angle, anglePerArm) / anglePerArm;
    return min + (max - min) * pow(2.0 * abs(inArm - 0.5), power);
}
