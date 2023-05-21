vec3 screenSprite(vec3 position, vec2 uv, vec2 size, vec2 anchor, float rotation) {
    float result_xAdjust = size.x * (uv.x - anchor.x);
    float result_yAdjust = size.y * (uv.y - anchor.y);
    float result_rotatedX = result_xAdjust * cos(rotation) - result_yAdjust * sin(rotation);
    float result_rotatedY = result_xAdjust * sin(rotation) + result_yAdjust * cos(rotation);

    return position + vec3(result_rotatedX, -result_rotatedY, 0.0);
}
