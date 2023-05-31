package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.IntMapping;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.particles.ParticleAttributeFunctions;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModel;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.particles.ParticleRenderableSprite;
import com.gempukku.libgdx.graph.util.particles.generator.DefaultParticleGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.ParticleGenerator;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMemoryMesh;
import com.gempukku.libgdx.graph.util.storage.GdxMeshRenderableModel;

import java.util.Iterator;

public class ParticlePreviewRenderableModel implements PreviewRenderableModel, Disposable {
    private SpriteModel spriteModel;
    private PreviewParticleGeneratorProducer particleGeneratorProducer;
    private DefaultParticleGenerator particleGenerator;

    private GdxMeshRenderableModel renderableModel;
    private ObjectMap<String, ShaderPropertySource> propertySourceMap;
    private VertexAttributes vertexAttributes;
    private ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;

    private final Array<ParticleRenderableSprite> allSprites = new Array<>();
    private final ObjectMap<ParticleRenderableSprite, SpriteReference> spriteIdentifiers = new ObjectMap<>();

    private final HierarchicalPropertyContainer hierarchicalPropertyContainer = new HierarchicalPropertyContainer();

    private ObjectSet<String> birthAttributeNames;
    private ObjectSet<String> deathAttributeNames;
    private SpriteSlotMemoryMesh<RenderableSprite> spriteMesh;

    private float lastTime;
    private float lifeLength;
    private int initialParticles;
    private float particlesPerSecond;

    public ParticlePreviewRenderableModel(
            PreviewParticleGeneratorProducer particleGeneratorProducer,
            float lifeLength, int initialParticles, float particlesPerSecond) {
        this.lifeLength = lifeLength;
        this.initialParticles = initialParticles;
        this.particlesPerSecond = particlesPerSecond;

        this.spriteModel = new QuadSpriteModel();
        this.particleGeneratorProducer = particleGeneratorProducer;
    }

    public void setSpriteModel(SpriteModel spriteModel) {
        this.spriteModel = spriteModel;
        recreateModel();
    }

    public void setParticleGeneratorProducer(PreviewParticleGeneratorProducer particleGeneratorProducer) {
        this.particleGeneratorProducer = particleGeneratorProducer;
        recreateModel();
    }

    public void setLifeLength(float lifeLength) {
        this.lifeLength = lifeLength;
        particleGenerator.setLifeLength(lifeLength);
    }

    public void setInitialParticles(int initialParticles) {
        this.initialParticles = initialParticles;
        particleGenerator.setInitialParticles(initialParticles);
    }

    public void setParticlesPerSecond(float particlesPerSecond) {
        this.particlesPerSecond = particlesPerSecond;
        particleGenerator.setParticlesPerSecond(particlesPerSecond);
    }

    public void initModel(GraphShader graphShader, PropertyContainer localPropertyContainer) {
        vertexAttributes = GraphModelUtil.getVertexAttributes(graphShader.getAttributes());
        propertySourceMap = graphShader.getProperties();

        vertexPropertySources = GraphModelUtil.getPropertySourceMap(vertexAttributes, propertySourceMap);

        hierarchicalPropertyContainer.setParent(localPropertyContainer);
        birthAttributeNames = getAttributesWithFunction(propertySourceMap, ParticleAttributeFunctions.ParticleBirth);
        deathAttributeNames = getAttributesWithFunction(propertySourceMap, ParticleAttributeFunctions.ParticleBirth);

        recreateModel();
    }

    private ObjectSet<String> getAttributesWithFunction(ObjectMap<String, ShaderPropertySource> propertySourceMap, String attributeFunction) {
        ObjectSet<String> result = new ObjectSet<>();
        for (ObjectMap.Entry<String, ShaderPropertySource> propertySourceEntry : propertySourceMap) {
            ShaderPropertySource propertySource = propertySourceEntry.value;
            if (propertySource.getAttributeFunction() != null && propertySource.getAttributeFunction().equals(attributeFunction))
                result.add(propertySourceEntry.key);
        }

        return result;
    }

    private void recreateModel() {
        recreateModelAtTime(0);
    }

    private void recreateModelAtTime(float currentTime) {
        if (vertexAttributes != null) {
            if (renderableModel != null) {
                renderableModel.dispose();
                spriteIdentifiers.clear();
                allSprites.clear();
                renderableModel = null;
            }

            spriteMesh = new SpriteSlotMemoryMesh<>((256 * 256 - 1) / 4,
                    spriteModel,
                    new SpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel));

            renderableModel = new GdxMeshRenderableModel(false, spriteMesh, vertexAttributes, hierarchicalPropertyContainer, "Test");

            createParticleGenerator(currentTime);
        }
    }

    private void createParticleGenerator(float currentTime) {
        particleGenerator = particleGeneratorProducer.createGenerator(lifeLength, initialParticles, particlesPerSecond, propertySourceMap);

        particleGenerator.initialCreateParticles(currentTime,
                new ParticleGenerator.ParticleCreateCallback() {
                    @Override
                    public void createParticle(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
                        createSprite(particleBirth, lifeLength, propertyContainer);
                    }
                });
    }

    public void resetParticles() {
        recreateModelAtTime(lastTime);
    }

    @Override
    public void update(float currentTime) {
        lastTime= currentTime;
        // Remove and create new particles, as needed
        Iterator<ParticleRenderableSprite> spriteIterator = allSprites.iterator();
        while (spriteIterator.hasNext()) {
            ParticleRenderableSprite sprite = spriteIterator.next();
            if (sprite.getParticleDeath() <= currentTime) {
                spriteMesh.removePart(spriteIdentifiers.remove(sprite));
                spriteIterator.remove();
            }
        }

        particleGenerator.createParticles(currentTime,
                new ParticleGenerator.ParticleCreateCallback() {
                    @Override
                    public void createParticle(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
                        createSprite(particleBirth, lifeLength, propertyContainer);
                    }
                });

    }

    private void createSprite(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
        ParticleRenderableSprite sprite = new ParticleRenderableSprite(particleBirth, lifeLength,
                birthAttributeNames, deathAttributeNames, propertyContainer);
        allSprites.add(sprite);
        spriteIdentifiers.put(sprite, spriteMesh.addPart(sprite));
    }

    @Override
    public Vector3 getPosition() {
        return renderableModel.getPosition();
    }

    @Override
    public boolean isRendered(GraphShader graphShader, Camera camera) {
        return renderableModel != null && renderableModel.isRendered(graphShader, camera);
    }

    @Override
    public Matrix4 getWorldTransform() {
        return renderableModel.getWorldTransform();
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return renderableModel.getPropertyContainer();
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        renderableModel.render(camera, shaderProgram, propertyToLocationMapping);
    }

    @Override
    public void dispose() {
        if (renderableModel != null) {
            renderableModel.dispose();
            renderableModel = null;
        }
    }
}
