vec3 billboardSprite(vec3 position, vec2 uv, vec2 size, vec2 anchor, float rotation) {
    vec3 result_cameraRight = normalize(cross(u_cameraDirection, u_cameraUp));
    vec3 result_cameraUp = normalize(u_cameraUp);

    float result_xAdjust = size.x * (uv.x - anchor.x);
    float result_yAdjust = size.y * (uv.y - anchor.y);
    float result_rotatedX = result_xAdjust * cos(rotation) - result_yAdjust * sin(rotation);
    float result_rotatedY = result_xAdjust * sin(rotation) + result_yAdjust * cos(rotation);

    return position + (result_rotatedX * result_cameraRight) - (result_rotatedY * result_cameraUp);
}
