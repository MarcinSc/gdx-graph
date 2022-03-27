package com.gempukku.libgdx.graph.plugin.sprites.impl;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.SpriteGraphShader;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public class BatchedTagSpriteData implements Disposable {
    private final String tag;
    private final VertexAttributes vertexAttributes;
    private final int numberOfSpritesPerBatch;
    private final ObjectMap<String, PropertySource> shaderProperties;
    private final Array<String> textureUniformNames;
    private final IntMap<String> propertyIndexNames = new IntMap<>();
    private final int floatCount;

    private final ObjectMap<String, Array<BatchedSpriteData>> dynamicCachedSpritesPerTextureSet = new ObjectMap<>();

    public BatchedTagSpriteData(String tag, VertexAttributes vertexAttributes, int numberOfSpritesPerBatch, ObjectMap<String, PropertySource> shaderProperties,
                                Array<String> textureUniformNames) {
        this.tag = tag;
        this.vertexAttributes = vertexAttributes;
        this.numberOfSpritesPerBatch = numberOfSpritesPerBatch;
        this.shaderProperties = shaderProperties;
        this.textureUniformNames = textureUniformNames;

        for (ObjectMap.Entry<String, PropertySource> shaderProperty : shaderProperties) {
            propertyIndexNames.put(shaderProperty.value.getPropertyIndex(), shaderProperty.key);
        }

        int fCount = 0;
        for (VertexAttribute vertexAttribute : vertexAttributes) {
            fCount += vertexAttribute.numComponents;
        }
        floatCount = fCount;
    }

    public void addSprite(GraphSprite sprite) {
        String textureSignature = getTextureSignature(sprite);
        Array<BatchedSpriteData> dynamicCachedSpriteData = dynamicCachedSpritesPerTextureSet.get(textureSignature);
        if (dynamicCachedSpriteData == null) {
            dynamicCachedSpriteData = new Array<>();
            dynamicCachedSpritesPerTextureSet.put(textureSignature, dynamicCachedSpriteData);
        }

        for (BatchedSpriteData dynamicCachedSprite : dynamicCachedSpriteData) {
            if (dynamicCachedSprite.addGraphSprite(sprite))
                return;
        }
        BatchedSpriteData batchedSpriteData = new BatchedSpriteData(false, numberOfSpritesPerBatch, floatCount,
                tag, vertexAttributes, shaderProperties, propertyIndexNames);
        dynamicCachedSpriteData.add(batchedSpriteData);
        batchedSpriteData.addGraphSprite(sprite);
    }

    public void spriteUpdated(GraphSprite graphSprite) {
        BatchedSpriteData batchedSpriteData = findCachedSpriteDataContainingSprite(graphSprite);
        if (batchedSpriteData == null) {
            // Texture change
            removeRegardlessOfTexture(graphSprite);
            addSprite(graphSprite);
        } else {
            batchedSpriteData.updateGraphSprite(graphSprite);
        }
    }

    private void removeRegardlessOfTexture(GraphSprite graphSprite) {
        for (ObjectMap.Entry<String, Array<BatchedSpriteData>> dynamicCachedSprites : new ObjectMap.Entries<>(dynamicCachedSpritesPerTextureSet)) {
            for (BatchedSpriteData dynamicCachedSprite : dynamicCachedSprites.value) {
                if (dynamicCachedSprite.removeGraphSprite(graphSprite)) {
                    // Cleanup unneeded collections
                    if (!dynamicCachedSprite.hasSprites()) {
                        dynamicCachedSprites.value.removeValue(dynamicCachedSprite, true);
                        dynamicCachedSprite.dispose();

                        if (dynamicCachedSprites.value.size == 0) {
                            dynamicCachedSpritesPerTextureSet.remove(dynamicCachedSprites.key);
                        }
                    }
                    return;
                }
            }
        }
    }

    private BatchedSpriteData findCachedSpriteDataContainingSprite(GraphSprite graphSprite) {
        String textureSignature = getTextureSignature(graphSprite);
        Array<BatchedSpriteData> array = dynamicCachedSpritesPerTextureSet.get(textureSignature);
        if (array != null) {
            for (BatchedSpriteData batchedSpriteData : array) {
                if (batchedSpriteData.hasSprite(graphSprite))
                    return batchedSpriteData;
            }
        }

        return null;
    }

    public void removeSprite(GraphSprite sprite) {
        String textureSignature = getTextureSignature(sprite);
        Array<BatchedSpriteData> dynamicCachedSprites = dynamicCachedSpritesPerTextureSet.get(textureSignature);
        if (dynamicCachedSprites != null) {
            for (BatchedSpriteData dynamicCachedSprite : dynamicCachedSprites) {
                if (dynamicCachedSprite.removeGraphSprite(sprite)) {
                    // Cleanup unneeded collections
                    if (!dynamicCachedSprite.hasSprites()) {
                        dynamicCachedSprites.removeValue(dynamicCachedSprite, true);
                        dynamicCachedSprite.dispose();

                        if (dynamicCachedSprites.size == 0) {
                            dynamicCachedSpritesPerTextureSet.remove(textureSignature);
                        }
                    }
                    return;
                }
            }
        }
    }

    public boolean hasSprites() {
        for (Array<BatchedSpriteData> array : dynamicCachedSpritesPerTextureSet.values()) {
            for (BatchedSpriteData batchedSpriteData : array) {
                if (batchedSpriteData.hasSprites())
                    return true;
            }
        }

        return false;
    }

    public Iterable<Array<BatchedSpriteData>> getSpriteData() {
        return dynamicCachedSpritesPerTextureSet.values();
    }

    public void render(SpriteGraphShader shader, ShaderContextImpl shaderContext) {
        for (Array<BatchedSpriteData> array : dynamicCachedSpritesPerTextureSet.values()) {
            for (BatchedSpriteData batchedSpriteData : array) {
                batchedSpriteData.prepareForRender(shaderContext);
                shader.renderSprites(shaderContext, batchedSpriteData);
            }
        }
    }

    @Override
    public void dispose() {
        for (Array<BatchedSpriteData> array : dynamicCachedSpritesPerTextureSet.values()) {
            for (BatchedSpriteData batchedSpriteData : array) {
                batchedSpriteData.dispose();
            }
        }
    }

    private String getTextureSignature(GraphSprite graphSprite) {
        if (textureUniformNames.size == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < textureUniformNames.size; i++) {
            String propertyName = textureUniformNames.get(i);
            PropertySource propertySource = shaderProperties.get(propertyName);
            Object region = graphSprite.getRenderableSprite().getPropertyContainer(tag).getValue(propertyName);
            region = propertySource.getValueToUse(region);
            sb.append(((TextureRegion) region).getTexture().getTextureObjectHandle()).append(",");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
