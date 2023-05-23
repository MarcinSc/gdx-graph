package com.gempukku.libgdx.graph.plugin.particles.design.generator;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.particles.design.PreviewParticleGeneratorProducer;
import com.gempukku.libgdx.graph.shader.AttributeFunctions;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PropertyGenerator;
import com.gempukku.libgdx.graph.util.sprite.SpriteUtil;

public class PositionParticleGeneratorProducer implements PreviewParticleGeneratorProducer {
    private Vector3 tmpVector = new Vector3();

    private PositionGenerator positionGenerator;

    public PositionParticleGeneratorProducer(PositionGenerator positionGenerator) {
        this.positionGenerator = positionGenerator;
    }

    @Override
    public DefaultParticleGenerator createGenerator(
            float lifeLength, int initialParticles, float particlesPerSecond,
            ObjectMap<String, ShaderPropertySource> shaderPropertySources) {
        DefaultParticleGenerator generator = new DefaultParticleGenerator(lifeLength, initialParticles, particlesPerSecond);
        for (ObjectMap.Entry<String, ShaderPropertySource> shaderPropertySource : shaderPropertySources) {
            String attributeFunction = shaderPropertySource.value.getAttributeFunction();
            if (attributeFunction != null && attributeFunction.equals(AttributeFunctions.Position)) {
                generator.setPropertyGenerator(shaderPropertySource.key,
                        new PropertyGenerator() {
                            @Override
                            public Object generateProperty(float seed) {
                                return positionGenerator.generateLocation(tmpVector);
                            }
                        });
            }
            if (attributeFunction != null && attributeFunction.equals(AttributeFunctions.TexCoord0)) {
                generator.setPropertyGenerator(shaderPropertySource.key,
                        new PropertyGenerator() {
                            @Override
                            public Object generateProperty(float seed) {
                                return SpriteUtil.QUAD_UVS;
                            }
                        });
            }
        }

        return generator;
    }
}
