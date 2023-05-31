package com.gempukku.libgdx.graph.artemis.particle;

import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.util.ShaderInformation;
import com.gempukku.libgdx.graph.util.particles.ParticleModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;

public class PipelineParticleModel extends ParticleModel {
    private final String pipelineName;

    public PipelineParticleModel(String pipelineName, int particlesPerPage, SpriteModel spriteModel, ShaderInformation shaderInformation, ModelContainer<RenderableModel> modelContainer, String tag) {
        super(particlesPerPage, spriteModel, shaderInformation, modelContainer, tag);
        this.pipelineName = pipelineName;
    }

    public String getPipelineName() {
        return pipelineName;
    }
}
