float isLighted_NODE_ID(vec3 position, int lightIndex) {
    mat4 shadowCameraMatrix = u_shadowCamera_NODE_ID[lightIndex];
    vec2 shadowCameraClipping = u_shadowCameraClipping_NODE_ID[lightIndex];
    vec2 positionInShadowMap = (shadowCameraMatrix * vec4(position, 1.0)).xy;

    vec4 shadowMapValue = probeShadowMap_NODE_ID(lightIndex, (((positionInShadowMap)+1.0) / 2.0));
    float shadowDistanceFromCamera = unpackVec3ToFloat(shadowMapValue.rgb, shadowCameraClipping.x, shadowCameraClipping.y);

    float result = 0.0;
    for (int x = -SHADOW_SOFTNESS; x <= SHADOW_SOFTNESS; x += 2) {
        for (int y = -SHADOW_SOFTNESS; y <= SHADOW_SOFTNESS; y += 2) {
            vec4 shadowMapValue = probeShadowMap_NODE_ID(lightIndex, (((positionInShadowMap) + 1.0) / 2.0) + vec2(x, y) * 0.5 / u_shadowCameraBufferSize_NODE_ID[lightIndex]);
            float shadowDistanceFromCamera = unpackVec3ToFloat(shadowMapValue.rgb, shadowCameraClipping.x, shadowCameraClipping.y);

            result += (distance(position, u_shadowCameraPosition_NODE_ID[lightIndex]) - SHADOW_ACNE_VALUE < shadowDistanceFromCamera) ? 1.0 : 0.0;
        }
    }
    return result / SHADOW_PROBE_COUNT;
}
