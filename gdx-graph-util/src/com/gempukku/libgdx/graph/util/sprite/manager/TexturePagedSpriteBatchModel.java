package com.gempukku.libgdx.graph.util.sprite.manager;

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
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;

public class TexturePagedSpriteBatchModel<T extends SpriteBatchModel> implements SpriteBatchModel {
    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    private final Array<BatchModelWithTextureSignature<T>> pages = new Array<>();

    private final DisposableProducer<T> spriteBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;
    private final Array<ShaderPropertySource> textureUniforms;

    public TexturePagedSpriteBatchModel(GraphModels graphModels, String tag,
                                        DisposableProducer<T> spriteBatchModelProducer) {
        this(graphModels, tag, spriteBatchModelProducer, new MapWritablePropertyContainer());
    }

    public TexturePagedSpriteBatchModel(GraphModels graphModels, String tag,
                                        DisposableProducer<T> spriteBatchModelProducer,
                                        WritablePropertyContainer propertyContainer) {
        this.spriteBatchModelProducer = spriteBatchModelProducer;
        this.propertyContainer = propertyContainer;

        ObjectMap<String, ShaderPropertySource> shaderProperties = graphModels.getShaderProperties(tag);
        if (shaderProperties == null)
            throw new GdxRuntimeException("Unable to locate shader with tag: " + tag);

        textureUniforms = getTextureUniforms(shaderProperties);
    }

    private static Array<ShaderPropertySource> getTextureUniforms(ObjectMap<String, ShaderPropertySource> shaderProperties) {
        Array<ShaderPropertySource> result = new Array<>();
        for (ShaderPropertySource value : shaderProperties.values()) {
            if (value.getPropertyLocation() == PropertyLocation.Attribute &&
                    value.getShaderFieldType().getName().equals(ShaderFieldType.TextureRegion))
                result.add(value);
        }
        return result;
    }

    @Override
    public int getSpriteCount() {
        int result = 0;
        for (BatchModelWithTextureSignature batchModelPerTextureSignature : pages) {
            result += batchModelPerTextureSignature.model.getSpriteCount();
        }
        return result;
    }

    @Override
    public boolean isAtCapacity() {
        return false;
    }

    @Override
    public SpriteReference addSprite(RenderableSprite sprite) {
        String textureSignature = getTextureSignature(sprite);
        BatchModelWithTextureSignature batchWithSignature = getBatchWithSignature(textureSignature, sprite);

        return batchWithSignature.model.addSprite(sprite);
    }

    private BatchModelWithTextureSignature getBatchWithSignature(String textureSignature, RenderableSprite renderableSprite) {
        for (BatchModelWithTextureSignature<T> page : pages) {
            if (page.textureSignature.equals(textureSignature))
                return page;
        }

        T spriteBatchModel = createNewSpriteBatchModel();
        BatchModelWithTextureSignature newModel = new BatchModelWithTextureSignature(spriteBatchModel, textureSignature);
        setupTextures(spriteBatchModel, renderableSprite);
        pages.add(newModel);
        return newModel;
    }

    @Override
    public boolean containsSprite(SpriteReference spriteReference) {
        for (BatchModelWithTextureSignature<T> batchModelPerTextureSignature : pages) {
            if (batchModelPerTextureSignature.model.containsSprite(spriteReference))
                return true;
        }
        return false;
    }

    private T createNewSpriteBatchModel() {
        T spriteBatchModel = spriteBatchModelProducer.create();
        spriteBatchModel.setCullingTest(cullingTest);
        spriteBatchModel.setPosition(position);
        spriteBatchModel.setWorldTransform(worldTransform);
        return spriteBatchModel;
    }

    @Override
    public void removeSprite(SpriteReference spriteReference) {
        for (BatchModelWithTextureSignature<T> page : pages) {
            T model = page.model;
            if (model.containsSprite(spriteReference)) {
                model.removeSprite(spriteReference);
                if (model.getSpriteCount() == 0) {
                    spriteBatchModelProducer.dispose(model);
                    pages.removeValue(page, true);
                }
                return;
            }
        }
    }

    @Override
    public SpriteReference updateSprite(RenderableSprite sprite, SpriteReference spriteReference) {
        removeSprite(spriteReference);
        return addSprite(sprite);
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
        for (BatchModelWithTextureSignature<T> batchModelPerTextureSignature : pages) {
            batchModelPerTextureSignature.model.setCullingTest(cullingTest);
        }
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
        for (BatchModelWithTextureSignature<T> batchModelPerTextureSignature : pages) {
            batchModelPerTextureSignature.model.setPosition(position);
        }
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
        for (BatchModelWithTextureSignature<T> batchModelPerTextureSignature : pages) {
            batchModelPerTextureSignature.model.setWorldTransform(worldTransform);
        }

    }

    @Override
    public void dispose() {
        for (BatchModelWithTextureSignature<T> batchModelPerTextureSignature : pages) {
            spriteBatchModelProducer.dispose(batchModelPerTextureSignature.model);
        }
        pages.clear();
    }

    private void setupTextures(SpriteBatchModel spriteBatchModel, RenderableSprite sprite) {
        WritablePropertyContainer propertyContainer = spriteBatchModel.getPropertyContainer();
        for (ShaderPropertySource textureUniform : textureUniforms) {
            Object region = sprite.getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            propertyContainer.setValue(textureUniform.getPropertyName(), new TextureRegion(((TextureRegion) region).getTexture()));
        }
    }

    private String getTextureSignature(RenderableSprite sprite) {
        if (textureUniforms.size == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (ShaderPropertySource textureUniform : textureUniforms) {
            Object region = sprite.getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            sb.append(((TextureRegion) region).getTexture().getTextureObjectHandle()).append(",");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static class BatchModelWithTextureSignature<T extends SpriteBatchModel> {
        private T model;
        private String textureSignature;

        public BatchModelWithTextureSignature(T model, String textureSignature) {
            this.model = model;
            this.textureSignature = textureSignature;
        }
    }
}
