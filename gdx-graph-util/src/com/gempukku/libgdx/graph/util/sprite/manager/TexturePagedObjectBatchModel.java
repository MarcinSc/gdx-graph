package com.gempukku.libgdx.graph.util.sprite.manager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PropertyContainer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.culling.CullingTest;
import com.gempukku.libgdx.graph.util.sprite.ObjectBatchModel;

public class TexturePagedObjectBatchModel<T extends PropertyContainer, U, V extends ObjectBatchModel<T, U>> implements ObjectBatchModel<T, U> {
    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    private final Array<BatchModelWithTextureSignature<V>> pages = new Array<>();

    private final DisposableProducer<V> objectBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;
    private final Array<ShaderPropertySource> textureUniforms;

    public TexturePagedObjectBatchModel(GraphModels graphModels, String tag,
                                        DisposableProducer<V> objectBatchModelProducer) {
        this(graphModels, tag, objectBatchModelProducer, new MapWritablePropertyContainer());
    }

    public TexturePagedObjectBatchModel(GraphModels graphModels, String tag,
                                        DisposableProducer<V> objectBatchModelProducer,
                                        WritablePropertyContainer propertyContainer) {
        this.objectBatchModelProducer = objectBatchModelProducer;
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
    public boolean canStore(PropertyContainer object) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        for (BatchModelWithTextureSignature<V> page : pages) {
            if (!page.model.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public U addObject(T object) {
        String textureSignature = getTextureSignature(object);
        BatchModelWithTextureSignature<V> batchWithSignature = getBatchWithSignature(textureSignature, object);

        return batchWithSignature.model.addObject(object);
    }

    private BatchModelWithTextureSignature<V> getBatchWithSignature(String textureSignature, PropertyContainer object) {
        for (BatchModelWithTextureSignature<V> page : pages) {
            if (page.textureSignature.equals(textureSignature))
                return page;
        }

        V objectBatchModel = createNewObjectBatchModel();
        BatchModelWithTextureSignature<V> newModel = new BatchModelWithTextureSignature<>(objectBatchModel, textureSignature);
        setupTextures(objectBatchModel, object);
        pages.add(newModel);
        return newModel;
    }

    @Override
    public boolean containsObject(U objectReference) {
        for (BatchModelWithTextureSignature<V> batchModelPerTextureSignature : pages) {
            if (batchModelPerTextureSignature.model.containsObject(objectReference))
                return true;
        }
        return false;
    }

    private V createNewObjectBatchModel() {
        V objectBatchModel = objectBatchModelProducer.create();
        objectBatchModel.setCullingTest(cullingTest);
        objectBatchModel.setPosition(position);
        objectBatchModel.setWorldTransform(worldTransform);
        return objectBatchModel;
    }

    @Override
    public void removeObject(U objectReference) {
        for (BatchModelWithTextureSignature<V> page : pages) {
            V model = page.model;
            if (model.containsObject(objectReference)) {
                model.removeObject(objectReference);
                if (model.isEmpty()) {
                    objectBatchModelProducer.dispose(model);
                    pages.removeValue(page, true);
                }
                return;
            }
        }
    }

    @Override
    public U updateObject(T object, U objectReference) {
        removeObject(objectReference);
        return addObject(object);
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
        for (BatchModelWithTextureSignature<V> batchModelPerTextureSignature : pages) {
            batchModelPerTextureSignature.model.setCullingTest(cullingTest);
        }
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
        for (BatchModelWithTextureSignature<V> batchModelPerTextureSignature : pages) {
            batchModelPerTextureSignature.model.setPosition(position);
        }
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
        for (BatchModelWithTextureSignature<V> batchModelPerTextureSignature : pages) {
            batchModelPerTextureSignature.model.setWorldTransform(worldTransform);
        }

    }

    @Override
    public void dispose() {
        for (BatchModelWithTextureSignature<V> batchModelPerTextureSignature : pages) {
            objectBatchModelProducer.dispose(batchModelPerTextureSignature.model);
        }
        pages.clear();
    }

    private void setupTextures(ObjectBatchModel<T, U> objectBatchModel, PropertyContainer object) {
        WritablePropertyContainer propertyContainer = objectBatchModel.getPropertyContainer();
        for (ShaderPropertySource textureUniform : textureUniforms) {
            Object region = object.getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            propertyContainer.setValue(textureUniform.getPropertyName(), new TextureRegion(((TextureRegion) region).getTexture()));
        }
    }

    private String getTextureSignature(PropertyContainer object) {
        if (textureUniforms.size == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (ShaderPropertySource textureUniform : textureUniforms) {
            Object region = object.getValue(textureUniform.getPropertyName());
            region = textureUniform.getValueToUse(region);
            sb.append(((TextureRegion) region).getTexture().getTextureObjectHandle()).append(",");
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private class BatchModelWithTextureSignature<V> {
        private final V model;
        private final String textureSignature;

        public BatchModelWithTextureSignature(V model, String textureSignature) {
            this.model = model;
            this.textureSignature = textureSignature;
        }
    }
}
