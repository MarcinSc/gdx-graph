package com.gempukku.libgdx.graph.util.storage;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.PropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.util.DisposableProducer;
import com.gempukku.libgdx.graph.shader.GraphModels;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.property.PropertyLocation;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.culling.CullingTest;

public class TexturePagedMultiPartBatchModel<T extends PropertyContainer, U> implements MultiPartBatchModel<T, U> {
    private final Vector3 position = new Vector3();
    private final Matrix4 worldTransform = new Matrix4();
    private CullingTest cullingTest;

    private final Array<BatchModelWithTextureSignature> pages = new Array<>();

    private final DisposableProducer objectBatchModelProducer;
    private final WritablePropertyContainer propertyContainer;
    private final Array<ShaderPropertySource> textureUniforms;

    public TexturePagedMultiPartBatchModel(GraphModels graphModels, String tag,
                                           DisposableProducer<? extends MultiPartBatchModel<T, U>> objectBatchModelProducer) {
        this(graphModels, tag, objectBatchModelProducer, new MapWritablePropertyContainer());
    }

    public TexturePagedMultiPartBatchModel(GraphModels graphModels, String tag,
                                           DisposableProducer<? extends MultiPartBatchModel<T, U>> objectBatchModelProducer,
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
    public boolean canStore(PropertyContainer part) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        for (BatchModelWithTextureSignature page : pages) {
            if (!page.model.isEmpty())
                return false;
        }

        return true;
    }

    @Override
    public U addPart(T object) {
        String textureSignature = getTextureSignature(object);
        BatchModelWithTextureSignature batchWithSignature = getBatchWithSignature(textureSignature, object);

        return batchWithSignature.model.addPart(object);
    }

    private BatchModelWithTextureSignature getBatchWithSignature(String textureSignature, PropertyContainer object) {
        for (BatchModelWithTextureSignature page : pages) {
            if (page.textureSignature.equals(textureSignature))
                return page;
        }

        MultiPartBatchModel<T, U> multiPartBatchModel = createNewObjectBatchModel();
        BatchModelWithTextureSignature newModel = new BatchModelWithTextureSignature(multiPartBatchModel, textureSignature);
        setupTextures(multiPartBatchModel, object);
        pages.add(newModel);
        return newModel;
    }

    @Override
    public boolean containsPart(U partReference) {
        for (BatchModelWithTextureSignature page : pages) {
            if (page.model.containsPart(partReference))
                return true;
        }
        return false;
    }

    private MultiPartBatchModel<T, U> createNewObjectBatchModel() {
        MultiPartBatchModel<T, U> multiPartBatchModel = (MultiPartBatchModel<T, U>) objectBatchModelProducer.create();
        multiPartBatchModel.setCullingTest(cullingTest);
        multiPartBatchModel.setPosition(position);
        multiPartBatchModel.setWorldTransform(worldTransform);
        return multiPartBatchModel;
    }

    @Override
    public void removePart(U partReference) {
        for (BatchModelWithTextureSignature page : pages) {
            MultiPartBatchModel<T, U> model = page.model;
            if (model.containsPart(partReference)) {
                model.removePart(partReference);
                if (model.isEmpty()) {
                    objectBatchModelProducer.dispose(model);
                    pages.removeValue(page, true);
                }
                return;
            }
        }
    }

    @Override
    public U updatePart(T part, U partReference) {
        removePart(partReference);
        return addPart(part);
    }

    @Override
    public WritablePropertyContainer getPropertyContainer() {
        return propertyContainer;
    }

    @Override
    public void setCullingTest(CullingTest cullingTest) {
        this.cullingTest = cullingTest;
        for (BatchModelWithTextureSignature page : pages) {
            page.model.setCullingTest(cullingTest);
        }
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position.set(position);
        for (BatchModelWithTextureSignature page : pages) {
            page.model.setPosition(position);
        }
    }

    @Override
    public void setWorldTransform(Matrix4 worldTransform) {
        this.worldTransform.set(worldTransform);
        for (BatchModelWithTextureSignature page : pages) {
            page.model.setWorldTransform(worldTransform);
        }

    }

    @Override
    public void dispose() {
        for (BatchModelWithTextureSignature page : pages) {
            objectBatchModelProducer.dispose(page.model);
        }
        pages.clear();
    }

    private void setupTextures(MultiPartBatchModel<T, U> multiPartBatchModel, PropertyContainer object) {
        WritablePropertyContainer propertyContainer = multiPartBatchModel.getPropertyContainer();
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

    private class BatchModelWithTextureSignature {
        private final MultiPartBatchModel<T, U> model;
        private final String textureSignature;

        public BatchModelWithTextureSignature(MultiPartBatchModel<T, U> model, String textureSignature) {
            this.model = model;
            this.textureSignature = textureSignature;
        }
    }
}
