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

public class ParticleSpriteRenderableModel implements SpriteRenderableModel {
    private float maxDeathTime = Float.MIN_VALUE;

    private final Matrix4 worldTransform = new Matrix4();
    private final Vector3 position = new Vector3();
    private final WritablePropertyContainer propertyContainer;
    private CullingTest cullingTest;

    private final Mesh mesh;
    private final int spriteCapacity;
    private final ObjectMap<VertexAttribute, PropertySource> vertexPropertySources;
    private final float[] vertexData;
    private final SpriteModel spriteModel;
    private final int floatCountPerVertex;
    private final VertexAttributes vertexAttributes;
    private int[] attributeLocations;

    private int spriteCount = 0;
    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    public ParticleSpriteRenderableModel(
            int spriteCapacity,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer) {
        this(spriteCapacity, vertexAttributes, vertexPropertySources, propertyContainer,
                new QuadSpriteModel());
    }

    public ParticleSpriteRenderableModel(
            int spriteCapacity,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.spriteCapacity = spriteCapacity;
        this.propertyContainer = propertyContainer;
        this.spriteModel = spriteModel;

        this.vertexAttributes = vertexAttributes;

        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;

        vertexData = new float[spriteModel.getVertexCount() * floatCountPerVertex * spriteCapacity];

        mesh = new Mesh(false, true,
                spriteModel.getVertexCount() * spriteCapacity,
                spriteModel.getIndexCount() * spriteCapacity, vertexAttributes);
        mesh.setVertices(vertexData);

        short[] indices = new short[spriteModel.getIndexCount() * spriteCapacity];
        spriteModel.initializeIndexBuffer(indices, spriteCapacity);

        mesh.setIndices(indices, 0, indices.length);
    }

    @Override
    public int getSpriteCount() {
        return spriteCount;
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
    public boolean hasSprite(RenderableSprite sprite) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addSprite(RenderableSprite sprite) {
        if (spriteCount == spriteCapacity)
            return false;

        updateSpriteData(sprite, spriteCount);

        spriteCount++;

        return true;
    }

    @Override
    public boolean updateSprite(RenderableSprite sprite) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeSprite(RenderableSprite sprite) {
        throw new UnsupportedOperationException();
    }

    public void removeAllSprites() {
        spriteCount = 0;
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    private int getSpriteDataStart(int spriteIndex) {
        return spriteIndex * floatCountPerVertex * spriteModel.getVertexCount();
    }

    private void updateSpriteData(RenderableSprite sprite, int spriteIndex) {
        int spriteDataStart = getSpriteDataStart(spriteIndex);
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

        markSpriteUpdated(spriteIndex);
    }

    private void markSpriteUpdated(int spriteIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, getSpriteDataStart(spriteIndex));
        maxUpdatedIndex = Math.max(maxUpdatedIndex, getSpriteDataStart(spriteIndex + 1));
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
        return spriteCount > 0 && !isCulled(camera);
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
        if (minUpdatedIndex != Integer.MAX_VALUE) {
            boolean debug = Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG;
            if (debug)
                Gdx.app.debug("Particles", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));

            mesh.updateVertices(minUpdatedIndex, vertexData, minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
            minUpdatedIndex = Integer.MAX_VALUE;
            maxUpdatedIndex = -1;
        }
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
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
