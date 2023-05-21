// Directions - left, right, up, down
vec4 borderDetection(sampler2D texture, vec4 uvTransform, vec2 uv, vec2 pixelSize, vec4 directions, float outlineWidth, float alphaEdge) {
    float alphaResult = 1.0 - step(alphaEdge, texture2D(texture, uvTransform.xy + uv * uvTransform.zw).a);

    float leftResult = directions.x * alphaResult * step(alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(pixelSize.x, 0.0)) * uvTransform.zw).a);
    float rightResult = directions.y * alphaResult * step(alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(-pixelSize.x, 0.0)) * uvTransform.zw).a);
    float upResult = directions.z * alphaResult * step(alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(0.0, pixelSize.y)) * uvTransform.zw).a);
    float downResult = directions.w * alphaResult * step(alphaEdge, texture2D(texture, uvTransform.xy + (uv + outlineWidth * vec2(0.0, -pixelSize.y)) * uvTransform.zw).a);

    return vec4(leftResult, rightResult, upResult, downResult);
}
