package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.libgdx.context.StateOpenGLContext;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
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
import com.gempukku.libgdx.graph.ui.PatternTextures;
import com.gempukku.libgdx.graph.ui.graph.GraphStatusChangeEvent;
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

import java.util.Iterator;

public class ParticlesShaderPreviewWidget extends Widget implements Disposable {

    private SpriteSlotMemoryMesh<RenderableSprite> spriteMesh;

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

    private GraphWithProperties graph;
    private boolean shaderInitialized;
    private int width;
    private int height;

    private FrameBuffer frameBuffer;
    private GraphShader graphShader;
    private OpenGLContext renderContext;

    private GdxMeshRenderableModel particleModel;
    private Array<ParticleRenderableSprite> sprites = new Array<>();
    private ObjectMap<ParticleRenderableSprite, SpriteReference> spriteIdentifiers = new ObjectMap<>();
    private DefaultParticleGenerator particleGenerator;

    private Camera camera;
    private final DefaultTimeKeeper timeKeeper;
    private Lighting3DEnvironment graphShaderEnvironment;
    private ShaderContextImpl shaderContext;

    private final MapWritablePropertyContainer localPropertyContainer;

    public ParticlesShaderPreviewWidget(int width, int height) {
        this.width = width;
        this.height = height;
        renderContext = new StateOpenGLContext();
        camera = new PerspectiveCamera();
        camera.near = 0.1f;
        camera.far = 100f;
        camera.position.set(-0.9f, 0f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();

        timeKeeper = new DefaultTimeKeeper();

        graphShaderEnvironment = new Lighting3DEnvironment();
        graphShaderEnvironment.setAmbientColor(new Color(0.1f, 0.1f, 0.1f, 1f));
        PointLight pointLight = new PointLight();
        pointLight.set(Color.WHITE, -4f, 1.8f, 1.8f, 8f);
        graphShaderEnvironment.addPointLight(new Point3DLight(pointLight));

        final Lighting3DPrivateData data = new Lighting3DPrivateData();
        data.setEnvironment("", graphShaderEnvironment);

        PluginPrivateDataSource dataSource = new PluginPrivateDataSource() {
            @Override
            public <T> T getPrivatePluginData(Class<T> clazz) {
                if (clazz == Lighting3DPrivateData.class)
                    return (T) data;
                return null;
            }
        };

        shaderContext = new ShaderContextImpl(dataSource);
        shaderContext.setCamera(camera);
        shaderContext.setRenderWidth(width);
        shaderContext.setRenderHeight(height);
        shaderContext.setColorTexture(PatternTextures.sharedInstance.texture);

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
        if (stage == null && shaderInitialized) {
            destroyShader();
        } else if (stage != null && !shaderInitialized && graph != null) {
            createShader(graph);
        }
    }

    @Override
    public float getPrefWidth() {
        return width;
    }

    @Override
    public float getPrefHeight() {
        return height;
    }

    private void createShader(final GraphWithProperties graph) {
        try {
            graphShader = GraphShaderBuilder.buildParticlesShader("Test", WhitePixel.sharedInstance.texture, graph, true);
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

            MapWritablePropertyContainer globalPropertyContainer = new MapWritablePropertyContainer();
            for (GraphProperty property : graph.getProperties()) {
                if (property.getLocation() == PropertyLocation.Global_Uniform) {
                    ShaderFieldType propertyType = ShaderFieldTypeRegistry.findShaderFieldType(property.getType());
                    globalPropertyContainer.setValue(property.getName(), propertyType.convertFromJson(property.getData()));
                }
            }
            shaderContext.setGlobalPropertyContainer(globalPropertyContainer);

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

            shaderContext.setTimeProvider(timeKeeper);

            resetParticles();

            shaderInitialized = true;
        } catch (Exception exp) {
            fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            if (graphShader != null)
                graphShader.dispose();
        }
    }

    public void resetParticles() {
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
        frameBuffer.dispose();
        frameBuffer = null;
        graphShader.dispose();
        graphShader = null;
        shaderInitialized = false;
    }

    @Override
    public void dispose() {
        if (shaderInitialized) {
            destroyShader();
            particleModel.dispose();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (frameBuffer != null) {
            batch.end();

            timeKeeper.updateTime(Gdx.graphics.getDeltaTime());
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
            try {
                frameBuffer.begin();
                camera.viewportWidth = width;
                camera.viewportHeight = height;
                camera.update();

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

                renderContext.begin();
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                graphShader.begin(shaderContext, renderContext);
                graphShader.render(shaderContext, particleModel);
                graphShader.end();
                frameBuffer.end();
                renderContext.end();
            } catch (Exception exp) {
                // Ignore
                fire(new GraphStatusChangeEvent(GraphStatusChangeEvent.Type.ERROR, exp.getMessage()));
            } finally {
                if (ScissorStack.peekScissors() != null)
                    Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            }

            batch.begin();
            batch.draw(frameBuffer.getColorBufferTexture(), getX(), getY() + height, width, -height);
        }
    }

    public void graphChanged(boolean hasErrors, GraphWithProperties graph) {
        if (hasErrors) {
            this.graph = null;
        } else {
            this.graph = graph;
        }

        if (hasErrors && shaderInitialized) {
            destroyShader();
        } else if (!hasErrors && !shaderInitialized) {
            createShader(graph);
        } else if (!hasErrors && shaderInitialized) {
            destroyShader();
            createShader(graph);
        }
    }
}
