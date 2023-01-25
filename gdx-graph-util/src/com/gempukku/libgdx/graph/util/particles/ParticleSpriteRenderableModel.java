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
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.IntMapping;
import com.gempukku.libgdx.graph.util.ValuePerVertex;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.SpriteRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteStorage;

public class ParticleSpriteRenderableModel implements SpriteRenderableModel {
    private float maxDeathTime = Float.MIN_VALUE;

    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources;
    private final SpriteModel spriteModel;
    private final int floatCountPerVertex;
    private final VertexAttributes vertexAttributes;
    private int[] attributeLocations;

    private SpriteStorage<RenderableSprite> spriteStorage;

    public ParticleSpriteRenderableModel(
            int spriteCapacity,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer) {
        this(spriteCapacity, vertexAttributes, vertexPropertySources, propertyContainer,
                new QuadSpriteModel());
    }

    public ParticleSpriteRenderableModel(
            int spriteCapacity,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.propertyContainer = propertyContainer;
        this.spriteModel = spriteModel;

        this.vertexAttributes = vertexAttributes;

        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;

        final int floatCountPerSprite = floatCountPerVertex * spriteModel.getVertexCount();

        spriteStorage = new SpriteStorage<>(spriteCapacity,
                new SpriteSerializer<RenderableSprite>() {
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
        return spriteStorage.getSpriteCount();
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
    public SpriteReference addSprite(RenderableSprite sprite) {
        return spriteStorage.addSprite(sprite);
    }

    @Override
    public boolean containsSprite(SpriteReference spriteReference) {
        return spriteStorage.containsSprite(spriteReference);
    }

    @Override
    public SpriteReference updateSprite(RenderableSprite sprite, SpriteReference spriteReference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSprite(SpriteReference spriteReference) {
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

            ShaderPropertySource shaderPropertySource = vertexPropertySources.get(vertexAttribute);
            if (shaderPropertySource == null) {
                for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                    int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;
                    sprite.setUnknownPropertyInAttribute(vertexAttribute, vertexData, vertexOffset + attributeOffset);
                }
            } else {
                ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
                Object attributeValue = sprite.getValue(shaderPropertySource.getPropertyName());
                if (attributeValue instanceof ValuePerVertex) {
                    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
                        int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                        Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, shaderPropertySource.getValueToUse(vertexValue));
                    }
                } else {
                    attributeValue = shaderPropertySource.getValueToUse(attributeValue);
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
        return spriteStorage.getSpriteCount() > 0 && !isCulled(camera);
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

        if (minUpdatedIndex < maxUpdatedIndex) {
            if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
                Gdx.app.debug("Particles", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));
            mesh.updateVertices(minUpdatedIndex, spriteStorage.getFloatArray(), minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
            spriteStorage.resetUpdates();
        }
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        int spriteCount = spriteStorage.getSpriteCount();
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Particles", "Rendering " + spriteCount + " particles(s)");
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
