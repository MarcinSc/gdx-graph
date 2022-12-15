package com.gempukku.libgdx.graph.util.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.PropertySource;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;

public class TexturePagedSpriteBatchModel implements SpriteBatchModel {
    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    private final Array<BatchModelWithTextureSignature> batchModelPerTextureSignatures = new Array<>();

    private int identifierCountPerTextures;
    private final SpriteBatchModelProducer spriteBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;
    private final Array<PropertySource> textureUniforms;

    public TexturePagedSpriteBatchModel(GraphModels graphModels, String tag,
                                        SpriteBatchModelProducer spriteBatchModelProducer) {
        this(graphModels, tag, 20000 * 5, spriteBatchModelProducer, new MapWritablePropertyContainer());
    }

    public TexturePagedSpriteBatchModel(GraphModels graphModels, String tag, int identifierCountPerTextures,
                                        SpriteBatchModelProducer spriteBatchModelProducer,
                                        WritablePropertyContainer propertyContainer) {
        this.identifierCountPerTextures = identifierCountPerTextures;
        this.spriteBatchModelProducer = spriteBatchModelProducer;
        this.propertyContainer = propertyContainer;

        ObjectMap<String, PropertySource> shaderProperties = graphModels.getShaderProperties(tag);
        if (shaderProperties == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        textureUniforms = getTextureUniforms(shaderProperties);
    }

    private static Array<PropertySource> getTextureUniforms(ObjectMap<String, PropertySource> shaderProperties) {
        Array<PropertySource> result = new Array<>();
        for (PropertySource value : shaderProperties.values()) {
            if (value.getPropertyLocation() == PropertyLocation.Attribute &&
                    value.getShaderFieldType().getName().equals(ShaderFieldType.TextureRegion))
                result.add(value);
        }
        return result;
    }

    @Override
    public int getSpriteCount() {
        int result = 0;
        for (BatchModelWithTextureSignature batchModelPerTextureSignature : batchModelPerTextureSignatures) {
            if (batchModelPerTextureSignature != null)
                result += batchModelPerTextureSignature.model.getSpriteCount();
        }
        return result;
    }

    @Override
    public boolean isAtCapacity() {
        return false;
    }

    @Override
    public int addSprite(RenderableSprite sprite) {
        String textureSignature = getTextureSignature(sprite);

        int modelIndex = getModelIndex(textureSignature);
        if (modelIndex == -1) {
            HierarchicalPropertyContainer childPropertyContainer = new HierarchicalPropertyContainer(propertyContainer);

            SpriteBatchModel spriteBatchModel = createNewSpriteBatchModel(childPropertyContainer);

            setupTextures(spriteBatchModel, sprite);

            modelIndex = findFirstEmptyModelIndex();
            BatchModelWithTextureSignature newModel = new BatchModelWithTextureSignature(spriteBatchModel, textureSignature);
            if (modelIndex > -1) {
                batchModelPerTextureSignatures.set(modelIndex, newModel);
            } else {
                batchModelPerTextureSignatures.add(newModel);
                modelIndex = batchModelPerTextureSignatures.size - 1;
            }
        }

        int spriteIndexForTextures = batchModelPerTextureSignatures.get(modelIndex).model.addSprite(sprite);
        return identifierCountPerTextures * modelIndex + spriteIndexForTextures;
    }

    private int findFirstEmptyModelIndex() {
        for (int pageIndex = 0; pageIndex < batchModelPerTextureSignatures.size; pageIndex++) {
            BatchModelWithTextureSignature page = batchModelPerTextureSignatures.get(pageIndex);
            if (page == null)
                return pageIndex;
        }
        return -1;
    }

    private int getModelIndex(String textureSignature) {
        for (int modelIndex = 0; modelIndex < batchModelPerTextureSignatures.size; modelIndex++) {
            BatchModelWithTextureSignature model = batchModelPerTextureSignatures.get(modelIndex);
            if (model != null && model.textureSignature.equals(textureSignature))
                return modelIndex;
        }
        return -1;
    }

    private SpriteBatchModel createNewSpriteBatchModel(WritablePropertyContainer propertyContainer) {
        SpriteBatchModel spriteBatchModel = spriteBatchModelProducer.create(propertyContainer);
        spriteBatchModel.setCullingTest(cullingTest);
        spriteBatchModel.setPosition(position);
        spriteBatchModel.setWorldTransform(worldTransform);
        return spriteBatchModel;
    }

    @Override
    public void removeSprite(int spriteIndex) {
        int pageIndex = spriteIndex / identifierCountPerTextures;
        batchModelPerTextureSignatures.get(pageIndex).model.removeSprite(spriteIndex % identifierCountPerTextures);
    }

    @Override
    public int updateSprite(RenderableSprite sprite, int spriteIndex) {
        removeSprite(spriteIndex);
        return addSprite(sprite);
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
        for (BatchModelWithTextureSignature batchModelPerTextureSignature : batchModelPerTextureSignatures) {
            if (batchModelPerTextureSignature != null)
                batchModelPerTextureSignature.model.setCullingTest(cullingTest);
        }
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
        for (BatchModelWithTextureSignature batchModelPerTextureSignature : batchModelPerTextureSignatures) {
            if (batchModelPerTextureSignature != null)
                batchModelPerTextureSignature.model.setPosition(position);
        }
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
        for (BatchModelWithTextureSignature batchModelPerTextureSignature : batchModelPerTextureSignatures) {
            if (batchModelPerTextureSignature != null)
                batchModelPerTextureSignature.model.setWorldTransform(worldTransform);
        }

    }

    @Override
    public void dispose() {
        for (BatchModelWithTextureSignature batchModelPerTextureSignature : batchModelPerTextureSignatures) {
            if (batchModelPerTextureSignature != null)
                batchModelPerTextureSignature.model.dispose();
        }
        batchModelPerTextureSignatures.clear();
    }

    private void setupTextures(SpriteBatchModel spriteBatchModel, RenderableSprite sprite) {
        WritablePropertyContainer propertyContainer = spriteBatchModel.getPropertyContainer();
        for (PropertySource textureUniform : textureUniforms) {
            Object region = sprite.getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            propertyContainer.setValue(textureUniform.getPropertyName(), new TextureRegion(((TextureRegion) region).getTexture()));
        }
    }

    private String getTextureSignature(RenderableSprite sprite) {
        if (textureUniforms.size == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (PropertySource textureUniform : textureUniforms) {
            Object region = sprite.getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            sb.append(((TextureRegion) region).getTexture().getTextureObjectHandle()).append(",");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static class BatchModelWithTextureSignature {
        private SpriteBatchModel model;
        private String textureSignature;

        public BatchModelWithTextureSignature(SpriteBatchModel model, String textureSignature) {
            this.model = model;
            this.textureSignature = textureSignature;
        }
    }
}
