package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPrivateData;
import com.gempukku.libgdx.graph.plugin.lighting3d.Point3DLight;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.ui.AssetResolver;
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
import com.gempukku.libgdx.graph.ui.shader.GraphShaderRenderingWidget;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.particles.ParticleRenderableSprite;
import com.gempukku.libgdx.graph.util.particles.generator.*;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.SpriteUtil;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMemoryMesh;
import com.gempukku.libgdx.graph.util.storage.GdxMeshRenderableModel;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.Iterator;

public class ParticlesShaderPreview extends VisTable implements Disposable {
    public enum ShaderPreviewModel {
        Point("Point"), SphereSurface("Sphere Surface"), Sphere("Sphere"), Line("Line");

        private String text;

        ShaderPreviewModel(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private final GraphShaderRenderingWidget graphShaderRenderingWidget;

    private GraphWithProperties graph;

    private GraphShader graphShader;
    private GdxMeshRenderableModel particleModel;

    private final Array<ParticleRenderableSprite> sprites = new Array<>();
    private final ObjectMap<ParticleRenderableSprite, SpriteReference> spriteIdentifiers = new ObjectMap<>();
    private final DefaultParticleGenerator particleGenerator;
    private SpriteSlotMemoryMesh<RenderableSprite> spriteMesh;

    private final Camera camera;
    private final DefaultTimeKeeper timeKeeper;

    private final MapWritablePropertyContainer globalPropertyContainer;
    private final MapWritablePropertyContainer localPropertyContainer;

    public ParticlesShaderPreview() {
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.9f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();

        timeKeeper = new DefaultTimeKeeper();

        globalPropertyContainer = new MapWritablePropertyContainer();
        localPropertyContainer = new MapWritablePropertyContainer();

        particleGenerator = new DefaultParticleGenerator(3f, 1, 10f);
        setPositionPropertyGenerator(ShaderPreviewModel.Sphere);
        particleGenerator.setPropertyGenerator("UV",
                new PropertyGenerator() {
                    @Override
                    public Object generateProperty(float seed) {
                        return SpriteUtil.QUAD_UVS;
                    }
                });

        graphShaderRenderingWidget = new GraphShaderRenderingWidget();
        graphShaderRenderingWidget.addPrivatePluginData(Lighting3DPrivateData.class, createLightingPluginData());
        graphShaderRenderingWidget.setColorTexture(PatternTextures.sharedInstance.texture);
        graphShaderRenderingWidget.setDepthTexture(WhitePixel.sharedInstance.texture);
        graphShaderRenderingWidget.setCamera(camera);
        graphShaderRenderingWidget.setTimeProvider(timeKeeper);
        graphShaderRenderingWidget.setGlobalPropertyContainer(globalPropertyContainer);
        graphShaderRenderingWidget.setLocalPropertyContainer(localPropertyContainer);

        add(graphShaderRenderingWidget).grow();
    }

    private static Lighting3DPrivateData createLightingPluginData() {
        Lighting3DEnvironment graphShaderLightingEnvironment = new Lighting3DEnvironment();
        graphShaderLightingEnvironment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -4f, 1.8f, 1.8f, 8f);
        graphShaderLightingEnvironment.addPointLight(new Point3DLight(pointLight));

        final Lighting3DPrivateData data = new Lighting3DPrivateData();
        data.setEnvironment("", graphShaderLightingEnvironment);
        return data;
    }

    private void setPositionPropertyGenerator(ShaderPreviewModel model) {
        final PositionGenerator positionGenerator = getPositionGenerator(model);
        particleGenerator.setPropertyGenerator("Position", new PropertyGenerator() {
            @Override
            public Object generateProperty(float seed) {
                return positionGenerator.generateLocation(new Vector3());
            }
        });
    }

    private static PositionGenerator getPositionGenerator(ShaderPreviewModel model) {
        switch (model) {
            case Point:
                return new PointPositionGenerator();
            case Sphere:
                return new SpherePositionGenerator();
            case SphereSurface:
                return new SphereSurfacePositionGenerator();
            case Line:
                LinePositionGenerator lineGenerator = new LinePositionGenerator();
                lineGenerator.getPoint1().set(0, 0, -1f);
                lineGenerator.getPoint2().set(0, 0, 1f);
                return lineGenerator;
        }
        return null;
    }

    public void setLifetime(float lifetime) {
        particleGenerator.setLifeLength(lifetime);
    }

    public void setInitialCount(int initialCount) {
        particleGenerator.setInitialParticles(initialCount);
    }

    public void setParticlesPerSecond(float particlesPerSecond) {
        particleGenerator.setParticlesPerSecond(particlesPerSecond);
    }

    public void setModel(ShaderPreviewModel model) {
        setPositionPropertyGenerator(model);
    }

    public void setCameraDistance(float distance) {
        camera.position.set(-distance, distance, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
    }


    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);
        if (stage == null) {
            destroyShader();
        } else if (graphShader == null && graph != null) {
            createShader(graph);
        }
    }

    private void createShader(final GraphWithProperties graph) {
        try {
            graphShader = GraphShaderBuilder.buildParticlesShader("Test", WhitePixel.sharedInstance.texture, AssetResolver.instance, graph, true);

            globalPropertyContainer.clear();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() == PropertyLocation.Global_Uniform) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    globalPropertyContainer.setValue(property.getName(), propertyType.convertFromJson(property.getData()));
                }
            }

            localPropertyContainer.clear();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() == PropertyLocation.Uniform || property.getLocation() == PropertyLocation.Attribute) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    Object value = propertyType.convertFromJson(property.getData());
                    if (propertyType.isTexture()) {
                        if (value != null) {
                            try {
                                Texture texture = new Texture(Gdx.files.absolute((String) value));
                                graphShader.addManagedResource(texture);
                                localPropertyContainer.setValue(property.getName(), new TextureRegion(texture));
                            } catch (Exception exp) {
                                localPropertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                            }
                        } else {
                            localPropertyContainer.setValue(property.getName(), WhitePixel.sharedInstance.textureRegion);
                        }
                    }
                }
            }

            resetParticles();

            graphShaderRenderingWidget.setGraphShader(graphShader);
        } catch (Exception exp) {
            fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            destroyShader();
        }
    }

    public void resetParticles() {
        timeKeeper.setTime(0);
        if (graphShader != null) {
            if (particleModel != null) {
                particleModel.dispose();
                spriteIdentifiers.clear();
                sprites.clear();
            }
            VertexAttributes vertexAttributes = GraphModelUtil.getVertexAttributes(graphShader.getAttributes());
            ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(vertexAttributes, graphShader.getProperties());
            QuadSpriteModel spriteModel = new QuadSpriteModel();
            spriteMesh = new SpriteSlotMemoryMesh<>((256 * 256 - 1) / 4,
                    spriteModel,
                    new SpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel));
            particleModel = new GdxMeshRenderableModel(false, spriteMesh, vertexAttributes, localPropertyContainer);
            graphShaderRenderingWidget.setRenderableModel(particleModel);

            particleGenerator.initialCreateParticles(timeKeeper.getTime(),
                    new ParticleGenerator.ParticleCreateCallback() {
                        @Override
                        public void createParticle(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
                            ParticleRenderableSprite sprite = new ParticleRenderableSprite(particleBirth, lifeLength, propertyContainer);
                            sprites.add(sprite);
                            spriteIdentifiers.put(sprite, spriteMesh.addPart(sprite));
                        }
                    });
        }
    }

    private void destroyShader() {
        if (graphShader != null) {
            graphShader.dispose();
            graphShaderRenderingWidget.setGraphShader(null);
            graphShader = null;
        }
    }

    @Override
    public void dispose() {
        destroyShader();
        if (particleModel != null) {
            particleModel.dispose();
        }
    }

    @Override
    public void act(float delta) {
        // Update time keeper
        timeKeeper.updateTime(Gdx.graphics.getDeltaTime());

        if (graphShader != null) {
            // Remove and create new particles, as needed
            float currentTime = timeKeeper.getTime();
            Iterator<ParticleRenderableSprite> spriteIterator = sprites.iterator();
            while (spriteIterator.hasNext()) {
                ParticleRenderableSprite sprite = spriteIterator.next();
                if (sprite.getParticleDeath() < currentTime) {
                    spriteMesh.removePart(spriteIdentifiers.remove(sprite));
                    spriteIterator.remove();
                }
            }

            particleGenerator.createParticles(currentTime,
                    new ParticleGenerator.ParticleCreateCallback() {
                        @Override
                        public void createParticle(float particleBirth, float lifeLength, PropertyContainer propertyContainer) {
                            ParticleRenderableSprite sprite = new ParticleRenderableSprite(particleBirth, lifeLength, propertyContainer);
                            sprites.add(sprite);
                            spriteIdentifiers.put(sprite, spriteMesh.addPart(sprite));
                        }
                    });
        }

        super.act(delta);
    }

    public void graphChanged(boolean hasErrors, GraphWithProperties graph) {
        destroyShader();
        if (hasErrors) {
            this.graph = null;
        } else {
            this.graph = graph;
            createShader(graph);
        }
    }
}
