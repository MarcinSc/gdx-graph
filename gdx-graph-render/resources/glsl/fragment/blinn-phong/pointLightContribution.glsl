Lighting getPointBlinnPhongLightContribution_NODE_ID(vec3 pos, vec3 N, float shininess, Lighting lighting) {
    vec3 V = normalize(u_cameraPosition.xyz - pos.xyz);
    for (int i = 0; i < NUM_POINT_LIGHTS; i++) {
        vec3 L = u_pointLights_NODE_ID[i].position - pos.xyz;
        float distanceSquared = dot(L, L);
        float attenuation = 1.0 / (1.0 + distanceSquared);

        L = normalize(L);
        vec3 H = normalize(L + V);

        float diffuse = clamp(dot(N, L), 0.0, 1.0);
        float specular = pow(clamp(dot(H, N), 0.0, 1.0), shininess);

        lighting.diffuse += u_pointLights_NODE_ID[i].color * attenuation * diffuse;
        lighting.specular += (diffuse > 0.0 ? 1.0 : 0.0) * u_pointLights_NODE_ID[i].color * attenuation * specular;
    }
    return lighting;
}
