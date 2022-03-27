package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.RenderableSprite;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteData;
import com.gempukku.libgdx.graph.plugin.sprites.ValuePerVertex;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class BatchedSpriteData implements SpriteData {
    private final int spriteCapacity;
    private final int floatCountPerVertex;
    private final String tag;
    private final VertexAttributes vertexAttributes;
    private final ObjectMap<String, PropertySource> shaderProperties;
    private final IntMap<String> propertyIndexNames;

    private final GraphSprite[] graphSpritesPosition;
    private final Mesh mesh;

    private final float[] vertexData;
    private int spriteCount = 0;
    private int minUpdatedIndex = Integer.MAX_VALUE;
    private int maxUpdatedIndex = -1;

    private final ObjectSet<GraphSprite> updatedSprites = new ObjectSet<>();

    public BatchedSpriteData(boolean staticCache, int spriteCapacity, int floatCountPerVertex,
                             String tag,
                             VertexAttributes vertexAttributes, ObjectMap<String, PropertySource> shaderProperties,
                             IntMap<String> propertyIndexNames) {
        this.spriteCapacity = spriteCapacity;
        this.floatCountPerVertex = floatCountPerVertex;
        this.tag = tag;
        this.vertexAttributes = vertexAttributes;
        this.shaderProperties = shaderProperties;
        this.propertyIndexNames = propertyIndexNames;

        graphSpritesPosition = new GraphSpriteImpl[spriteCapacity];
        vertexData = new float[4 * floatCountPerVertex * spriteCapacity];

        int numberOfIndices = 6 * spriteCapacity;
        mesh = new Mesh(staticCache, true, 4 * spriteCapacity, numberOfIndices, vertexAttributes);
        mesh.setVertices(vertexData);
        short[] indices = new short[numberOfIndices];
        int vertexIndex = 0;
        for (int i = 0; i < numberOfIndices; i += 6) {
            indices[i + 0] = (short) (vertexIndex * 4 + 0);
            indices[i + 1] = (short) (vertexIndex * 4 + 2);
            indices[i + 2] = (short) (vertexIndex * 4 + 1);
            indices[i + 3] = (short) (vertexIndex * 4 + 2);
            indices[i + 4] = (short) (vertexIndex * 4 + 3);
            indices[i + 5] = (short) (vertexIndex * 4 + 1);
            vertexIndex++;
        }
        mesh.setIndices(indices, 0, indices.length);
    }

    public boolean addGraphSprite(GraphSprite sprite) {
        if (spriteCount == spriteCapacity)
            return false;

        graphSpritesPosition[spriteCount] = sprite;

        updatedSprites.add(sprite);

        spriteCount++;

        return true;
    }

    public void updateGraphSprite(GraphSprite sprite) {
        updatedSprites.add(sprite);
    }

    public boolean removeGraphSprite(GraphSprite sprite) {
        int position = findSpriteIndex(sprite);
        if (position == -1)
            return false;

        updatedSprites.remove(sprite);

        if (spriteCount > 1 && position != spriteCount - 1) {
            // Need to shrink the arrays
            graphSpritesPosition[position] = graphSpritesPosition[spriteCount - 1];
            int sourcePosition = getSpriteDataStart(spriteCount - 1);
            int destinationPosition = getSpriteDataStart(position);
            int floatCount = floatCountPerVertex * 4;
            System.arraycopy(vertexData, sourcePosition, vertexData, destinationPosition, floatCount);

            markSpriteUpdated(position);
        }
        spriteCount--;

        return true;
    }

    private void updateSpriteData(GraphSprite sprite, int spriteIndex) {
        RenderableSprite renderableSprite = sprite.getRenderableSprite();

        int spriteDataStart = getSpriteDataStart(spriteIndex);
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            String alias = vertexAttribute.alias;
            int attributeOffset = vertexAttribute.offset / 4;
            if (alias.equals(ShaderProgram.POSITION_ATTRIBUTE)) {
                Vector3 position = renderableSprite.getPosition();
                for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                    int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                    vertexData[vertexOffset + attributeOffset + 0] = position.x;
                    vertexData[vertexOffset + attributeOffset + 1] = position.y;
                    vertexData[vertexOffset + attributeOffset + 2] = position.z;
                }
            } else if (alias.equals(ShaderProgram.TEXCOORD_ATTRIBUTE + 0)) {
                for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                    int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                    vertexData[vertexOffset + attributeOffset + 0] = vertexIndex % 2;
                    vertexData[vertexOffset + attributeOffset + 1] = (float) (vertexIndex / 2);
                }
            } else if (alias.startsWith("a_property_")) {
                int propertyIndex = Integer.parseInt(alias.substring(11));
                String propertyName = propertyIndexNames.get(propertyIndex);
                PropertySource propertySource = shaderProperties.get(propertyName);

                ShaderFieldType shaderFieldType = propertySource.getShaderFieldType();
                Object attributeValue = renderableSprite.getPropertyContainer(tag).getValue(propertyName);
                if (attributeValue instanceof ValuePerVertex) {
                    for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                        int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                        Object vertexValue = ((ValuePerVertex) attributeValue).getValue(vertexIndex);
                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, propertySource.getValueToUse(vertexValue));
                    }
                } else {
                    attributeValue = propertySource.getValueToUse(attributeValue);
                    for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                        int vertexOffset = spriteDataStart + vertexIndex * floatCountPerVertex;

                        shaderFieldType.setValueInAttributesArray(vertexData, vertexOffset + attributeOffset, attributeValue);
                    }
                }
            }
        }

        markSpriteUpdated(spriteIndex);
    }

    @Override
    public void prepareForRender(ShaderContextImpl shaderContext) {
        boolean debug = Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG;
        if (debug && !updatedSprites.isEmpty())
            Gdx.app.debug("Sprite", "Updating info of " + updatedSprites.size + " sprite(s)");

        for (GraphSprite updatedSprite : updatedSprites) {
            updateSpriteData(updatedSprite, findSpriteIndex(updatedSprite));
        }
        updatedSprites.clear();

        shaderContext.setLocalPropertyContainer(graphSpritesPosition[0].getRenderableSprite().getPropertyContainer(tag));
        if (minUpdatedIndex != Integer.MAX_VALUE) {
            if (debug)
                Gdx.app.debug("Sprite", "Updating vertex array - float count: " + (maxUpdatedIndex - minUpdatedIndex));
            mesh.updateVertices(minUpdatedIndex, vertexData, minUpdatedIndex, maxUpdatedIndex - minUpdatedIndex);
            minUpdatedIndex = Integer.MAX_VALUE;
            maxUpdatedIndex = -1;
        }
    }

    @Override
    public void render(ShaderContextImpl shaderContext, ShaderProgram shaderProgram, int[] attributeLocations) {
        if (Gdx.app.getLogLevel() >= Gdx.app.LOG_DEBUG)
            Gdx.app.debug("Sprite", "Rendering " + spriteCount + " sprite(s)");
        mesh.bind(shaderProgram, attributeLocations);
        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, 6 * spriteCount, GL20.GL_UNSIGNED_SHORT, 0);
        mesh.unbind(shaderProgram, attributeLocations);
    }

    public boolean hasSprites() {
        return spriteCount > 0;
    }

    public void dispose() {
        mesh.dispose();
    }

    private void markSpriteUpdated(int spriteIndex) {
        minUpdatedIndex = Math.min(minUpdatedIndex, getSpriteDataStart(spriteIndex));
        maxUpdatedIndex = Math.max(maxUpdatedIndex, getSpriteDataStart(spriteIndex + 1));
    }

    private int getSpriteDataStart(int spriteIndex) {
        return spriteIndex * floatCountPerVertex * 4;
    }

    private int findSpriteIndex(GraphSprite sprite) {
        for (int i = 0; i < spriteCount; i++) {
            if (graphSpritesPosition[i] == sprite)
                return i;
        }
        return -1;
    }

    public boolean hasSprite(GraphSprite graphSprite) {
        return findSpriteIndex(graphSprite) != -1;
    }
}
