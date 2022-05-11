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
    private final ObjectMap<String, SpriteBatchModel> batchModelPerTextureSignature = new ObjectMap<>();

    private SpriteBatchModelProducer spriteBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;
    private final Array<PropertySource> textureUniforms;

    public TexturePagedSpriteBatchModel(GraphModels graphModels, String tag,
                                        SpriteBatchModelProducer spriteBatchModelProducer) {
        this(graphModels, tag, spriteBatchModelProducer, new MapWritablePropertyContainer());
    }

    public TexturePagedSpriteBatchModel(GraphModels graphModels, String tag,
                                        SpriteBatchModelProducer spriteBatchModelProducer,
                                        WritablePropertyContainer propertyContainer) {
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
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            result += value.getSpriteCount();
        }
        return result;
    }

    @Override
    public boolean addSprite(RenderableSprite sprite) {
        String textureSignature = getTextureSignature(sprite);

        SpriteBatchModel spriteBatchModel = batchModelPerTextureSignature.get(textureSignature);
        if (spriteBatchModel == null) {
            HierarchicalPropertyContainer childPropertyContainer = new HierarchicalPropertyContainer(propertyContainer);

            spriteBatchModel = createNewSpriteBatchModel(childPropertyContainer);

            setupTextures(spriteBatchModel, sprite);
            batchModelPerTextureSignature.put(textureSignature, spriteBatchModel);
        }
        return spriteBatchModel.addSprite(sprite);
    }

    private SpriteBatchModel createNewSpriteBatchModel(WritablePropertyContainer propertyContainer) {
        SpriteBatchModel spriteBatchModel = spriteBatchModelProducer.create(propertyContainer);
        spriteBatchModel.setCullingTest(cullingTest);
        spriteBatchModel.setPosition(position);
        spriteBatchModel.setWorldTransform(worldTransform);
        return spriteBatchModel;
    }

    @Override
    public boolean hasSprite(RenderableSprite sprite) {
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            if (value.hasSprite(sprite))
                return true;
        }
        return false;
    }

    @Override
    public boolean removeSprite(RenderableSprite sprite) {
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            if (value.removeSprite(sprite))
                return true;
        }

        return false;
    }


    @Override
    public boolean updateSprite(RenderableSprite sprite) {
        String oldTextureSignature = getOldTextureSignature(sprite);
        if (oldTextureSignature == null)
            return false;

        String textureSignature = getTextureSignature(sprite);
        if (textureSignature.equals(oldTextureSignature)) {
            return batchModelPerTextureSignature.get(textureSignature).updateSprite(sprite);
        } else {
            batchModelPerTextureSignature.get(oldTextureSignature).removeSprite(sprite);
            return batchModelPerTextureSignature.get(textureSignature).addSprite(sprite);
        }
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.setCullingTest(cullingTest);
        }
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.setPosition(position);
        }
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.setWorldTransform(worldTransform);
        }
    }

    @Override
    public void dispose() {
        for (SpriteBatchModel value : batchModelPerTextureSignature.values()) {
            value.dispose();
        }
        batchModelPerTextureSignature.clear();
    }

    private String getOldTextureSignature(RenderableSprite sprite) {
        for (ObjectMap.Entry<String, SpriteBatchModel> keyToSpriteBatchModel : batchModelPerTextureSignature) {
            if (keyToSpriteBatchModel.value.hasSprite(sprite))
                return keyToSpriteBatchModel.key;
        }
        return null;
    }

    private void setupTextures(SpriteBatchModel spriteBatchModel, RenderableSprite sprite) {
//        WritablePropertyContainer propertyContainer = spriteBatchModel.getPropertyContainer();
//        for (PropertySource textureUniform : textureUniforms) {
//            Object region = sprite.getPropertyContainer().getValue(textureUniform.getPropertyName());
//            region = textureUniform.getValueToUse(region);
//            propertyContainer.setValue(textureUniform.getPropertyName(), new TextureRegion(((TextureRegion) region).getTexture()));
//        }
    }

    private String getTextureSignature(RenderableSprite sprite) {
        if (textureUniforms.size == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (PropertySource textureUniform : textureUniforms) {
            Object region = sprite.getPropertyContainer().getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            sb.append(((TextureRegion) region).getTexture().getTextureObjectHandle()).append(",");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
}
