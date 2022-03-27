package com.gempukku.libgdx.graph.plugin.particles;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.plugin.particles.model.ParticleModel;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;

public class ParticlesGraphShader extends GraphShader {
    private int maxNumberOfParticles;

    public ParticlesGraphShader(String tag, Texture defaultTexture) {
        super(tag, defaultTexture);
    }

    public int getMaxNumberOfParticles() {
        return maxNumberOfParticles;
    }

    public void setMaxNumberOfParticles(int maxNumberOfParticles) {
        this.maxNumberOfParticles = maxNumberOfParticles;
    }

    public void renderParticles(ShaderContext shaderContext, ParticleModel particleModel, Mesh mesh) {
        for (Uniform uniform : localUniforms.values()) {
            uniform.getSetter().set(this, uniform.getLocation(), shaderContext);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            uniform.getSetter().set(this, uniform.getStartIndex(), uniform.getFieldOffsets(), uniform.getSize(), shaderContext);
        }
        int[] attributeLocations = getAttributeLocations();
        particleModel.renderMesh(program, mesh, attributeLocations);
    }
}
