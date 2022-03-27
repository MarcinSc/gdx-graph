vec3 applyNormalMap(vec3 tangent, vec3 normal, vec3 bump, float strength) {
    tangent = normalize(tangent);
    normal = normalize(normal);
    bump = bump * 2.0 - 1.0;
    bump.rg *= strength;
    vec3 bitangent = normalize(cross(normal, tangent));
    return normalize(mat3(tangent, bitangent, normal) * normalize(bump));
}
