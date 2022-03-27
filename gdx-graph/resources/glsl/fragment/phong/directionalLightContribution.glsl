Lighting getDirectionalPhongLightContribution_NODE_ID(vec3 pos, vec3 N, float shininess, Lighting lighting) {
    vec3 V = normalize(u_cameraPosition.xyz - pos.xyz);
    for (int i = 0; i < NUM_DIRECTIONAL_LIGHTS; i++) {
        vec3 L = -u_dirLights_NODE_ID[i].direction;
        vec3 R = reflect(-L, N);

        float diffuse = clamp(dot(N, L), 0.0, 1.0);
        float specular = pow(clamp(dot(V, R), 0.0, 1.0), shininess);

        lighting.diffuse += u_dirLights_NODE_ID[i].color * diffuse;
        lighting.specular += (diffuse > 0.0 ? 1.0 : 0.0) * u_dirLights_NODE_ID[i].color * specular;
    }
    return lighting;
}
