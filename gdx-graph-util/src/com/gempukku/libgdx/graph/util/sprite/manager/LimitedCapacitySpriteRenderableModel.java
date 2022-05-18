package com.gempukku.libgdx.graph.util.sprite.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
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

public class LimitedCapacitySpriteRenderableModel implements SpriteRenderableModel {
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

    private final RenderableSprite[] spritePosition;

    private int spriteCount = 0;
    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    private final ObjectSet<RenderableSprite> updatedSprites = new ObjectSet<>();
    private final ObjectSet<RenderableSprite> allSprites = new ObjectSet<>();

    public LimitedCapacitySpriteRenderableModel(
            boolean staticBatch, int spriteCapacity,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer) {
        this(staticBatch, spriteCapacity, vertexAttributes, vertexPropertySources, propertyContainer,
                new QuadSpriteModel());
    }

    public LimitedCapacitySpriteRenderableModel(
            boolean staticBatch, int spriteCapacity,
            VertexAttributes vertexAttributes, ObjectMap<VertexAttribute, PropertySource> vertexPropertySources,
            WritablePropertyContainer propertyContainer, SpriteModel spriteModel) {
        this.spriteCapacity = spriteCapacity;
        this.propertyContainer = propertyContainer;
        this.spriteModel = spriteModel;

        this.vertexAttributes = vertexAttributes;

        this.vertexPropertySources = vertexPropertySources;

        floatCountPerVertex = vertexAttributes.vertexSize / 4;

        spritePosition = new RenderableSprite[spriteCapacity];

        vertexData = new float[spriteModel.getVertexCount() * floatCountPerVertex * spriteCapacity];

        mesh = new Mesh(staticBatch, true,
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
        return allSprites.contains(sprite);
    }

    @Override
    public boolean addSprite(RenderableSprite sprite) {
        if (spriteCount == spriteCapacity)
            return false;

        spritePosition[spriteCount] = sprite;

        updatedSprites.add(sprite);
        allSprites.add(sprite);

        spriteCount++;

        return true;
    }

    @Override
    public boolean updateSprite(RenderableSprite sprite) {
        if (hasSprite(sprite)) {
            updatedSprites.add(sprite);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeSprite(RenderableSprite sprite) {
        int position = findSpriteIndex(sprite);
        if (position == -1)
            return false;

        updatedSprites.remove(sprite);
        allSprites.remove(sprite);

        if (spriteCount > 1 && position != spriteCount - 1) {
            // Need to shrink the arrays
            spritePosition[position] = spritePosition[spriteCount - 1];
            int sourcePosition = getSpriteDataStart(spriteCount - 1);
            int destinationPosition = getSpriteDataStart(position);
            int floatCount = floatCountPerVertex * spriteModel.getVertexCount();
            System.arraycopy(vertexData, sourcePosition, vertexData, destinationPosition, floatCount);

            markSpriteUpdated(position);
        }
        spriteCount--;

        return true;
    }

    public void removeAllSprites() {
        spriteCount = 0;
        updatedSprites.clear();
        allSprites.clear();
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    private void markSpriteUpdated(int spriteIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, getSpriteDataStart(spriteIndex));
        maxUpdatedIndex = Math.max(maxUpdatedIndex, getSpriteDataStart(spriteIndex + 1));
    }

    private int getSpriteDataStart(int spriteIndex) {
        return spriteIndex * floatCountPerVertex * spriteModel.getVertexCount();
    }

    private int findSpriteIndex(RenderableSprite sprite) {
        for (int i = 0; i < spriteCount; i++) {
            if (spritePosition[i] == sprite)
                return i;
        }
        return -1;
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
        boolean debug = Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG;
        if (debug && !updatedSprites.isEmpty())
            Gdx.app.debug("Sprite", "Updating info of " + updatedSprites.size + " sprite(s)");

        for (RenderableSprite updatedSprite : updatedSprites) {
            updateSpriteData(updatedSprite, findSpriteIndex(updatedSprite));
        }
        updatedSprites.clear();

        if (minUpdatedIndex != Integer.MAX_VALUE) {
            if (debug)
                Gdx.app.debug("Sprite", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));
            mesh.updateVertices(minUpdatedIndex, vertexData, minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
            minUpdatedIndex = Integer.MAX_VALUE;
            maxUpdatedIndex = -1;
        }
    }

    @Override
    public void render(Camera camera, ShaderProgram shaderProgram, IntMapping<String> propertyToLocationMapping) {
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Sprite", "Rendering " + spriteCount + " sprite(s)");
        if (attributeLocations == null)
            attributeLocations = GraphModelUtil.getAttributeLocations(shaderProgram, vertexAttributes);

        spriteModel.renderMesh(shaderProgram, mesh, 0, spriteCount, attributeLocations);
    }
}
