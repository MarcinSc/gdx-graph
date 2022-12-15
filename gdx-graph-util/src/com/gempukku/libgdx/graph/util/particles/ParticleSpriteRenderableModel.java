package com.gempukku.libgdx.graph.util.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.FloatArrayObjectStorage;
import com.gempukku.libgdx.graph.util.sprite.storage.ToFloatArraySerializer;

public class ParticleSpriteRenderableModel implements SpriteRenderableModel {
    private float maxDeathTime = Float.MIN_VALUE;

    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final ObjectMap<VertexAttribute, PropertySource> vertexPropertySources;
    private final SpriteModel spriteModel;
    private final int floatCountPerVertex;
    private final VertexAttributes vertexAttributes;
    private int[] attributeLocations;

    private FloatArrayObjectStorage<RenderableSprite> spriteStorage;

    public ParticleSpriteRenderableModel(
            int spriteCapacity, int identifierCount,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer) {
        this(spriteCapacity, identifierCount, vertexAttributes, vertexPropertySources, propertyContainer,
                new QuadSpriteModel());
    }

    public ParticleSpriteRenderableModel(
            int spriteCapacity, int identifierCount,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.propertyContainer = propertyContainer;
        this.spriteModel = spriteModel;

        this.vertexAttributes = vertexAttributes;

        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;

        final int floatCountPerSprite = floatCountPerVertex * spriteModel.getVertexCount();

        spriteStorage = new FloatArrayObjectStorage<>(spriteCapacity, identifierCount,
                new ToFloatArraySerializer<RenderableSprite>() {
                    @Override
                    public int getFloatCount() {
                        return floatCountPerSprite;
                    }

                    @Override
                    public void serializeToFloatArray(RenderableSprite value, float[] floatArray, int startIndex) {
                        updateSpriteData(value, floatArray, startIndex);
                    }
                });

        mesh = new Mesh(false, true,
                spriteModel.getVertexCount() * spriteCapacity,
                spriteModel.getIndexCount() * spriteCapacity, vertexAttributes);
        mesh.setVertices(spriteStorage.getFloatArray());

        short[] indices = new short[spriteModel.getIndexCount() * spriteCapacity];
        spriteModel.initializeIndexBuffer(indices, spriteCapacity);

        mesh.setIndices(indices, 0, indices.length);
    }

    @Override
    public int getSpriteCount() {
        return spriteStorage.getObjectCount();
    }

    @Override
    public boolean isAtCapacity() {
        return spriteStorage.isAtCapacity();
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
    }

    @Override
    public int addSprite(RenderableSprite sprite) {
        return spriteStorage.addObject(sprite);
    }

    @Override
    public int updateSprite(RenderableSprite sprite, int spriteIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSprite(int spriteIndex) {
        throw new UnsupportedOperationException();
    }

    public void removeAllSprites() {
        spriteStorage.clear();
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    private void updateSpriteData(RenderableSprite sprite, float[] vertexData, int spriteDataStart) {
        int vertexCount = spriteModel.getVertexCount();

        for (VertexAttribute vertexAttribute : vertexAttributes) {
            int attributeOffset = vertexAttribute.offset / 4;

            PropertySource propertySource = vertexPropertySources.get(vertexAttribute);
            if (propertySource == null) {
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;
                    sprite.setUnknownPropertyInAttribute(vertexAttribute, vertexData, vertexOffset + attributeOffset);
                }
            } else {
                ShaderFieldType shaderFieldType = propertySource.getShaderFieldType();
                Object attributeValue = sprite.getPropertyContainer().getValue(propertySource.getPropertyName());
                if (attributeValue instanceof ValuePerVertex) {
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                        Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, propertySource.getValueToUse(vertexValue));
                    }
                } else {
                    attributeValue = propertySource.getValueToUse(attributeValue);
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, attributeValue);
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        mesh.dispose();
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public boolean isRendered(Camera camera) {
        return spriteStorage.getObjectCount() > 0 && !isCulled(camera);
    }

    private boolean isCulled(Camera camera) {
        return cullingTest != null && cullingTest.isCulled(camera, getPosition());
    }

    @Override
    public Matrix4 getWorldTransform() {
        return worldTransform;
    }

    @Override
    public void prepareToRender(ShaderContext shaderContext) {
        int minUpdatedIndex = spriteStorage.getMinUpdatedIndex();
        int maxUpdatedIndex = spriteStorage.getMaxUpdatedIndex();

        if (minUpdatedIndex != Integer.MAX_VALUE && minUpdatedIndex != maxUpdatedIndex) {
            boolean debug = Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG;
            if (debug)
                Gdx.app.debug("Particles", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));

            mesh.updateVertices(minUpdatedIndex, spriteStorage.getFloatArray(), minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);

            spriteStorage.resetUpdates();
        }
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        int spriteCount = spriteStorage.getObjectCount();
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG) {
            Gdx.app.debug("Particles", "Rendering " + spriteCount + " particles(s)");
        }
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);

        spriteModel.renderMesh(shaderProgram, mesh, 0, spriteCount, attributeLocations);
    }

    public void updateWithMaxDeathTime(float maxDeathTime) {
        this.maxDeathTime = Math.max(this.maxDeathTime, maxDeathTime);
    }

    public float getMaxDeathTime() {
        return maxDeathTime;
    }
}
